package pl.wojtyna.velocity.metrics.bad;

import pl.wojtyna.dslv2.archmodel.ArchElement;
import pl.wojtyna.dslv2.archmodel.Architecture;
import pl.wojtyna.dslv2.archmodel.DomainService;
import pl.wojtyna.dslv2.contextmap.ContextMapDsl;

import java.util.LinkedHashSet;
import java.util.Set;

import static pl.wojtyna.dslv2.contextmap.ContextMapDsl.boundedContext;

public final class HighOutgoingCouplingArchitecture {

    private HighOutgoingCouplingArchitecture() {
    }

    public static Architecture build() {
        var bc = boundedContext("Core");
        var contextMap = ContextMapDsl.contextMap(bc);

        var hub = new DomainService(bc, "HubService");
        Set<ArchElement> all = new LinkedHashSet<>();
        all.add(hub);
        for (int i = 1; i <= 6; i++) {
            var dep = new DomainService(bc, "Service" + i);
            hub.uses(dep);
            all.add(dep);
        }
        return new Architecture(contextMap, all);
    }
}
