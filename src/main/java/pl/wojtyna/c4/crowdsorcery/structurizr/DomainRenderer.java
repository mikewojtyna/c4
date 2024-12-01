package pl.wojtyna.c4.crowdsorcery.structurizr;

import com.structurizr.Workspace;
import com.structurizr.model.CreateImpliedRelationshipsUnlessSameRelationshipExistsStrategy;
import com.structurizr.model.Tags;
import com.structurizr.util.WorkspaceUtils;
import com.structurizr.view.*;
import pl.wojtyna.c4.crowdsorcery.dsl.CrowdSorcery;
import pl.wojtyna.c4.crowdsorcery.dsl.Domain;
import pl.wojtyna.c4.crowdsorcery.dsl.Persona;
import pl.wojtyna.c4.crowdsorcery.dsl.Relationship;

import java.io.File;
import java.util.HashSet;

public class DomainRenderer {

    public static void render(Domain domain) throws Exception {
        var workspace = new Workspace(domain.name(), domain.name() + " workspace");
        var model = workspace.getModel();
        model.setImpliedRelationshipsStrategy(new CreateImpliedRelationshipsUnlessSameRelationshipExistsStrategy());

        var structurizrSystem = model.addSoftwareSystem(CrowdSorcery.NAME);
        var relationships = new HashSet<Relationship>();
        domain.elements()
              .forEach(archElement -> {
                  if (archElement instanceof Persona) {
                      model.addPerson(archElement.name());
                  }
                  else {
                      structurizrSystem.addContainer(archElement.name());
                  }
                  relationships.addAll(archElement.relationships());
              });
        domain.dependencies().forEach(archElement -> {
            model.addSoftwareSystem(archElement.name()).addTags("ExternalSystem");
        });
        relationships.forEach(relationship -> {
            if (relationship.source() instanceof Persona) {
                var source = model.getPersonWithName(relationship.source().name());
                var destination = structurizrSystem.getContainerWithName(relationship.target().name());
                source.uses(destination, relationship.description());
            }
            else if (domain.dependencies().stream()
                           .anyMatch(archElement -> archElement.name().equals(relationship.target().name()))) {
                var source = structurizrSystem.getContainerWithName(relationship.source().name());
                var destination = model.getSoftwareSystemWithName(relationship.target().name());
                source.uses(destination, relationship.description());
            }
            else {
                var source = structurizrSystem.getContainerWithName(relationship.source().name());
                var destination = structurizrSystem.getContainerWithName(relationship.target().name());
                source.uses(destination, relationship.description());
            }
        });

        // create a system context diagram showing people and software systems
        ViewSet views = workspace.getViews();
        SystemContextView systemContextView = views.createSystemContextView(structurizrSystem,
                                                                                        domain.name() + " System Context",
                                                                                        domain.name() + " System Context Diagram");
        systemContextView.addAllSoftwareSystems();
        systemContextView.addAllPeople();
        systemContextView.enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom,
                                                            300,
                                                            300,
                                                            200,
                                                            true);
        // create CrowdSorcery container diagram
        var containerView = views.createContainerView(structurizrSystem,
                                                                  domain.name() + " Containers",
                                                                  domain.name() + " Containers Diagram");
        containerView.addAllInfluencers();
        containerView.addAllContainers();
        containerView.enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom,
                                                        300,
                                                        300,
                                                        200,
                                                        true);

        // add some styling to the diagram elements
        Styles styles = views.getConfiguration().getStyles();
        styles.addElementStyle("ExternalSystem").background("#808080").color("#ffffff");
        styles.addElementStyle(Tags.CONTAINER).background("#438CD4").color("#ffffff");
        styles.addElementStyle(Tags.SOFTWARE_SYSTEM).background("#1168bd").color("#ffffff");
        styles.addElementStyle(Tags.PERSON).background("#08427b").color("#ffffff").shape(Shape.Person);

        // workspace can be rendered by structurizr
        WorkspaceUtils.saveWorkspaceToJson(workspace, new File("src/main/resources/workspace.json"));
    }
}
