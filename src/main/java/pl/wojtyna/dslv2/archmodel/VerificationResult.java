package pl.wojtyna.dslv2.archmodel;

import java.util.Set;

public record VerificationResult(VerificationStatus status, Set<String> violations) {
}
