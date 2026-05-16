package pl.wojtyna.velocity.metrics.bad;

import pl.wojtyna.dslv2.archmodel.Architecture;
import pl.wojtyna.dslv2.archmodel.DomainService;
import pl.wojtyna.dslv2.contextmap.ContextMapDsl;

import java.util.Set;

import static pl.wojtyna.dslv2.archmodel.process.ProcessDsl.process;
import static pl.wojtyna.dslv2.contextmap.ContextMapDsl.boundedContext;

public final class OverusedBoundedContextArchitecture {

    private OverusedBoundedContextArchitecture() {
    }

    public static Architecture build() {
        var bc = boundedContext("Workhorse");
        var contextMap = ContextMapDsl.contextMap(bc);

        var s1 = new DomainService(bc, "S1Service");
        var s2 = new DomainService(bc, "S2Service");
        var s3 = new DomainService(bc, "S3Service");
        var s4 = new DomainService(bc, "S4Service");

        s1.uses(s2);
        s3.uses(s4);
        s1.uses(s3);

        var architecture = new Architecture(contextMap, Set.of(s1, s2, s3, s4));
        architecture.register(process("Flow 1").startsWith(s1).then(s2).build());
        architecture.register(process("Flow 2").startsWith(s3).then(s4).build());
        architecture.register(process("Flow 3").startsWith(s1).then(s3).build());
        return architecture;
    }
}
