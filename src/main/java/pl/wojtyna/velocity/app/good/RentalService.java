package pl.wojtyna.velocity.app.good;

import org.springframework.stereotype.Service;
import pl.wojtyna.dslv2.archmodel.annotation.DomainService;

@DomainService(boundedContext = "Rental", name = "RentalService")
@Service
public class RentalService {

    private final RentalRepository rentalRepository;

    public RentalService(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    public void rent(String rentalId, String customerId, String bikeId) {
        rentalRepository.save(new Rental(rentalId, customerId, bikeId));
    }

    public Rental find(String rentalId) {
        return rentalRepository.get(rentalId);
    }
}
