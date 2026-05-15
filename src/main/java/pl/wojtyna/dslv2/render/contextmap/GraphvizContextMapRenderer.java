package pl.wojtyna.dslv2.render.contextmap;

import guru.nidi.graphviz.engine.Format;
import org.contextmapper.contextmap.generator.ContextMapGenerator;
import pl.wojtyna.dslv2.archmodel.Architecture;
import pl.wojtyna.dslv2.render.ContextMapRenderer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class GraphvizContextMapRenderer implements ContextMapRenderer {

    @Override
    public void render(Architecture architecture, Path output) {
        try {
            Files.createDirectories(output.toAbsolutePath().getParent());
            new ContextMapGenerator()
                .generateContextMapGraphic(architecture.contextMap(), Format.PNG, output.toString());
        } catch (IOException e) {
            throw new RuntimeException("Failed to render context map to " + output, e);
        }
    }
}
