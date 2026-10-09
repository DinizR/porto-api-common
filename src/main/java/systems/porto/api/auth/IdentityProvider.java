package systems.porto.api.auth;

/**
 * Pluggable identity provider. Application plugins implement this; the host never
 * imports Spring Security, Quarkus security, Micronaut security, or Keycloak types.
 *
 * <p>Two intended implementations:
 * <ul>
 *   <li>{@link IdentityProviderKind#LOCAL} — authenticate local accounts, then ask the
 *       host {@link AccessTokenService} to issue and validate JWTs.</li>
 *   <li>{@link IdentityProviderKind#EXTERNAL} — Keycloak (or another IdP server). JWTs
 *       are issued by that server; the host {@link AccessTokenService} validates them
 *       with that issuer's keys.</li>
 * </ul>
 */
public interface IdentityProvider {

    /**
     * Stable plugin id (registry / YAML id).
     */
    String id();

    IdentityProviderKind kind();

    IdentityProviderCapabilities capabilities();

    /**
     * Authenticate a grant (password, authorization code, refresh, …).
     * Throws {@link UnauthorizedException} when the grant is rejected.
     */
    AuthenticationResult authenticate(AuthenticationRequest request);

    /**
     * Validate a bearer access token and resolve the identity. Used on each API call.
     */
    TokenValidationResult validateAccessToken(String rawToken);

    /**
     * End the session when the provider supports it (local token denylist or IdP logout).
     */
    default void logout(AuthenticatedPrincipal principal) {
    }

    /**
     * Replace the caller's local secret after verifying the current one. Local
     * providers persist a host-hashed secret and clear password expiry. External
     * IdPs typically leave this unimplemented.
     */
    default void changePassword(String username, String currentSecret, String newSecret) {
        throw new ForbiddenException("Password change is not supported");
    }
}
