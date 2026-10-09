package systems.porto.api.auth;

import java.time.Instant;

/**
 * Issued access token. {@code value} is the compact JWT (or opaque token) the client sends.
 */
public record AccessToken(
    String value,
    String tokenType,
    Instant issuedAt,
    Instant expiresAt,
    String issuer
) {

    public static AccessToken bearer(
        final String value,
        final Instant issuedAt,
        final Instant expiresAt,
        final String issuer
    ) {
        return new AccessToken(value, "Bearer", issuedAt, expiresAt, issuer);
    }
}
