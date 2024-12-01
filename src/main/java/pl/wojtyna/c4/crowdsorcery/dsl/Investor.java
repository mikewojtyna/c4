package pl.wojtyna.c4.crowdsorcery.dsl;

public class Investor extends Persona {

    public Investor(SinglePageApplication singlePageApplication) {
        uses(singlePageApplication);
    }

    @Override
    public String name() {
        return "Investor";
    }
}
