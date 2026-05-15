package pl.wojtyna.dslv2.archmodel.process;

public final class ProcessDsl {

    private ProcessDsl() {
    }

    public static BusinessProcessBuilder process(String name) {
        return new BusinessProcessBuilder(name);
    }
}
