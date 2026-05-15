package pl.wojtyna.dslv2.archmodel;

import org.contextmapper.contextmap.generator.model.BoundedContext;
import org.contextmapper.contextmap.generator.model.ContextMap;
import org.contextmapper.contextmap.generator.model.Relationship;
import org.contextmapper.contextmap.generator.model.UpstreamDownstreamRelationship;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import pl.wojtyna.dslv2.archmodel.process.BusinessProcess;
import pl.wojtyna.dslv2.archmodel.process.ProcessStep;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Architecture {

    private final ContextMap contextMap;
    private final Set<ArchElement> elements;
    private final List<BusinessProcess> processes = new ArrayList<>();

    public Architecture(ContextMap contextMap, Set<ArchElement> elements) {
        this.contextMap = contextMap;
        this.elements = elements;
    }

    public Architecture register(BusinessProcess process) {
        processes.add(process);
        return this;
    }

    public ContextMap contextMap() {
        return contextMap;
    }

    public Set<ArchElement> elements() {
        return Set.copyOf(elements);
    }

    public List<BusinessProcess> processes() {
        return List.copyOf(processes);
    }

    public VerificationResult verify() {
        Set<String> violations = new LinkedHashSet<>();
        for (var element : elements) {
            for (var rel : element.relationships()) {
                var srcBc = rel.source().boundedContext();
                var tgtBc = rel.target().boundedContext();
                if (srcBc.equals(tgtBc)) {
                    continue;
                }
                var cmRel = findContextMapRelationship(srcBc, tgtBc);
                if (cmRel == null) {
                    violations.add(noRelationshipViolation(rel, srcBc, tgtBc));
                } else if (cmRel instanceof UpstreamDownstreamRelationship ud) {
                    if (!(srcBc.equals(ud.getDownstreamBoundedContext())
                          && tgtBc.equals(ud.getUpstreamBoundedContext()))) {
                        violations.add(upstreamViolation(rel, ud));
                    }
                }
                // Partnership / SharedKernel are bidirectional -> always allowed
            }
        }
        return new VerificationResult(
            violations.isEmpty() ? VerificationStatus.PASSED : VerificationStatus.VIOLATED,
            violations);
    }

    public VerificationResult verifyApplication(String basePackage) {
        var reflections = new Reflections(basePackage, Scanners.TypesAnnotated);
        Set<String> violations = new LinkedHashSet<>();
        Set<Class<?>> annotatedClasses = new java.util.HashSet<>();
        annotatedClasses.addAll(reflections.getTypesAnnotatedWith(
            pl.wojtyna.dslv2.archmodel.annotation.DomainService.class));
        annotatedClasses.addAll(reflections.getTypesAnnotatedWith(
            pl.wojtyna.dslv2.archmodel.annotation.Database.class));
        annotatedClasses.addAll(reflections.getTypesAnnotatedWith(
            pl.wojtyna.dslv2.archmodel.annotation.Queue.class));

        for (var clazz : annotatedClasses) {
            var element = resolveElement(clazz);
            if (element == null) {
                violations.add("Class '" + clazz.getName()
                               + "' is annotated as architectural element but no matching element is declared in the architecture model");
                continue;
            }
            Arrays.stream(clazz.getConstructors()).findAny().ifPresent(constructor -> {
                for (var paramType : constructor.getParameterTypes()) {
                    var depElement = resolveElement(paramType);
                    if (depElement == null) {
                        continue;
                    }
                    boolean declared = element.relationships().stream()
                                              .anyMatch(r -> r.target().name().equals(depElement.name())
                                                             && r.target().boundedContext()
                                                                 .equals(depElement.boundedContext()));
                    if (!declared) {
                        violations.add(
                            clazz.getSimpleName() + " (" + element.name() + " in BC '"
                            + element.boundedContext().getName() + "') depends on "
                            + paramType.getSimpleName() + " (" + depElement.name() + " in BC '"
                            + depElement.boundedContext().getName()
                            + "') but no such relationship is declared in the architecture model");
                    }
                }
            });
        }
        return new VerificationResult(
            violations.isEmpty() ? VerificationStatus.PASSED : VerificationStatus.VIOLATED,
            violations);
    }

    public VerificationResult verifyProcesses() {
        Set<String> violations = new LinkedHashSet<>();
        for (var process : processes) {
            var steps = process.steps();
            for (int i = 0; i < steps.size(); i++) {
                var step = steps.get(i);
                if (!isStepBacked(step)) {
                    violations.add(stepViolation(process, i + 1, step));
                }
            }
        }
        return new VerificationResult(
            violations.isEmpty() ? VerificationStatus.PASSED : VerificationStatus.VIOLATED,
            violations);
    }

    private boolean isStepBacked(ProcessStep step) {
        return switch (step) {
            case ProcessStep.Invoke i -> hasRelationship(i.from(), i.to(), RelationshipKind.USES);
            case ProcessStep.Publish p -> hasRelationship(p.from(), p.to(), RelationshipKind.SENDS_TO);
            // Consume in process flow: queue -> service. Meta-model has service.consumesFrom(queue).
            case ProcessStep.Consume c -> hasRelationship(c.to(), c.from(), RelationshipKind.CONSUMES_FROM);
        };
    }

    private static boolean hasRelationship(ArchElement source, ArchElement target, RelationshipKind kind) {
        return source.relationships().stream()
                     .anyMatch(r -> r.target().equals(target) && r.kind() == kind);
    }

    private static String stepViolation(BusinessProcess process, int stepIndex, ProcessStep step) {
        return "Process '" + process.name() + "' step " + stepIndex + ": "
               + actionVerb(step) + " "
               + step.from().getClass().getSimpleName() + " '" + step.from().name() + "' in BC '"
               + step.from().boundedContext().getName() + "' -> "
               + step.to().getClass().getSimpleName() + " '" + step.to().name() + "' in BC '"
               + step.to().boundedContext().getName()
               + "' is not backed by a declared relationship in the architecture model";
    }

    private static String actionVerb(ProcessStep step) {
        return switch (step) {
            case ProcessStep.Invoke i -> "invokes";
            case ProcessStep.Publish p -> "publishes '" + p.eventName() + "' from";
            case ProcessStep.Consume c -> "consumed by";
        };
    }

    private ArchElement resolveElement(Class<?> clazz) {
        String bcName = null;
        String elementName = null;
        var ds = clazz.getAnnotation(pl.wojtyna.dslv2.archmodel.annotation.DomainService.class);
        if (ds != null) {
            bcName = ds.boundedContext();
            elementName = ds.name();
        }
        var db = clazz.getAnnotation(pl.wojtyna.dslv2.archmodel.annotation.Database.class);
        if (db != null) {
            bcName = db.boundedContext();
            elementName = db.name();
        }
        var q = clazz.getAnnotation(pl.wojtyna.dslv2.archmodel.annotation.Queue.class);
        if (q != null) {
            bcName = q.boundedContext();
            elementName = q.name();
        }
        if (bcName == null) {
            return null;
        }
        final var bcNameFinal = bcName;
        final var elementNameFinal = elementName;
        return elements.stream()
                       .filter(e -> e.name().equals(elementNameFinal)
                                    && e.boundedContext().getName().equals(bcNameFinal))
                       .findFirst()
                       .orElse(null);
    }

    private Relationship findContextMapRelationship(BoundedContext a, BoundedContext b) {
        for (var rel : contextMap.getRelationships()) {
            var p1 = rel.getFirstParticipant();
            var p2 = rel.getSecondParticipant();
            if ((p1.equals(a) && p2.equals(b)) || (p1.equals(b) && p2.equals(a))) {
                return rel;
            }
        }
        return null;
    }

    private static String noRelationshipViolation(pl.wojtyna.dslv2.archmodel.Relationship rel,
                                                  BoundedContext srcBc,
                                                  BoundedContext tgtBc) {
        return describe(rel) + " — but there is no relationship between BC '"
               + srcBc.getName() + "' and BC '" + tgtBc.getName() + "' in the context map";
    }

    private static String upstreamViolation(pl.wojtyna.dslv2.archmodel.Relationship rel,
                                            UpstreamDownstreamRelationship ud) {
        return describe(rel) + " — but BC '" + ud.getUpstreamBoundedContext().getName()
               + "' is upstream of BC '" + ud.getDownstreamBoundedContext().getName()
               + "' in the context map (upstream must not depend on downstream)";
    }

    private static String describe(pl.wojtyna.dslv2.archmodel.Relationship rel) {
        var src = rel.source();
        var tgt = rel.target();
        return src.getClass().getSimpleName() + " '" + src.name() + "' in BC '"
               + src.boundedContext().getName() + "' "
               + verb(rel.kind()) + " "
               + tgt.getClass().getSimpleName() + " '" + tgt.name() + "' in BC '"
               + tgt.boundedContext().getName() + "'";
    }

    private static String verb(RelationshipKind kind) {
        return switch (kind) {
            case USES -> "uses";
            case SENDS_TO -> "sends to";
            case STORES_IN -> "stores in";
            case CONSUMES_FROM -> "consumes from";
        };
    }
}
