package pl.wojtyna.c4.crowdsorcery;

import pl.wojtyna.c4.crowdsorcery.dsl.CrowdSorcery;
import pl.wojtyna.c4.crowdsorcery.dsl.Email;
import pl.wojtyna.c4.crowdsorcery.dsl.Payment;
import pl.wojtyna.c4.crowdsorcery.structurizr.DomainRenderer;

public class DslModel {

    public static void main(String[] args) throws Exception {
        var payment = new Payment();
        var email = new Email();
        var crowdSorcery = new CrowdSorcery(payment, email);
        DomainRenderer.render(crowdSorcery);
    }
}
