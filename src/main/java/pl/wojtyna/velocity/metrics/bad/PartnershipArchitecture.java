package pl.wojtyna.velocity.metrics.bad;

import pl.wojtyna.dslv2.archmodel.Architecture;
import pl.wojtyna.dslv2.archmodel.DomainService;
import pl.wojtyna.dslv2.contextmap.ContextMapDsl;

import java.util.Set;

import static pl.wojtyna.dslv2.contextmap.ContextMapDsl.boundedContext;
import static pl.wojtyna.dslv2.contextmap.ContextMapDsl.partnership;

public final class PartnershipArchitecture {

    private PartnershipArchitecture() {
    }

    public static Architecture build() {
        var bcA = boundedContext("Sales");
        var bcB = boundedContext("Billing");
        var contextMap = ContextMapDsl.contextMap(bcA, bcB)
            .addRelationship(partnership(bcA, bcB));

        var salesService = new DomainService(bcA, "SalesService");
        var billingService = new DomainService(bcB, "BillingService");
        return new Architecture(contextMap, Set.of(salesService, billingService));
    }
}
