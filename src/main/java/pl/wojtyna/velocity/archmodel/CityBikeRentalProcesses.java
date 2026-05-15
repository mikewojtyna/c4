package pl.wojtyna.velocity.archmodel;

import pl.wojtyna.dslv2.archmodel.process.BusinessProcess;

import static pl.wojtyna.dslv2.archmodel.process.ProcessDsl.process;

public class CityBikeRentalProcesses {

    private final CityBikeRentalArchitecture velocity;

    public CityBikeRentalProcesses(CityBikeRentalArchitecture velocity) {
        this.velocity = velocity;
    }

    public BusinessProcess rentingABike() {
        return process("Renting a bike")
            .startsWith(velocity.rentalService)
            .then(velocity.fleetService)
            .publishesTo(velocity.rentalEvents, "bike.rented")
            .consumedBy(velocity.maintenanceService)
            .build();
    }
}
