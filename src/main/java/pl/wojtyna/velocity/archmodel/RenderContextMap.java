package pl.wojtyna.velocity.archmodel;

import guru.nidi.graphviz.engine.Format;
import org.contextmapper.contextmap.generator.ContextMapGenerator;

import java.io.IOException;

public class RenderContextMap {

    public static void main(String[] args) throws IOException {
        new ContextMapGenerator().generateContextMapGraphic(new CityBikeRentalContextMap().contextMap(),
                                                            Format.PNG,
                                                            "target/velocity-context-map.png");
    }
}
