package pl.wojtyna.c4.crowdsorcery.dsl;

public class Borrower extends Persona {

    public Borrower(SinglePageApplication singlePageApplication) {
        uses(singlePageApplication);
    }

    @Override
    public String name() {
        return "Borrower";
    }
}
