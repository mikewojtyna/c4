package pl.wojtyna.c4;

import com.structurizr.Workspace;
import com.structurizr.model.CreateImpliedRelationshipsUnlessSameRelationshipExistsStrategy;
import com.structurizr.model.Tags;
import com.structurizr.util.WorkspaceUtils;
import com.structurizr.view.*;

import java.io.File;

public class Model {

    public static void main(String[] args) throws Exception {
        var workspace = new Workspace("Name", "Description");
        var model = workspace.getModel();
        model.setImpliedRelationshipsStrategy(new CreateImpliedRelationshipsUnlessSameRelationshipExistsStrategy());

        // persons
        var investor = model.addPerson("Investor");
        var borrower = model.addPerson("Borrower");

        // software systems
        var crowdSorcery = model.addSoftwareSystem("CrowdSorcery", "A crowdfunding investment platform");
        var paymentSystem = model.addSoftwareSystem("Payment System", "Allows to make payments");
        paymentSystem.addTags("ExternalSystem");
        var emailSystem = model.addSoftwareSystem("E-mail System", "Sends emails to customers");
        emailSystem.addTags("ExternalSystem");

        // containers
        var singlePageApplication = crowdSorcery.addContainer("Single-Page Application",
                                                              "Allows an investor to interact with CrowdSorcery",
                                                              "React");
        var crowdSorceryBackend = crowdSorcery.addContainer("CrowdSorcery Backend",
                                                            "Handles all the logic of CrowdSorcery",
                                                            "Spring Boot");
        var database = crowdSorcery.addContainer("Database", "Stores all the data", "PostgreSQL");

        // components
        var fundraisingModule = crowdSorceryBackend.addComponent("Fundraising",
                                                                 "Allows borrowers to fund their projects and let investors to invest into assets",
                                                                 "Module");
        var loanScheduleManagementModule = crowdSorceryBackend.addComponent("Loan Schedule Management",
                                                                            "Allows borrowers to receive money according to Loan Schedule",
                                                                            "Module");
        var depositsModule = crowdSorceryBackend.addComponent("Deposits",
                                                              "Manages investor and borrower funds",
                                                              "Module");
        var interestsModule = crowdSorceryBackend.addComponent("Interests",
                                                               "Allows investors to receive interests",
                                                               "Module");
        var defaultHandlingModule = crowdSorceryBackend.addComponent("Default Handling",
                                                                     "Handles defaults",
                                                                     "Module");

        // relationships
        // container-level
        // no need to include system level relationships since we are using implied relationships strategy
        investor.uses(singlePageApplication, "invests into assets using");
        borrower.uses(singlePageApplication, "finances her projects using");
        singlePageApplication.uses(crowdSorceryBackend, "makes REST calls to");
        crowdSorceryBackend.uses(database, "reads from and writes to", "JDBC");
        crowdSorceryBackend.uses(paymentSystem, "initiates payments using");
        crowdSorceryBackend.uses(emailSystem, "sends notifications and personalized messages using");
        // component relationships
        singlePageApplication.uses(fundraisingModule, "makes REST calls to");
        singlePageApplication.uses(loanScheduleManagementModule, "makes REST calls to");
        fundraisingModule.uses(depositsModule, "stores funds");
        fundraisingModule.uses(database, "stores and reads data");
        loanScheduleManagementModule.uses(depositsModule, "transfers funds to borrower's account");
        loanScheduleManagementModule.uses(fundraisingModule, "consumes project funded/overfunded events from");
        interestsModule.uses(depositsModule, "stores deposits coming when interest is paid using");
        defaultHandlingModule.uses(interestsModule, "consumes interest paid events from");

        // create a system context diagram showing people and software systems
        ViewSet views = workspace.getViews();
        SystemContextView crowdSorcerySystemContextView = views.createSystemContextView(crowdSorcery,
                                                                                        "CrowdSorcery System Context",
                                                                                        "CrowdSorcery System Context Diagram");
        crowdSorcerySystemContextView.addAllSoftwareSystems();
        crowdSorcerySystemContextView.addAllPeople();
        crowdSorcerySystemContextView.enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom,
                                                            300,
                                                            300,
                                                            200,
                                                            true);
        // create CrowdSorcery container diagram
        var crowdSorceryContainerView = views.createContainerView(crowdSorcery,
                                                                  "CrowdSorcery Containers",
                                                                  "CrowdSorcery Containers Diagram");
        crowdSorceryContainerView.addAllInfluencers();
        crowdSorceryContainerView.addAllContainers();
        crowdSorceryContainerView.enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300, 200, true);

        // create CrowdSorcery Backend components diagram
        var crowdSorceryBackendComponentView = views.createComponentView(crowdSorceryBackend,
                                                                         "CrowdSorcery Backend Modules",
                                                                         "CrowdSorcery Backend Module Diagram");
        crowdSorceryBackendComponentView.addAllContainers();
        crowdSorceryBackendComponentView.addAllComponents();
        crowdSorceryBackendComponentView.enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom,
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
        WorkspaceUtils.saveWorkspaceToJson(workspace, new File("src/main/resources/workspace.json"));
    }
}
