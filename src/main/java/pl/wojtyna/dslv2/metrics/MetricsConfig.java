package pl.wojtyna.dslv2.metrics;

public record MetricsConfig(int maxBoundedContextsPerElement,
                            int maxOutgoingDependenciesPerElement,
                            int maxIncomingDependenciesPerElement,
                            int maxBoundedContextsPerProcess,
                            int maxElementsPerProcess,
                            int maxResponsibilitiesPerElement,
                            int maxElementsPerBoundedContext,
                            int maxCoupledBoundedContexts,
                            boolean flagNonFavorableRelationships,
                            int maxProcessesPerElement,
                            int maxProcessesPerBoundedContext) {

    public static MetricsConfig defaults() {
        return new MetricsConfig(
            1,    // maxBoundedContextsPerElement
            5,    // maxOutgoingDependenciesPerElement
            3,    // maxIncomingDependenciesPerElement
            4,    // maxBoundedContextsPerProcess
            6,    // maxElementsPerProcess
            3,    // maxResponsibilitiesPerElement
            5,    // maxElementsPerBoundedContext
            4,    // maxCoupledBoundedContexts
            true, // flagNonFavorableRelationships
            2,    // maxProcessesPerElement
            2     // maxProcessesPerBoundedContext
        );
    }

    public MetricsConfig withMaxBoundedContextsPerElement(int n) {
        return new MetricsConfig(n, maxOutgoingDependenciesPerElement, maxIncomingDependenciesPerElement,
            maxBoundedContextsPerProcess, maxElementsPerProcess, maxResponsibilitiesPerElement,
            maxElementsPerBoundedContext, maxCoupledBoundedContexts, flagNonFavorableRelationships,
            maxProcessesPerElement, maxProcessesPerBoundedContext);
    }

    public MetricsConfig withMaxOutgoingDependenciesPerElement(int n) {
        return new MetricsConfig(maxBoundedContextsPerElement, n, maxIncomingDependenciesPerElement,
            maxBoundedContextsPerProcess, maxElementsPerProcess, maxResponsibilitiesPerElement,
            maxElementsPerBoundedContext, maxCoupledBoundedContexts, flagNonFavorableRelationships,
            maxProcessesPerElement, maxProcessesPerBoundedContext);
    }

    public MetricsConfig withMaxIncomingDependenciesPerElement(int n) {
        return new MetricsConfig(maxBoundedContextsPerElement, maxOutgoingDependenciesPerElement, n,
            maxBoundedContextsPerProcess, maxElementsPerProcess, maxResponsibilitiesPerElement,
            maxElementsPerBoundedContext, maxCoupledBoundedContexts, flagNonFavorableRelationships,
            maxProcessesPerElement, maxProcessesPerBoundedContext);
    }

    public MetricsConfig withMaxBoundedContextsPerProcess(int n) {
        return new MetricsConfig(maxBoundedContextsPerElement, maxOutgoingDependenciesPerElement,
            maxIncomingDependenciesPerElement, n, maxElementsPerProcess, maxResponsibilitiesPerElement,
            maxElementsPerBoundedContext, maxCoupledBoundedContexts, flagNonFavorableRelationships,
            maxProcessesPerElement, maxProcessesPerBoundedContext);
    }

    public MetricsConfig withMaxElementsPerProcess(int n) {
        return new MetricsConfig(maxBoundedContextsPerElement, maxOutgoingDependenciesPerElement,
            maxIncomingDependenciesPerElement, maxBoundedContextsPerProcess, n, maxResponsibilitiesPerElement,
            maxElementsPerBoundedContext, maxCoupledBoundedContexts, flagNonFavorableRelationships,
            maxProcessesPerElement, maxProcessesPerBoundedContext);
    }

    public MetricsConfig withMaxResponsibilitiesPerElement(int n) {
        return new MetricsConfig(maxBoundedContextsPerElement, maxOutgoingDependenciesPerElement,
            maxIncomingDependenciesPerElement, maxBoundedContextsPerProcess, maxElementsPerProcess, n,
            maxElementsPerBoundedContext, maxCoupledBoundedContexts, flagNonFavorableRelationships,
            maxProcessesPerElement, maxProcessesPerBoundedContext);
    }

    public MetricsConfig withMaxElementsPerBoundedContext(int n) {
        return new MetricsConfig(maxBoundedContextsPerElement, maxOutgoingDependenciesPerElement,
            maxIncomingDependenciesPerElement, maxBoundedContextsPerProcess, maxElementsPerProcess,
            maxResponsibilitiesPerElement, n, maxCoupledBoundedContexts, flagNonFavorableRelationships,
            maxProcessesPerElement, maxProcessesPerBoundedContext);
    }

    public MetricsConfig withMaxCoupledBoundedContexts(int n) {
        return new MetricsConfig(maxBoundedContextsPerElement, maxOutgoingDependenciesPerElement,
            maxIncomingDependenciesPerElement, maxBoundedContextsPerProcess, maxElementsPerProcess,
            maxResponsibilitiesPerElement, maxElementsPerBoundedContext, n, flagNonFavorableRelationships,
            maxProcessesPerElement, maxProcessesPerBoundedContext);
    }

    public MetricsConfig withFlagNonFavorableRelationships(boolean flag) {
        return new MetricsConfig(maxBoundedContextsPerElement, maxOutgoingDependenciesPerElement,
            maxIncomingDependenciesPerElement, maxBoundedContextsPerProcess, maxElementsPerProcess,
            maxResponsibilitiesPerElement, maxElementsPerBoundedContext, maxCoupledBoundedContexts, flag,
            maxProcessesPerElement, maxProcessesPerBoundedContext);
    }

    public MetricsConfig withMaxProcessesPerElement(int n) {
        return new MetricsConfig(maxBoundedContextsPerElement, maxOutgoingDependenciesPerElement,
            maxIncomingDependenciesPerElement, maxBoundedContextsPerProcess, maxElementsPerProcess,
            maxResponsibilitiesPerElement, maxElementsPerBoundedContext, maxCoupledBoundedContexts,
            flagNonFavorableRelationships, n, maxProcessesPerBoundedContext);
    }

    public MetricsConfig withMaxProcessesPerBoundedContext(int n) {
        return new MetricsConfig(maxBoundedContextsPerElement, maxOutgoingDependenciesPerElement,
            maxIncomingDependenciesPerElement, maxBoundedContextsPerProcess, maxElementsPerProcess,
            maxResponsibilitiesPerElement, maxElementsPerBoundedContext, maxCoupledBoundedContexts,
            flagNonFavorableRelationships, maxProcessesPerElement, n);
    }
}
