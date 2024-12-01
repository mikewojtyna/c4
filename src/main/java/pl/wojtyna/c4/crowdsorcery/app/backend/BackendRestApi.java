package pl.wojtyna.c4.crowdsorcery.app.backend;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrowdSorceryBackend
@RestController
@RequestMapping("/api")
public class BackendRestApi {

    private InvestmentRepository investmentRepository;
    private SendgridClient sendgrid;
    private PaypalClient paypal;

    public BackendRestApi(InvestmentRepository investmentRepository, SendgridClient sendgrid, PaypalClient paypal) {
        this.investmentRepository = investmentRepository;
        this.sendgrid = sendgrid;
        this.paypal = paypal;
    }


    @PostMapping("/investments")
    public void createInvestment(@RequestBody Investment investment) {
        investmentRepository.save(investment);
        sendgrid.sendEmail("admin@crowdsorcery.com",
                           "New investment",
                           "New investment of " + investment.amount() + " from " + investment.investor());
    }
}
