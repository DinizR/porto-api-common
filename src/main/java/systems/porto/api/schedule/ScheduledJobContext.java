package systems.porto.api.schedule;

import systems.porto.api.spi.HostContext;

import java.util.Map;

/**
 * Runtime envelope passed to a {@link ScheduledJob} when a persisted schedule fires.
 */
public interface ScheduledJobContext {

    /** Persistence key of the schedule row (Quartz-free). */
    String scheduleKey();

    String jobId();

    Map<String, String> parameters();

    HostContext host();

    /**
     * Client adapter for a connector hook declared on the job YAML ({@code DB}, {@code EMAIL}, …).
     */
    <T> T requireClientAdapter(String connectorId, Class<T> type);
}
