package pl.wojtyna.c4.crowdsorcery.dsl;

public class CrowdSorceryBackend extends ArchElement {

    public CrowdSorceryBackend(Database database, Email email, Payment payment) {
        uses(database);
        uses(email);
        uses(payment);
    }

    @Override
    public String name() {
        return "CrowdSorceryBackend";
    }
}
