package pl.wojtyna.velocity.metrics.bad;

import pl.wojtyna.dslv2.archmodel.ArchElement;
import pl.wojtyna.dslv2.archmodel.Architecture;
import pl.wojtyna.dslv2.archmodel.Database;
import pl.wojtyna.dslv2.archmodel.DomainService;
import pl.wojtyna.dslv2.contextmap.ContextMapDsl;

import java.util.LinkedHashSet;
import java.util.Set;

import static pl.wojtyna.dslv2.contextmap.ContextMapDsl.boundedContext;

public final class SinglePointOfFailureArchitecture {

    private SinglePointOfFailureArchitecture() {
    }

    public static Architecture build() {
        var bc = boundedContext("Core");
        var contextMap = ContextMapDsl.contextMap(bc);
        var sharedDb = new Database(bc, "MonolithDb");
        Set<ArchElement> all = new LinkedHashSet<>();
        all.add(sharedDb);
        for (int i = 1; i <= 4; i++) {
            var service = new DomainService(bc, "Service" + i);
            service.storesIn(sharedDb);
            all.add(service);
        }
        return new Architecture(contextMap, all);
    }
}
