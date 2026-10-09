package systems.porto.api.schedule;

import java.util.Map;

/**
 * Persisted schedule the host should provision. Plugins never construct Quartz types.
 *
 * @param scheduleKey unique key for this row (typically the numeric id)
 * @param jobId       {@link ScheduledJob#id()} of the plugin to run
 * @param cronExpression Quartz 6/7-field cron (seconds minutes hours day-of-month month day-of-week)
 * @param parameters  job parameters copied into {@link ScheduledJobContext}
 */
public record JobScheduleDefinition(
    String scheduleKey,
    String jobId,
    String cronExpression,
    Map<String, String> parameters
) {
}
