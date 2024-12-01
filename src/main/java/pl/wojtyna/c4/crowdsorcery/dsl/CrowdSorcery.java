package pl.wojtyna.c4.crowdsorcery.dsl;

import java.util.Set;

public class CrowdSorcery implements Domain {

    public final CrowdSorceryBackend crowdSorceryBackend;
    public final SinglePageApplication singlePageApplication;
    public final Database database;
    private final Borrower borrower;
    private final Investor investor;
    public static final String NAME = "CrowdSorcery";
    private final Payment payment;
    private final Email email;

    public CrowdSorcery(Payment payment, Email email) {
        this.payment = payment;
        this.email = email;
        database = new Database();
        crowdSorceryBackend = new CrowdSorceryBackend(database, email, payment);
        singlePageApplication = new SinglePageApplication(crowdSorceryBackend);
        borrower = new Borrower(singlePageApplication);
        investor = new Investor(singlePageApplication);
    }

    public Set<ArchElement> elements() {
        return Set.of(crowdSorceryBackend, singlePageApplication, borrower, investor, database);
    }

    public Set<ArchElement> dependencies() {
        return Set.of(payment, email);
    }

    @Override
    public String name() {
        return "CrowdSorcery";
    }
}
