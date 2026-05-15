package pl.wojtyna.dslv2.archmodel;

import org.contextmapper.contextmap.generator.model.BoundedContext;
import org.contextmapper.contextmap.generator.model.ContextMap;
import org.contextmapper.contextmap.generator.model.Relationship;
import org.contextmapper.contextmap.generator.model.UpstreamDownstreamRelationship;

import java.util.LinkedHashSet;
import java.util.Set;

public class Architecture {

    private final ContextMap contextMap;
    private final Set<ArchElement> elements;

    public Architecture(ContextMap contextMap, Set<ArchElement> elements) {
        this.contextMap = contextMap;
        this.elements = elements;
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
