package pl.wojtyna.dslv2.archmodel;

public record Relationship(ArchElement source, ArchElement target, RelationshipKind kind) {
}
