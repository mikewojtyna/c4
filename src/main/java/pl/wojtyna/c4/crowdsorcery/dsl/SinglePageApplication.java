package pl.wojtyna.c4.crowdsorcery.dsl;

public class SinglePageApplication extends ArchElement {

    public SinglePageApplication(CrowdSorceryBackend crowdSorceryBackend) {
        uses(crowdSorceryBackend);
    }

    @Override
    public String name() {
        return "SinglePageApplication";
    }
}
