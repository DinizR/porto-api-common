package systems.porto.api.schedule;

import systems.porto.api.spi.HostContext;

import java.util.Map;

public final class DefaultScheduledJobContext implements ScheduledJobContext {
    private final String scheduleKey;
    private final String jobId;
    private final Map<String, String> parameters;
    private final HostContext host;
    private final Map<String, String> connectors;

    public static ScheduledJobContext open(
        final ScheduledJob job,
        final String scheduleKey,
        final String jobId,
        final Map<String, String> parameters,
        final HostContext host
    ) {
        Map<String, String> connectors = job == null || job.connectors() == null ? Map.of() : job.connectors();
        return new DefaultScheduledJobContext(scheduleKey, jobId, parameters, host, connectors);
    }

    public DefaultScheduledJobContext(
        final String scheduleKey,
        final String jobId,
        final Map<String, String> parameters,
        final HostContext host
    ) {
        this(scheduleKey, jobId, parameters, host, Map.of());
    }

    public DefaultScheduledJobContext(
        final String scheduleKey,
        final String jobId,
        final Map<String, String> parameters,
        final HostContext host,
        final Map<String, String> connectors
    ) {
        this.scheduleKey = scheduleKey;
        this.jobId = jobId;
        this.parameters = parameters == null ? Map.of() : Map.copyOf(parameters);
        this.host = host;
        this.connectors = connectors == null ? Map.of() : Map.copyOf(connectors);
    }

    @Override
    public String scheduleKey() {
        return scheduleKey;
    }

    @Override
    public String jobId() {
        return jobId;
    }

    @Override
    public Map<String, String> parameters() {
        return parameters;
    }

    @Override
    public HostContext host() {
        return host;
    }

    @Override
    public <T> T requireClientAdapter(final String connectorId, final Class<T> type) {
        if (connectorId == null || connectorId.isBlank()) {
            throw new IllegalStateException("Connector id is required");
        }
        String adapterId = connectors.get(connectorId);
        if (adapterId == null || adapterId.isBlank()) {
            throw new IllegalStateException(
                "No adapter mapped for connector '" + connectorId + "' in scheduled-job config"
            );
        }
        if (host == null) {
            throw new IllegalStateException("Host context is not bound");
        }
        Object adapter = host.findClientAdapter(adapterId);
        if (adapter == null) {
            throw new IllegalStateException(
                "Client adapter '" + adapterId + "' not found for connector '" + connectorId + "'"
            );
        }
        if (!type.isInstance(adapter)) {
            throw new IllegalStateException(
                "Adapter '" + adapterId + "' for connector '" + connectorId
                    + "' does not implement " + type.getName()
                    + " (actual: " + adapter.getClass().getName() + ")"
            );
        }
        return type.cast(adapter);
    }
}
