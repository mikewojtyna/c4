package pl.wojtyna.velocity.archmodel;

import pl.wojtyna.dslv2.archmodel.ArchElement;
import pl.wojtyna.dslv2.archmodel.Architecture;
import pl.wojtyna.dslv2.archmodel.Database;
import pl.wojtyna.dslv2.archmodel.DomainService;
import pl.wojtyna.dslv2.archmodel.Queue;

import java.util.Set;

public class CityBikeRentalArchitecture {

    private final CityBikeRentalContextMap contextMap;

    public final DomainService customerService;
    public final DomainService rentalService;
    public final DomainService fleetService;
    public final DomainService paymentService;
    public final DomainService maintenanceService;

    public final Database customerDb;
    public final Database rentalDb;
    public final Database fleetDb;
    public final Database paymentDb;
    public final Database maintenanceDb;

    public final Queue maintenanceRequested;

    public CityBikeRentalArchitecture(CityBikeRentalContextMap contextMap) {
        this.contextMap = contextMap;

        customerService = new DomainService(contextMap.customer(), "CustomerService");
        rentalService = new DomainService(contextMap.rental(), "RentalService");
        fleetService = new DomainService(contextMap.fleet(), "FleetService");
        paymentService = new DomainService(contextMap.payments(), "PaymentService");
        maintenanceService = new DomainService(contextMap.maintenance(), "MaintenanceService");

        customerDb = new Database(contextMap.customer(), "CustomerDb");
        rentalDb = new Database(contextMap.rental(), "RentalDb");
        fleetDb = new Database(contextMap.fleet(), "FleetDb");
        paymentDb = new Database(contextMap.payments(), "PaymentDb");
        maintenanceDb = new Database(contextMap.maintenance(), "MaintenanceDb");

        maintenanceRequested = new Queue(contextMap.fleet(), "maintenance.requested");

        // Same-BC wiring
        customerService.storesIn(customerDb);
        rentalService.storesIn(rentalDb);
        fleetService.storesIn(fleetDb)
                    .sendsTo(maintenanceRequested);
        paymentService.storesIn(paymentDb);
        maintenanceService.storesIn(maintenanceDb)
                          .consumesFrom(maintenanceRequested);

        // Cross-BC wiring — all downstream → upstream (valid per context map)
        rentalService.uses(customerService) // Rental (downstream) -> Customer (upstream)
                     .uses(fleetService);    // Rental (downstream) -> Fleet (upstream)
        paymentService.uses(rentalService); // Payments (downstream) -> Rental (upstream)
    }

    public Architecture architecture() {
        return new Architecture(contextMap.contextMap(), elements());
    }

    public Set<ArchElement> elements() {
        return Set.of(
            customerService, rentalService, fleetService, paymentService, maintenanceService,
            customerDb, rentalDb, fleetDb, paymentDb, maintenanceDb,
            maintenanceRequested
        );
    }
}
