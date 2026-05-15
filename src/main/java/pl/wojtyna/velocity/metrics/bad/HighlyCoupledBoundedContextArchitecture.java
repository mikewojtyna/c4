package pl.wojtyna.velocity.metrics.bad;

import pl.wojtyna.dslv2.archmodel.ArchElement;
import pl.wojtyna.dslv2.archmodel.Architecture;
import pl.wojtyna.dslv2.archmodel.DomainService;
import pl.wojtyna.dslv2.contextmap.ContextMapDsl;

import java.util.LinkedHashSet;
import java.util.Set;

import static pl.wojtyna.dslv2.contextmap.ContextMapDsl.boundedContext;

public final class HighlyCoupledBoundedContextArchitecture {

    private HighlyCoupledBoundedContextArchitecture() {
    }

    public static Architecture build() {
        var central = boundedContext("Central");
        var sat1 = boundedContext("Sat1");
        var sat2 = boundedContext("Sat2");
        var sat3 = boundedContext("Sat3");
        var sat4 = boundedContext("Sat4");
        var sat5 = boundedContext("Sat5");
        var contextMap = ContextMapDsl.contextMap(central, sat1, sat2, sat3, sat4, sat5);

        var hub = new DomainService(central, "HubService");
        Set<ArchElement> all = new LinkedHashSet<>();
        all.add(hub);
        for (var sat : new org.contextmapper.contextmap.generator.model.BoundedContext[] {sat1, sat2, sat3, sat4, sat5}) {
            var peer = new DomainService(sat, sat.getName() + "Service");
            hub.uses(peer);
            all.add(peer);
        }
        return new Architecture(contextMap, all);
    }
}
