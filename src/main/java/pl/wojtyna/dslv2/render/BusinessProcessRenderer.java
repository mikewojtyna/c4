package pl.wojtyna.dslv2.render;

import com.structurizr.Workspace;
import pl.wojtyna.dslv2.archmodel.Architecture;
import pl.wojtyna.dslv2.archmodel.process.BusinessProcess;

public interface BusinessProcessRenderer {

    void renderInto(Workspace workspace, Architecture architecture, BusinessProcess process);
}
