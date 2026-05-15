package pl.wojtyna.dslv2.contextmap;

import org.contextmapper.contextmap.generator.model.BoundedContext;
import org.contextmapper.contextmap.generator.model.ContextMap;
import org.contextmapper.contextmap.generator.model.Partnership;
import org.contextmapper.contextmap.generator.model.SharedKernel;
import org.contextmapper.contextmap.generator.model.UpstreamDownstreamRelationship;

public final class ContextMapDsl {

    private ContextMapDsl() {
    }

    public static BoundedContext boundedContext(String name) {
        return new BoundedContext(name);
    }

    public static ContextMap contextMap(BoundedContext... boundedContexts) {
        var map = new ContextMap();
        for (var bc : boundedContexts) {
            map.addBoundedContext(bc);
        }
        return map;
    }

    public static UpstreamDownstreamRelationship upstreamDownstream(BoundedContext upstream, BoundedContext downstream) {
        return new UpstreamDownstreamRelationship(upstream, downstream);
    }

    public static Partnership partnership(BoundedContext a, BoundedContext b) {
        return new Partnership(a, b);
    }

    public static SharedKernel sharedKernel(BoundedContext a, BoundedContext b) {
        return new SharedKernel(a, b);
    }
}
