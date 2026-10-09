package systems.porto.api.auth;

/**
 * Grants and token behaviour this provider supports. The host and login entry
 * adapters use this instead of knowing Keycloak vs local.
 */
public record IdentityProviderCapabilities(
    boolean password,
    boolean authorizationCode,
    boolean refreshToken,
    boolean issuesTokens,
    boolean validatesExternalTokens
) {

    /**
     * Local users: password login, host-issued JWT, optional refresh.
     */
    public static IdentityProviderCapabilities localJwt() {
        return new IdentityProviderCapabilities(true, false, true, true, false);
    }

    /**
     * Keycloak-style server: authorization code + refresh; tokens issued by the IdP.
     */
    public static IdentityProviderCapabilities externalIdp() {
        return new IdentityProviderCapabilities(false, true, true, false, true);
    }

    /**
     * External IdP that also accepts Resource Owner Password (direct access grants),
     * used for local/API tests against Keycloak.
     */
    public static IdentityProviderCapabilities externalIdpWithPassword() {
        return new IdentityProviderCapabilities(true, true, true, false, true);
    }
}
