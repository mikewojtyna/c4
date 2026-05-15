package pl.wojtyna.velocity.app.bad;

import org.springframework.stereotype.Service;
import pl.wojtyna.dslv2.archmodel.annotation.DomainService;

// Architecturally invalid: the meta-model says Rental is upstream of Payments,
// so RentalService must NOT depend on PaymentService.
@DomainService(boundedContext = "Rental", name = "RentalService")
@Service
public class RentalService {

    private final RentalRepository rentalRepository;
    private final PaymentService paymentService;

    public RentalService(RentalRepository rentalRepository, PaymentService paymentService) {
        this.rentalRepository = rentalRepository;
        this.paymentService = paymentService;
    }

    public void rent(String rentalId, String customerId, String bikeId, double price) {
        rentalRepository.save(new Rental(rentalId, customerId, bikeId));
        paymentService.charge(customerId, price);
    }
}
