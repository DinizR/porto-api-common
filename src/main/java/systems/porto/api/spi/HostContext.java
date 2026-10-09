package systems.porto.api.spi;

import systems.porto.api.auth.AccessTokenService;
import systems.porto.api.auth.IdentityProvider;
import systems.porto.api.auth.SecretHasher;
import systems.porto.api.route.RouteRegistrar;
import systems.porto.api.schedule.JobScheduler;

import javax.sql.DataSource;
import java.util.Optional;

/**
 * Host services exposed to plugins without depending on the Spring Boot host module.
 */
public interface HostContext {

    Optional<Object> getVariable(String key);

    void setVariable(String key, Object value);

    /**
     * Running application id (e.g. {@code registry}, {@code shine-media}), used for
     * app-scoped plugin directories under {@code plugins/{type}/{application}/}.
     */
    String getApplication();

    /**
     * Named datasource registered in the host context (e.g. {@code registry}).
     */
    DataSource getDataSource(String id);

    RouteRegistrar getRouteRegistrar();

    /**
     * Client adapter registered under {@code id}, or null when that plugin is not loaded.
     * Callers cast to a capability interface. The host does not expose the scheduler library.
     */
    default Object findClientAdapter(String id) {
        return null;
    }

    /**
     * Host scheduler used to provision persisted cron rows. Implementations never
     * leak Quartz (or any other library) to plugins.
     */
    default JobScheduler jobScheduler() {
        return getVariable(HostContextConstants.CONTEXT_JOB_SCHEDULER)
            .filter(JobScheduler.class::isInstance)
            .map(JobScheduler.class::cast)
            .orElseThrow(() -> new IllegalStateException("JobScheduler is not registered on the host"));
    }

    /**
     * Host JWT issuer/validator (Spring Boot, Quarkus, Micronaut, …).
     */
    default Optional<AccessTokenService> findAccessTokenService() {
        return getVariable(HostContextConstants.CONTEXT_ACCESS_TOKEN_SERVICE)
            .filter(AccessTokenService.class::isInstance)
            .map(AccessTokenService.class::cast);
    }

    default AccessTokenService accessTokenService() {
        return findAccessTokenService()
            .orElseThrow(() -> new IllegalStateException("AccessTokenService is not registered on the host"));
    }

    /**
     * Active identity provider plugin (local JWT or Keycloak-style IdP).
     */
    default Optional<IdentityProvider> findIdentityProvider() {
        return getVariable(HostContextConstants.CONTEXT_IDENTITY_PROVIDER)
            .filter(IdentityProvider.class::isInstance)
            .map(IdentityProvider.class::cast);
    }

    default IdentityProvider identityProvider() {
        return findIdentityProvider()
            .orElseThrow(() -> new IllegalStateException("IdentityProvider is not registered on the host"));
    }

    /**
     * Host password hasher used by a local {@link IdentityProvider}.
     */
    default Optional<SecretHasher> findSecretHasher() {
        return getVariable(HostContextConstants.CONTEXT_SECRET_HASHER)
            .filter(SecretHasher.class::isInstance)
            .map(SecretHasher.class::cast);
    }

    default SecretHasher secretHasher() {
        return findSecretHasher()
            .orElseThrow(() -> new IllegalStateException("SecretHasher is not registered on the host"));
    }
}
