package pl.wojtyna.velocity.archmodel;

import com.structurizr.Workspace;
import com.structurizr.util.WorkspaceUtils;
import pl.wojtyna.dslv2.render.contextmap.GraphvizContextMapRenderer;
import pl.wojtyna.dslv2.render.process.StructurizrBusinessProcessRenderer;
import pl.wojtyna.dslv2.render.technological.StructurizrTechnologicalRenderer;

import java.io.File;
import java.nio.file.Path;

public class VelocityRenderExample {

    public static void main(String[] args) throws Exception {
        var contextMap = new CityBikeRentalContextMap();
        var architecture = new CityBikeRentalArchitecture(contextMap).architecture();

        new GraphvizContextMapRenderer().render(
            architecture, Path.of("src/main/resources/velocity/context-map.png"));

        var workspace = new Workspace("VeloCity", "VeloCity bike rental");
        new StructurizrTechnologicalRenderer().renderInto(workspace, architecture);
        for (var process : architecture.processes()) {
            new StructurizrBusinessProcessRenderer().renderInto(workspace, architecture, process);
        }

        var workspaceJson = new File("src/main/resources/velocity/workspace.json");
        workspaceJson.getParentFile().mkdirs();
        WorkspaceUtils.saveWorkspaceToJson(workspace, workspaceJson);

        System.out.println("Rendered:");
        System.out.println("- src/main/resources/velocity/context-map.png");
        System.out.println("- " + workspaceJson.getPath()
                           + " (" + workspace.getViews().getContainerViews().size() + " container view(s), "
                           + workspace.getViews().getDynamicViews().size() + " dynamic view(s))");
    }
}
