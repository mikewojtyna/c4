package pl.wojtyna.velocity.metrics.bad;

import pl.wojtyna.dslv2.archmodel.ArchElement;
import pl.wojtyna.dslv2.archmodel.Architecture;
import pl.wojtyna.dslv2.archmodel.Database;
import pl.wojtyna.dslv2.archmodel.DomainService;
import pl.wojtyna.dslv2.contextmap.ContextMapDsl;

import java.util.LinkedHashSet;
import java.util.Set;

import static pl.wojtyna.dslv2.contextmap.ContextMapDsl.boundedContext;

public final class BloatedBoundedContextArchitecture {

    private BloatedBoundedContextArchitecture() {
    }

    public static Architecture build() {
        var bc = boundedContext("MegaContext");
        var contextMap = ContextMapDsl.contextMap(bc);

        Set<ArchElement> all = new LinkedHashSet<>();
        for (int i = 1; i <= 3; i++) {
            var svc = new DomainService(bc, "Service" + i);
            var db = new Database(bc, "Db" + i);
            svc.storesIn(db);
            all.add(svc);
            all.add(db);
        }
        return new Architecture(contextMap, all);
    }
}
