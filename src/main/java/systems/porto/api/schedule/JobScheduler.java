package systems.porto.api.schedule;

/**
 * Host capability for provisioning triggers. Implementations live in porto-api
 * (Quartz today). Processors and scheduled-job plugins must not import the library.
 */
public interface JobScheduler {

    boolean jobRegistered(String jobId);

    void validateCron(String cronExpression);

    void schedule(JobScheduleDefinition definition);

    void unschedule(String scheduleKey);

    /**
     * Runs the registered plugin once, using this definition's parameters.
     * Does not create or change a cron trigger.
     */
    void runNow(JobScheduleDefinition definition);

    default void reschedule(final JobScheduleDefinition definition) {
        if (definition == null) {
            return;
        }
        unschedule(definition.scheduleKey());
        schedule(definition);
    }

    void clear();
}
