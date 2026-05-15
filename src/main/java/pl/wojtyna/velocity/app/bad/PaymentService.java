package pl.wojtyna.velocity.app.bad;

import org.springframework.stereotype.Service;
import pl.wojtyna.dslv2.archmodel.annotation.DomainService;

@DomainService(boundedContext = "Payments", name = "PaymentService")
@Service
public class PaymentService {

    public void charge(String customerId, double amount) {
        // imagine calling some payment provider here
    }
}
