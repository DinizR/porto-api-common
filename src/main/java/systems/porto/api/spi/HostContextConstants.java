package systems.porto.api.spi;

/**
 * Shared variable keys for {@link HostContext} (aligned with porto-api ApiContextConstants).
 */
public final class HostContextConstants {
    public static final String CONTEXT_APPLICATION_CONTEXT = "applicationContext";
    public static final String CONTEXT_DATASOURCES = "datasources";
    public static final String CONTEXT_JOB_SCHEDULER = "jobScheduler";
    public static final String CONTEXT_ACCESS_TOKEN_SERVICE = "accessTokenService";
    public static final String CONTEXT_IDENTITY_PROVIDER = "identityProvider";
    public static final String CONTEXT_SECRET_HASHER = "secretHasher";

    private HostContextConstants() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static String datasourceKey(final String id) {
        return CONTEXT_DATASOURCES + "." + id;
    }
}
