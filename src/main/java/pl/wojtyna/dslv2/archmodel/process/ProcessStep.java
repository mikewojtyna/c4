package pl.wojtyna.dslv2.archmodel.process;

import pl.wojtyna.dslv2.archmodel.ArchElement;
import pl.wojtyna.dslv2.archmodel.DomainService;
import pl.wojtyna.dslv2.archmodel.Queue;

public sealed interface ProcessStep {

    ArchElement from();

    ArchElement to();

    record Invoke(DomainService from, DomainService to) implements ProcessStep {
    }

    record Publish(DomainService from, Queue to, String eventName) implements ProcessStep {
    }

    record Consume(Queue from, DomainService to) implements ProcessStep {
    }
}