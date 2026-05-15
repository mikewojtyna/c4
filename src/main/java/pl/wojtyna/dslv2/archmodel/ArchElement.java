package pl.wojtyna.dslv2.archmodel;

import org.contextmapper.contextmap.generator.model.BoundedContext;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public abstract class ArchElement {

    private final BoundedContext boundedContext;
    private final String name;
    private final Set<BoundedContext> additionalContexts = new LinkedHashSet<>();
    protected final Set<Relationship> relationships = new HashSet<>();

    protected ArchElement(BoundedContext boundedContext, String name) {
        this.boundedContext = boundedContext;
        this.name = name;
    }

    public BoundedContext boundedContext() {
        return boundedContext;
    }

    public ArchElement inBoundedContext(BoundedContext bc) {
        additionalContexts.add(bc);
        return this;
    }

    public Set<BoundedContext> boundedContexts() {
        var all = new LinkedHashSet<BoundedContext>();
        all.add(boundedContext);
        all.addAll(additionalContexts);
        return Set.copyOf(all);
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
