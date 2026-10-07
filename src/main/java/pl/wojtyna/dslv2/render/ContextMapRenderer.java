package pl.wojtyna.dslv2.render;

import pl.wojtyna.dslv2.archmodel.Architecture;

import java.nio.file.Path;

public interface ContextMapRenderer {

    void render(Architecture architecture, Path output);
}
