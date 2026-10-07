package pl.wojtyna.dslv2.metrics;

public record MetricViolation(Rule rule,
                              String subject,
                              Integer observedValue,
                              Integer threshold,
                              String message) {
}
