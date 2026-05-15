package pl.wojtyna.dslv2.archmodel;

import org.contextmapper.contextmap.generator.model.BoundedContext;

public class Database extends ArchElement {

    public Database(BoundedContext boundedContext, String name) {
        super(boundedContext, name);
    }
}
