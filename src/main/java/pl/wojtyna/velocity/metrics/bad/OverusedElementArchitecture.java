package pl.wojtyna.velocity.metrics.bad;

import pl.wojtyna.dslv2.archmodel.Architecture;
import pl.wojtyna.dslv2.archmodel.DomainService;
import pl.wojtyna.dslv2.contextmap.ContextMapDsl;

import java.util.Set;

import static pl.wojtyna.dslv2.archmodel.process.ProcessDsl.process;
import static pl.wojtyna.dslv2.contextmap.ContextMapDsl.boundedContext;

public final class OverusedElementArchitecture {

    private OverusedElementArchitecture() {
    }

    public static Architecture build() {
        var bc = boundedContext("Core");
        var contextMap = ContextMapDsl.contextMap(bc);

        var hot = new DomainService(bc, "HotService");
        var partner1 = new DomainService(bc, "Partner1Service");
        var partner2 = new DomainService(bc, "Partner2Service");
        var partner3 = new DomainService(bc, "Partner3Service");

        hot.uses(partner1);
        hot.uses(partner2);
        hot.uses(partner3);

        var architecture = new Architecture(contextMap, Set.of(hot, partner1, partner2, partner3));
        architecture.register(process("Flow A").startsWith(hot).then(partner1).build());
        architecture.register(process("Flow B").startsWith(hot).then(partner2).build());
        architecture.register(process("Flow C").startsWith(hot).then(partner3).build());
        return architecture;
    }
}
