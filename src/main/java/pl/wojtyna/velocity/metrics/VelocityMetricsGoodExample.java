package pl.wojtyna.velocity.metrics;

import pl.wojtyna.dslv2.metrics.ArchitectureMetrics;
import pl.wojtyna.dslv2.metrics.MetricsConfig;
import pl.wojtyna.velocity.archmodel.CityBikeRentalArchitecture;
import pl.wojtyna.velocity.archmodel.CityBikeRentalContextMap;

public class VelocityMetricsGoodExample {

    public static void main(String[] args) {
        var architecture = new CityBikeRentalArchitecture(new CityBikeRentalContextMap()).architecture();
        var report = ArchitectureMetrics.analyze(architecture, MetricsConfig.defaults());
        System.out.println(report);
    }
}
