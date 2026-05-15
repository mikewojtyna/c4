package pl.wojtyna.velocity.metrics;

import pl.wojtyna.dslv2.metrics.ArchitectureMetrics;
import pl.wojtyna.dslv2.metrics.MetricsConfig;
import pl.wojtyna.velocity.metrics.bad.LongProcessArchitecture;

public class VelocityLongProcessMetricExample {

    public static void main(String[] args) {
        var report = ArchitectureMetrics.analyze(LongProcessArchitecture.build(), MetricsConfig.defaults());
        System.out.println(report);
    }
}
