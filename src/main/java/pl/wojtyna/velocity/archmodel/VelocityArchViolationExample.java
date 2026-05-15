package pl.wojtyna.velocity.archmodel;

import pl.wojtyna.dslv2.archmodel.Architecture;

import java.util.HashSet;

public class VelocityArchViolationExample {

    public static void main(String[] args) {
        var contextMap = new CityBikeRentalContextMap();
        var velocity = new CityBikeRentalArchitecture(contextMap);

        // Deliberate violation: Rental is upstream of Payments in the context map,
        // so an upstream → downstream dependency must be reported as VIOLATED.
        velocity.rentalService.uses(velocity.paymentService);

        // Deliberate violation: Customer and Maintenance are NOT connected in the context map at all.
        velocity.customerService.uses(velocity.maintenanceService);

        var architecture = new Architecture(contextMap.contextMap(), new HashSet<>(velocity.elements()));
        var result = architecture.verify();
        System.out.println("Verification: " + result.status());
        if (result.violations().isEmpty()) {
            System.out.println("Violations: []");
        } else {
            result.violations().forEach(v -> System.out.println("- " + v));
        }
    }
}
