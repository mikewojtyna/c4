package pl.wojtyna.dslv2.metrics;

import org.contextmapper.contextmap.generator.model.BoundedContext;
import org.contextmapper.contextmap.generator.model.Partnership;
import org.contextmapper.contextmap.generator.model.SharedKernel;
import pl.wojtyna.dslv2.archmodel.ArchElement;
import pl.wojtyna.dslv2.archmodel.Architecture;
import pl.wojtyna.dslv2.archmodel.RelationshipKind;
import pl.wojtyna.dslv2.archmodel.process.BusinessProcess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class ArchitectureMetrics {

    private ArchitectureMetrics() {
    }

    public static MetricsReport analyze(Architecture architecture, MetricsConfig config) {
        var violations = new ArrayList<MetricViolation>();
        var elements = architecture.elements();
        var processes = architecture.processes();
        var incomingEdges = incomingEdgeCounts(elements);

        checkMultiBcElements(elements, config, violations);
        checkOutgoingCoupling(elements, config, violations);
        checkSinglePointOfFailure(incomingEdges, config, violations);
        checkProcessCriticalPath(processes, config, violations);
        checkElementResponsibilities(elements, config, violations);
        checkBcResponsibilities(elements, config, violations);
        checkBcCoupling(elements, config, violations);
        checkNonFavorableRelationships(architecture, config, violations);
        checkProcessLoad(elements, processes, config, violations);

        return new MetricsReport(violations);
    }

    private static Map<ArchElement, Integer> incomingEdgeCounts(Set<ArchElement> elements) {
        var counts = new HashMap<ArchElement, Integer>();
        for (var e : elements) {
            counts.putIfAbsent(e, 0);
            for (var rel : e.relationships()) {
                counts.merge(rel.target(), 1, Integer::sum);
            }
        }
        return counts;
    }

    private static void checkMultiBcElements(Set<ArchElement> elements,
                                             MetricsConfig config,
                                             List<MetricViolation> out) {
        for (var e : elements) {
            int n = e.boundedContexts().size();
            if (n > config.maxBoundedContextsPerElement()) {
                out.add(new MetricViolation(
                    Rule.MULTI_BC_ELEMENT,
                    e.name(),
                    n,
                    config.maxBoundedContextsPerElement(),
                    e.getClass().getSimpleName() + " '" + e.name() + "' belongs to "
                    + n + " bounded contexts"));
            }
        }
    }

    private static void checkOutgoingCoupling(Set<ArchElement> elements,
                                              MetricsConfig config,
                                              List<MetricViolation> out) {
        for (var e : elements) {
            int n = e.relationships().size();
            if (n > config.maxOutgoingDependenciesPerElement()) {
                out.add(new MetricViolation(
                    Rule.HIGH_OUTGOING_COUPLING,
                    e.name(),
                    n,
                    config.maxOutgoingDependenciesPerElement(),
                    e.getClass().getSimpleName() + " '" + e.name() + "' has "
                    + n + " outgoing dependencies"));
            }
        }
    }

    private static void checkSinglePointOfFailure(Map<ArchElement, Integer> incoming,
                                                  MetricsConfig config,
                                                  List<MetricViolation> out) {
        incoming.forEach((element, count) -> {
            if (count > config.maxIncomingDependenciesPerElement()) {
                out.add(new MetricViolation(
                    Rule.SINGLE_POINT_OF_FAILURE,
                    element.name(),
                    count,
                    config.maxIncomingDependenciesPerElement(),
                    element.getClass().getSimpleName() + " '" + element.name() + "' has "
                    + count + " incoming dependencies"));
            }
        });
    }

    private static void checkProcessCriticalPath(List<BusinessProcess> processes,
                                                 MetricsConfig config,
                                                 List<MetricViolation> out) {
        for (var process : processes) {
            var bcs = new LinkedHashSet<BoundedContext>();
            var els = new LinkedHashSet<ArchElement>();
            for (var step : process.steps()) {
                bcs.add(step.from().boundedContext());
                bcs.add(step.to().boundedContext());
                els.add(step.from());
                els.add(step.to());
            }
            if (bcs.size() > config.maxBoundedContextsPerProcess()) {
                out.add(new MetricViolation(
                    Rule.PROCESS_TOO_MANY_BCS,
                    process.name(),
                    bcs.size(),
                    config.maxBoundedContextsPerProcess(),
                    "Process '" + process.name() + "' crosses " + bcs.size() + " bounded contexts"));
            }
            if (els.size() > config.maxElementsPerProcess()) {
                out.add(new MetricViolation(
                    Rule.PROCESS_TOO_MANY_ELEMENTS,
                    process.name(),
                    els.size(),
                    config.maxElementsPerProcess(),
                    "Process '" + process.name() + "' touches " + els.size() + " arch elements"));
            }
        }
    }

    private static void checkElementResponsibilities(Set<ArchElement> elements,
                                                     MetricsConfig config,
                                                     List<MetricViolation> out) {
        for (var e : elements) {
            var kinds = new LinkedHashSet<RelationshipKind>();
            for (var rel : e.relationships()) {
                kinds.add(rel.kind());
            }
            if (kinds.size() > config.maxResponsibilitiesPerElement()) {
                out.add(new MetricViolation(
                    Rule.ELEMENT_TOO_MANY_RESPONSIBILITIES,
                    e.name(),
                    kinds.size(),
                    config.maxResponsibilitiesPerElement(),
                    e.getClass().getSimpleName() + " '" + e.name() + "' uses "
                    + kinds.size() + " distinct relationship kinds"));
            }
        }
    }

    private static void checkBcResponsibilities(Set<ArchElement> elements,
                                                MetricsConfig config,
                                                List<MetricViolation> out) {
        Map<BoundedContext, Integer> byBc = new HashMap<>();
        for (var e : elements) {
            byBc.merge(e.boundedContext(), 1, Integer::sum);
        }
        byBc.forEach((bc, count) -> {
            if (count > config.maxElementsPerBoundedContext()) {
                out.add(new MetricViolation(
                    Rule.BC_TOO_MANY_RESPONSIBILITIES,
                    bc.getName(),
                    count,
                    config.maxElementsPerBoundedContext(),
                    "Bounded context '" + bc.getName() + "' contains " + count + " arch elements"));
            }
        });
    }

    private static void checkBcCoupling(Set<ArchElement> elements,
                                        MetricsConfig config,
                                        List<MetricViolation> out) {
        Map<BoundedContext, Set<BoundedContext>> neighbours = new HashMap<>();
        for (var e : elements) {
            var srcBc = e.boundedContext();
            for (var rel : e.relationships()) {
                var tgtBc = rel.target().boundedContext();
                if (!srcBc.equals(tgtBc)) {
                    neighbours.computeIfAbsent(srcBc, k -> new LinkedHashSet<>()).add(tgtBc);
                    neighbours.computeIfAbsent(tgtBc, k -> new LinkedHashSet<>()).add(srcBc);
                }
            }
        }
        neighbours.forEach((bc, others) -> {
            if (others.size() > config.maxCoupledBoundedContexts()) {
                out.add(new MetricViolation(
                    Rule.BC_HIGH_COUPLING,
                    bc.getName(),
                    others.size(),
                    config.maxCoupledBoundedContexts(),
                    "Bounded context '" + bc.getName() + "' is coupled with "
                    + others.size() + " other bounded contexts"));
            }
        });
    }

    private static void checkNonFavorableRelationships(Architecture architecture,
                                                       MetricsConfig config,
                                                       List<MetricViolation> out) {
        if (!config.flagNonFavorableRelationships()) {
            return;
        }
        for (var rel : architecture.contextMap().getRelationships()) {
            String kind = null;
            if (rel instanceof Partnership) {
                kind = "Partnership";
            } else if (rel instanceof SharedKernel) {
                kind = "Shared Kernel";
            }
            if (kind == null) {
                continue;
            }
            var a = rel.getFirstParticipant().getName();
            var b = rel.getSecondParticipant().getName();
            out.add(new MetricViolation(
                Rule.NON_FAVORABLE_RELATIONSHIP,
                a + " <-> " + b,
                null,
                null,
                kind + " between '" + a + "' and '" + b + "' is generally not favored"));
        }
    }

    private static void checkProcessLoad(Set<ArchElement> elements,
                                         List<BusinessProcess> processes,
                                         MetricsConfig config,
                                         List<MetricViolation> out) {
        Map<ArchElement, Integer> elementProcessCount = new HashMap<>();
        Map<BoundedContext, Integer> bcProcessCount = new HashMap<>();
        for (var process : processes) {
            var elsInProcess = new LinkedHashSet<ArchElement>();
            var bcsInProcess = new LinkedHashSet<BoundedContext>();
            for (var step : process.steps()) {
                elsInProcess.add(step.from());
                elsInProcess.add(step.to());
                bcsInProcess.add(step.from().boundedContext());
                bcsInProcess.add(step.to().boundedContext());
            }
            for (var e : elsInProcess) {
                elementProcessCount.merge(e, 1, Integer::sum);
            }
            for (var bc : bcsInProcess) {
                bcProcessCount.merge(bc, 1, Integer::sum);
            }
        }
        elementProcessCount.forEach((element, count) -> {
            if (count > config.maxProcessesPerElement()) {
                out.add(new MetricViolation(
                    Rule.ELEMENT_TOO_MANY_PROCESSES,
                    element.name(),
                    count,
                    config.maxProcessesPerElement(),
                    element.getClass().getSimpleName() + " '" + element.name()
                    + "' participates in " + count + " business processes"));
            }
        });
        bcProcessCount.forEach((bc, count) -> {
            if (count > config.maxProcessesPerBoundedContext()) {
                out.add(new MetricViolation(
                    Rule.BC_TOO_MANY_PROCESSES,
                    bc.getName(),
                    count,
                    config.maxProcessesPerBoundedContext(),
                    "Bounded context '" + bc.getName() + "' participates in "
                    + count + " business processes"));
            }
        });
    }
}
