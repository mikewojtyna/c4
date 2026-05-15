package pl.wojtyna.dslv2.render;

import com.structurizr.Workspace;
import pl.wojtyna.dslv2.archmodel.Architecture;

public interface TechnologicalRenderer {

    void renderInto(Workspace workspace, Architecture architecture);
}
