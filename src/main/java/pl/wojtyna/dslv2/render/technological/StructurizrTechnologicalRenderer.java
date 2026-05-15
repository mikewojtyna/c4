package pl.wojtyna.dslv2.render.technological;

import com.structurizr.Workspace;
import com.structurizr.model.Container;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.view.AutomaticLayout;
import com.structurizr.view.Shape;
import com.structurizr.view.Styles;
import pl.wojtyna.dslv2.archmodel.ArchElement;
import pl.wojtyna.dslv2.archmodel.Architecture;
import pl.wojtyna.dslv2.archmodel.Database;
import pl.wojtyna.dslv2.archmodel.DomainService;
import pl.wojtyna.dslv2.archmodel.Queue;
import pl.wojtyna.dslv2.archmodel.RelationshipKind;
import pl.wojtyna.dslv2.render.TechnologicalRenderer;

public class StructurizrTechnologicalRenderer implements TechnologicalRenderer {

    public static final String SYSTEM_NAME = "System";

    @Override
    public void renderInto(Workspace workspace, Architecture architecture) {
        var model = workspace.getModel();
        SoftwareSystem system = model.getSoftwareSystemWithName(SYSTEM_NAME);
        if (system == null) {
            system = model.addSoftwareSystem(SYSTEM_NAME);
        }

        for (var element : architecture.elements()) {
            if (system.getContainerWithName(element.name()) != null) {
                continue;
            }
            var container = system.addContainer(element.name());
            container.setGroup(element.boundedContext().getName());
            container.addTags(tagFor(element));
        }

        for (var element : architecture.elements()) {
            var source = system.getContainerWithName(element.name());
            for (var rel : element.relationships()) {
                var target = system.getContainerWithName(rel.target().name());
                if (source != null && target != null) {
                    source.uses(target, verb(rel.kind()));
                }
            }
        }

        var views = workspace.getViews();
        var containerView = views.createContainerView(system, "Technological", "Technological perspective");
        containerView.addAllContainers();
        containerView.enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300, 200, true);

        Styles styles = views.getConfiguration().getStyles();
        styles.addElementStyle("DomainService").background("#438CD4").color("#ffffff");
        styles.addElementStyle("Database").background("#2e8b57").color("#ffffff").shape(Shape.Cylinder);
        styles.addElementStyle("Queue").background("#d4a017").color("#ffffff").shape(Shape.Pipe);
    }

    private static String tagFor(ArchElement element) {
        if (element instanceof DomainService) return "DomainService";
        if (element instanceof Database) return "Database";
        if (element instanceof Queue) return "Queue";
        return "ArchElement";
    }

    private static String verb(RelationshipKind kind) {
        return switch (kind) {
            case USES -> "uses";
            case SENDS_TO -> "sends to";
            case STORES_IN -> "stores in";
            case CONSUMES_FROM -> "consumes from";
        };
    }
}
