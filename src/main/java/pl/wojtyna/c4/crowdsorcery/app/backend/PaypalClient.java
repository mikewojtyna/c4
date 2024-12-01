package pl.wojtyna.c4.crowdsorcery.app.backend;

@Payment
public interface PaypalClient {

    void pay(String to, double amount);
}
