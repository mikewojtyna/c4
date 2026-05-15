package pl.wojtyna.dslv2.render.process;

import com.structurizr.Workspace;
import com.structurizr.model.Container;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.view.AutomaticLayout;
import pl.wojtyna.dslv2.archmodel.ArchElement;
import pl.wojtyna.dslv2.archmodel.Architecture;
import pl.wojtyna.dslv2.archmodel.Database;
import pl.wojtyna.dslv2.archmodel.DomainService;
import pl.wojtyna.dslv2.archmodel.Queue;
import pl.wojtyna.dslv2.archmodel.process.BusinessProcess;
import pl.wojtyna.dslv2.archmodel.process.ProcessStep;
import pl.wojtyna.dslv2.render.BusinessProcessRenderer;
import pl.wojtyna.dslv2.render.technological.StructurizrTechnologicalRenderer;

public class StructurizrBusinessProcessRenderer implements BusinessProcessRenderer {

    @Override
    public void renderInto(Workspace workspace, Architecture architecture, BusinessProcess process) {
        var model = workspace.getModel();
        SoftwareSystem system = model.getSoftwareSystemWithName(StructurizrTechnologicalRenderer.SYSTEM_NAME);
        if (system == null) {
            system = model.addSoftwareSystem(StructurizrTechnologicalRenderer.SYSTEM_NAME);
        }

        for (var step : process.steps()) {
            ensureContainer(system, step.from());
            ensureContainer(system, step.to());
        }

        var view = workspace.getViews().createDynamicView(
            system,
            viewKey(process),
            process.name());

        for (var step : process.steps()) {
            switch (step) {
                case ProcessStep.Invoke i -> {
                    var src = system.getContainerWithName(i.from().name());
                    var dst = system.getContainerWithName(i.to().name());
                    view.add(src, "invokes", dst);
                }
                case ProcessStep.Publish p -> {
                    var src = system.getContainerWithName(p.from().name());
                    var dst = system.getContainerWithName(p.to().name());
                    view.add(src, p.eventName(), dst);
                }
                case ProcessStep.Consume c -> {
                    var src = system.getContainerWithName(c.from().name());
                    var dst = system.getContainerWithName(c.to().name());
                    view.add(src, "consumes", dst);
                }
            }
        }

        view.enableAutomaticLayout(AutomaticLayout.RankDirection.LeftRight, 300, 300, 200, true);
    }

    private static void ensureContainer(SoftwareSystem system, ArchElement element) {
        if (system.getContainerWithName(element.name()) != null) {
            return;
        }
        Container container = system.addContainer(element.name());
        container.setGroup(element.boundedContext().getName());
        container.addTags(tagFor(element));
    }

    private static String tagFor(ArchElement element) {
        if (element instanceof DomainService) return "DomainService";
        if (element instanceof Database) return "Database";
        if (element instanceof Queue) return "Queue";
        return "ArchElement";
    }

    private static String viewKey(BusinessProcess process) {
        var key = process.name().toLowerCase().replaceAll("[^a-z0-9]+", "-");
        if (key.startsWith("-")) key = key.substring(1);
        if (key.endsWith("-")) key = key.substring(0, key.length() - 1);
        return "process-" + key;
    }
}
