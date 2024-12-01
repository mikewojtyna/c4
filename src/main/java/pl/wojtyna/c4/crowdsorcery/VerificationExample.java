package pl.wojtyna.c4.crowdsorcery;

import pl.wojtyna.c4.crowdsorcery.dsl.CrowdSorcery;
import pl.wojtyna.c4.crowdsorcery.dsl.Email;
import pl.wojtyna.c4.crowdsorcery.dsl.Payment;

public class VerificationExample {

    public static void main(String[] args) {
        var payment = new Payment();
        var email = new Email();
        var crowdSorcery = new CrowdSorcery(payment, email);
        var verificationResult = crowdSorcery.crowdSorceryBackend.verifyArchitecture();
        System.out.println(verificationResult);
    }
}
