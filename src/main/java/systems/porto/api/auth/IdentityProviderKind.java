package systems.porto.api.auth;

/**
 * Where identities and tokens come from.
 */
public enum IdentityProviderKind {

    /**
     * Accounts live with the application. The host signs and validates JWTs.
     */
    LOCAL,

    /**
     * Accounts and tokens live on an IdP server (Keycloak, …). The host validates
     * JWTs issued by that server.
     */
    EXTERNAL
}
