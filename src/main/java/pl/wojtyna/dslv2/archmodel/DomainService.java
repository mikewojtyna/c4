package pl.wojtyna.dslv2.archmodel;

import org.contextmapper.contextmap.generator.model.BoundedContext;

public class DomainService extends ArchElement {

    public DomainService(BoundedContext boundedContext, String name) {
        super(boundedContext, name);
    }

    public DomainService uses(DomainService other) {
        addRelationship(other, RelationshipKind.USES);
        return this;
    }

    public DomainService sendsTo(Queue queue) {
        addRelationship(queue, RelationshipKind.SENDS_TO);
        return this;
    }

    public DomainService storesIn(Database database) {
        addRelationship(database, RelationshipKind.STORES_IN);
        return this;
    }

    public DomainService consumesFrom(Queue queue) {
        addRelationship(queue, RelationshipKind.CONSUMES_FROM);
        return this;
    }
}
