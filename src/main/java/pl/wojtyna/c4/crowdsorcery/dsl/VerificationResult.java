package pl.wojtyna.c4.crowdsorcery.dsl;

import java.util.Set;

public record VerificationResult(VerificationStatus status, Set<String> violations) {
}
