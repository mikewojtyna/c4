package pl.wojtyna.dslv2.archmodel.process;

import java.util.List;

public record BusinessProcess(String name, List<ProcessStep> steps) {
}
