package pl.wojtyna.velocity.metrics.bad;

import pl.wojtyna.dslv2.archmodel.Architecture;
import pl.wojtyna.dslv2.archmodel.DomainService;
import pl.wojtyna.dslv2.contextmap.ContextMapDsl;

import java.util.Set;

import static pl.wojtyna.dslv2.archmodel.process.ProcessDsl.process;
import static pl.wojtyna.dslv2.contextmap.ContextMapDsl.boundedContext;

public final class LongProcessArchitecture {

    private LongProcessArchitecture() {
    }

    public static Architecture build() {
        var bcA = boundedContext("Alpha");
        var bcB = boundedContext("Beta");
        var contextMap = ContextMapDsl.contextMap(bcA, bcB);

        var a1 = new DomainService(bcA, "A1Service");
        var a2 = new DomainService(bcA, "A2Service");
        var a3 = new DomainService(bcA, "A3Service");
        var a4 = new DomainService(bcA, "A4Service");
        var b1 = new DomainService(bcB, "B1Service");
        var b2 = new DomainService(bcB, "B2Service");
        var b3 = new DomainService(bcB, "B3Service");

        a1.uses(a2);
        a2.uses(a3);
        a3.uses(a4);
        a4.uses(b1);
        b1.uses(b2);
        b2.uses(b3);

        var architecture = new Architecture(contextMap, Set.of(a1, a2, a3, a4, b1, b2, b3));
        architecture.register(process("Long pipeline")
            .startsWith(a1).then(a2).then(a3).then(a4).then(b1).then(b2).then(b3).build());
        return architecture;
    }
}
