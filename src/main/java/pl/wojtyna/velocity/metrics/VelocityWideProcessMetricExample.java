package pl.wojtyna.velocity.metrics;

import pl.wojtyna.dslv2.metrics.ArchitectureMetrics;
import pl.wojtyna.dslv2.metrics.MetricsConfig;
import pl.wojtyna.velocity.metrics.bad.WideProcessArchitecture;

public class VelocityWideProcessMetricExample {

    public static void main(String[] args) {
        var report = ArchitectureMetrics.analyze(WideProcessArchitecture.build(), MetricsConfig.defaults());
        System.out.println(report);
    }
}
