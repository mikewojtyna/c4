package pl.wojtyna.velocity.metrics.bad;

import pl.wojtyna.dslv2.archmodel.Architecture;
import pl.wojtyna.dslv2.archmodel.Database;
import pl.wojtyna.dslv2.archmodel.DomainService;
import pl.wojtyna.dslv2.archmodel.Queue;
import pl.wojtyna.dslv2.contextmap.ContextMapDsl;

import java.util.Set;

import static pl.wojtyna.dslv2.contextmap.ContextMapDsl.boundedContext;

public final class OverloadedElementArchitecture {

    private OverloadedElementArchitecture() {
    }

    public static Architecture build() {
        var bc = boundedContext("Core");
        var contextMap = ContextMapDsl.contextMap(bc);

        var overloaded = new DomainService(bc, "OverloadedService");
        var helper = new DomainService(bc, "HelperService");
        var db = new Database(bc, "CoreDb");
        var queue = new Queue(bc, "core.events");

        overloaded.uses(helper).storesIn(db).sendsTo(queue).consumesFrom(queue);

        return new Architecture(contextMap, Set.of(overloaded, helper, db, queue));
    }
}
