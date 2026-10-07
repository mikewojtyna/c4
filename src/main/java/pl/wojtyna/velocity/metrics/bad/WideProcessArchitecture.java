package pl.wojtyna.velocity.metrics.bad;

import pl.wojtyna.dslv2.archmodel.Architecture;
import pl.wojtyna.dslv2.archmodel.DomainService;
import pl.wojtyna.dslv2.contextmap.ContextMapDsl;

import java.util.Set;

import static pl.wojtyna.dslv2.archmodel.process.ProcessDsl.process;
import static pl.wojtyna.dslv2.contextmap.ContextMapDsl.boundedContext;

public final class WideProcessArchitecture {

    private WideProcessArchitecture() {
    }

    public static Architecture build() {
        var bcA = boundedContext("Alpha");
        var bcB = boundedContext("Beta");
        var bcC = boundedContext("Gamma");
        var bcD = boundedContext("Delta");
        var bcE = boundedContext("Epsilon");
        var contextMap = ContextMapDsl.contextMap(bcA, bcB, bcC, bcD, bcE);

        var a = new DomainService(bcA, "AService");
        var b = new DomainService(bcB, "BService");
        var c = new DomainService(bcC, "CService");
        var d = new DomainService(bcD, "DService");
        var e = new DomainService(bcE, "EService");
        a.uses(b);
        b.uses(c);
        c.uses(d);
        d.uses(e);

        var architecture = new Architecture(contextMap, Set.of(a, b, c, d, e));
        architecture.register(process("Cross-galactic flow")
            .startsWith(a).then(b).then(c).then(d).then(e).build());
        return architecture;
    }
}
