package pl.wojtyna.velocity.archmodel;

import static pl.wojtyna.dslv2.archmodel.process.ProcessDsl.process;

public class VelocityProcessViolationExample {

    public static void main(String[] args) {
        var contextMap = new CityBikeRentalContextMap();
        var velocity = new CityBikeRentalArchitecture(contextMap);
        var architecture = velocity.architecture();

        // Deliberately broken process: step 2 invokes a service that the meta-model
        // does NOT declare as a dependency (fleetService has no uses(customerService)).
        architecture.register(
            process("Renting a bike (incorrect)")
                .startsWith(velocity.rentalService)
                .then(velocity.fleetService)
                .then(velocity.customerService)
                .build());

        var result = architecture.verifyProcesses();
        System.out.println("Verification: " + result.status());
        if (result.violations().isEmpty()) {
            System.out.println("Violations: []");
        } else {
            result.violations().forEach(v -> System.out.println("- " + v));
        }
    }
}
