package systems.porto.api.auth;

/**
 * How the host validates a JWT issued by an external IdP (Keycloak, …).
 */
public record ExternalTokenConstraints(
    String issuer,
    String jwksUri,
    String audience
) {
}
