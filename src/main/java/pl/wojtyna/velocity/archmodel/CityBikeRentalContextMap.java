package pl.wojtyna.velocity.archmodel;

import org.contextmapper.contextmap.generator.model.BoundedContext;
import org.contextmapper.contextmap.generator.model.ContextMap;

import pl.wojtyna.dslv2.contextmap.ContextMapDsl;

import static pl.wojtyna.dslv2.contextmap.ContextMapDsl.boundedContext;
import static pl.wojtyna.dslv2.contextmap.ContextMapDsl.upstreamDownstream;

public class CityBikeRentalContextMap {

    private final BoundedContext customer = boundedContext("Customer");
    private final BoundedContext fleet = boundedContext("Fleet");
    private final BoundedContext rental = boundedContext("Rental");
    private final BoundedContext payments = boundedContext("Payments");
    private final BoundedContext maintenance = boundedContext("Maintenance");
    private final ContextMap contextMap = ContextMapDsl.contextMap(customer, fleet, rental, payments, maintenance)
        .addRelationship(upstreamDownstream(customer, rental))
        .addRelationship(upstreamDownstream(fleet, rental))
        .addRelationship(upstreamDownstream(rental, payments))
        .addRelationship(upstreamDownstream(fleet, maintenance));

    public ContextMap contextMap() {
        return contextMap;
    }

    public BoundedContext customer() {
        return customer;
    }

    public BoundedContext fleet() {
        return fleet;
    }

    public BoundedContext rental() {
        return rental;
    }

    public BoundedContext payments() {
        return payments;
    }

    public BoundedContext maintenance() {
        return maintenance;
    }
}
