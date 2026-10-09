package systems.porto.api.auth;

import java.time.Instant;

/**
 * Token used to obtain a new {@link AccessToken} without presenting credentials again.
 */
public record RefreshToken(
    String value,
    Instant expiresAt
) {
}
