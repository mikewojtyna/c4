package pl.wojtyna.velocity.app.good;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class InMemoryRentalRepository implements RentalRepository {

    private final Map<String, Rental> store = new HashMap<>();

    @Override
    public void save(Rental rental) {
        store.put(rental.id(), rental);
    }

    @Override
    public Rental get(String id) {
        return store.get(id);
    }
}
