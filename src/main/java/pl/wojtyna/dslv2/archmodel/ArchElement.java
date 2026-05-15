package pl.wojtyna.dslv2.archmodel;

import org.contextmapper.contextmap.generator.model.BoundedContext;

import java.util.HashSet;
import java.util.Set;

public abstract class ArchElement {

    private final BoundedContext boundedContext;
    private final String name;
    protected final Set<Relationship> relationships = new HashSet<>();

    protected ArchElement(BoundedContext boundedContext, String name) {
        this.boundedContext = boundedContext;
        this.name = name;
    }

    public BoundedContext boundedContext() {
        return boundedContext;
    }

    public String name() {
        return name;
    }

    public Set<Relationship> relationships() {
        return Set.copyOf(relationships);
    }

    protected void addRelationship(ArchElement target, RelationshipKind kind) {
        relationships.add(new Relationship(this, target, kind));
    }
}
