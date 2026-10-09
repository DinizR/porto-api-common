package systems.porto.api.schedule;

import java.util.Map;

/**
 * Work unit implemented by {@code scheduled-jobs} plugins. The host scheduler
 * (Quartz or otherwise) is not part of this contract.
 */
public interface ScheduledJob {

    /**
     * Stable id used by the management API {@code jobId} field. Must match the
     * plugin registry id.
     */
    String id();

    /**
     * Values from {@code plugins/scheduled-jobs/{app}/{id}-{env}.yaml} {@code properties}.
     * Schedule-row parameters override these when {@link #execute} runs.
     */
    default void bindProperties(Map<String, String> properties) {
    }

    /**
     * Connector hook id to client-adapter id, from the same YAML {@code connectors} section
     * processors use. Jobs resolve capabilities through these hooks, not by adapter id.
     */
    default void bindConnectors(Map<String, String> connectorIdToAdapterId) {
    }

    default Map<String, String> connectors() {
        return Map.of();
    }

    void execute(ScheduledJobContext context);
}
