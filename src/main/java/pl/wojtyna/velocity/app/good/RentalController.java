package pl.wojtyna.velocity.app.good;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public void rent(@RequestBody Rental rental) {
        rentalService.rent(rental.id(), rental.customerId(), rental.bikeId());
    }

    @GetMapping("/{id}")
    public Rental find(@PathVariable String id) {
        return rentalService.find(id);
    }
}
