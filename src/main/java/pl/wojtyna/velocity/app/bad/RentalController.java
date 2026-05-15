package pl.wojtyna.velocity.app.bad;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    private final RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @PostMapping
    public void rent(@RequestBody RentRequest request) {
        rentalService.rent(request.id(), request.customerId(), request.bikeId(), request.price());
    }

    public record RentRequest(String id, String customerId, String bikeId, double price) {
    }
}
