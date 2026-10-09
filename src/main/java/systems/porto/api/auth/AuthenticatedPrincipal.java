package systems.porto.api.auth;

import java.time.Instant;

/**
 * Caller bound to the current {@link systems.porto.api.spi.RequestContext}.
 */
public record AuthenticatedPrincipal(
    Identity identity,
    String tokenIssuer,
    Instant authenticatedAt
) {

    public String subject() {
        return identity == null ? null : identity.subject();
    }

    public String username() {
        return identity == null ? null : identity.username();
    }

    public boolean hasProfile(final String profile) {
        return identity != null && identity.hasProfile(profile);
    }

    public boolean hasPermission(final String permission) {
        return identity != null && identity.hasPermission(permission);
    }
}
