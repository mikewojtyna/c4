package pl.wojtyna.velocity.metrics;

import pl.wojtyna.dslv2.metrics.ArchitectureMetrics;
import pl.wojtyna.dslv2.metrics.MetricsConfig;
import pl.wojtyna.velocity.metrics.bad.BloatedBoundedContextArchitecture;

public class VelocityBloatedBoundedContextMetricExample {

    public static void main(String[] args) {
        var report = ArchitectureMetrics.analyze(BloatedBoundedContextArchitecture.build(), MetricsConfig.defaults());
        System.out.println(report);
    }
}
