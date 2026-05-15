package pl.wojtyna.dslv2.archmodel.process;

import pl.wojtyna.dslv2.archmodel.ArchElement;
import pl.wojtyna.dslv2.archmodel.DomainService;
import pl.wojtyna.dslv2.archmodel.Queue;

import java.util.ArrayList;
import java.util.List;

public class BusinessProcessBuilder {

    private final String name;
    private final List<ProcessStep> steps = new ArrayList<>();
    private ArchElement current;

    public BusinessProcessBuilder(String name) {
        this.name = name;
    }

    public BusinessProcessBuilder startsWith(DomainService service) {
        this.current = service;
        return this;
    }

    public BusinessProcessBuilder then(DomainService next) {
        steps.add(new ProcessStep.Invoke((DomainService) current, next));
        current = next;
        return this;
    }

    public BusinessProcessBuilder publishesTo(Queue queue, String eventName) {
        steps.add(new ProcessStep.Publish((DomainService) current, queue, eventName));
        current = queue;
        return this;
    }

    public BusinessProcessBuilder consumedBy(DomainService service) {
        steps.add(new ProcessStep.Consume((Queue) current, service));
        current = service;
        return this;
    }

    public BusinessProcess build() {
        return new BusinessProcess(name, List.copyOf(steps));
    }
}