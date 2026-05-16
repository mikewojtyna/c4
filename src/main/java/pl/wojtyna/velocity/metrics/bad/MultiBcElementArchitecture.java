package pl.wojtyna.velocity.metrics.bad;

import pl.wojtyna.dslv2.archmodel.Architecture;
import pl.wojtyna.dslv2.archmodel.DomainService;
import pl.wojtyna.dslv2.contextmap.ContextMapDsl;

import java.util.Set;

import static pl.wojtyna.dslv2.contextmap.ContextMapDsl.boundedContext;

public final class MultiBcElementArchitecture {

    private MultiBcElementArchitecture() {
    }

    public static Architecture build() {
        var bcA = boundedContext("Alpha");
        var bcB = boundedContext("Beta");
        var contextMap = ContextMapDsl.contextMap(bcA, bcB);
        var shared = new DomainService(bcA, "SharedService");
        shared.inBoundedContext(bcB);
        return new Architecture(contextMap, Set.of(shared));
    }
}
