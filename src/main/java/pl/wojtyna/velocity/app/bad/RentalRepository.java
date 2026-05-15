package pl.wojtyna.velocity.app.bad;

import pl.wojtyna.dslv2.archmodel.annotation.Database;

@Database(boundedContext = "Rental", name = "RentalDb")
public interface RentalRepository {

    void save(Rental rental);

    Rental get(String id);
}
