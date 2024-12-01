package pl.wojtyna.c4.crowdsorcery.app.backend;

@Email
public interface SendgridClient {

    void sendEmail(String to, String subject, String body);
}
