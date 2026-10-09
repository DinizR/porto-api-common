package systems.porto.api.spi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * Operation bookend logs matching porto-smart-ui {@code activity_log} /
 * {@code Start|Completed|Failed operation …}, using the same MDC keys as MPOS
 * ({@code application}, {@code session}, {@code user}, {@code correlation}).
 */
public final class OperationLog {
    private static final Logger activityLogger = LoggerFactory.getLogger("systems.porto.activity");
    private static final String MDC_APPLICATION = "application";
    private static final String MDC_MISSING = "-";

    private OperationLog() {
    }

    public static void bindUser(final String username) {
        if (username == null || username.isBlank()) {
            MDC.put("user", MDC_MISSING);
        } else {
            MDC.put("user", username);
        }
    }

    public static void start(final String operation) {
        activityLogger.info("Start operation {}", label(operation));
    }

    public static void completed(final String operation) {
        activityLogger.info("Completed operation {}", label(operation));
    }

    public static void failed(final String operation, final Throwable error) {
        activityLogger.error("Failed operation {}", label(operation), error);
    }

    private static String label(final String operation) {
        String application = MDC.get(MDC_APPLICATION);
        if (application == null || application.isBlank() || MDC_MISSING.equals(application)) {
            return operation;
        }
        if (operation != null && operation.startsWith(application + ":")) {
            return operation;
        }
        return application + ":" + operation;
    }
}
