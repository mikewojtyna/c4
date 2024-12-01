package pl.wojtyna.c4.crowdsorcery.dsl;

import java.util.HashSet;
import java.util.Set;

public abstract class ArchElement {

    protected final Set<Relationship> relationships = new HashSet<>();

    protected void uses(ArchElement target) {
        relationships.add(new Relationship(this, target, "uses"));
    }

    public Set<Relationship> relationships() {
        return Set.copyOf(relationships);
    }

    public abstract String name();
}
