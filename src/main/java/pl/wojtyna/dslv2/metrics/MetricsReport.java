package pl.wojtyna.dslv2.metrics;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record MetricsReport(List<MetricViolation> violations) {

    public MetricsReport {
        violations = List.copyOf(violations);
    }

    public boolean passed() {
        return violations.isEmpty();
    }

    public List<MetricViolation> byRule(Rule rule) {
        return violations.stream().filter(v -> v.rule() == rule).toList();
    }

    @Override
    public String toString() {
        if (violations.isEmpty()) {
            return "MetricsReport: passed (0 violations)";
        }
        Map<Rule, List<MetricViolation>> grouped = violations.stream()
            .collect(Collectors.groupingBy(MetricViolation::rule));
        var sb = new StringBuilder();
        sb.append("MetricsReport: ").append(violations.size()).append(" violation(s)\n");
        grouped.forEach((rule, list) -> {
            sb.append("  [").append(rule).append("]\n");
            for (var v : list) {
                sb.append("    - ").append(v.message());
                if (v.observedValue() != null && v.threshold() != null) {
                    sb.append(" (observed=").append(v.observedValue())
                      .append(", threshold=").append(v.threshold()).append(")");
                }
                sb.append("\n");
            }
        });
        return sb.toString();
    }
}
