package systems.porto.api.auth;

import java.util.Optional;

/**
 * Host JWT infrastructure. Implementations live in the host (Spring Boot today;
 * Quarkus, Micronaut, or others later). Plugins must not import a JWT library.
 *
 * <p>Local IdP: {@link #issue} then {@link #validate}. External IdP (Keycloak):
 * the server issues the JWT; this port only {@link #validate}s it against that
 * issuer.
 */
public interface AccessTokenService {

    /**
     * Sign an access token for a local identity. External IdP plugins do not call this.
     */
    AccessToken issue(Identity identity, TokenIssueOptions options);

    /**
     * Verify signature, issuer, expiry, and map claims to an {@link Identity}.
     * Local HMAC tokens use this. External IdP tokens use {@link #validateExternal}.
     */
    TokenValidationResult validate(String rawToken);

    /**
     * Verify an IdP-issued JWT against that issuer's JWKS. Host implementations
     * (Nimbus today) own the JWT library; plugins must not import one.
     */
    default TokenValidationResult validateExternal(String rawToken, ExternalTokenConstraints constraints) {
        throw new UnsupportedOperationException("External token validation is not implemented");
    }

    /**
     * Reads {@code Bearer <token>} from an Authorization header. Empty when absent
     * or not a bearer token.
     */
    default Optional<String> extractBearer(final String authorizationHeader) {
        if (authorizationHeader == null || authorizationHeader.isBlank()) {
            return Optional.empty();
        }
        String value = authorizationHeader.trim();
        if (value.length() > 7 && value.regionMatches(true, 0, "Bearer ", 0, 7)) {
            String token = value.substring(7).trim();
            return token.isEmpty() ? Optional.empty() : Optional.of(token);
        }
        return Optional.empty();
    }
}
