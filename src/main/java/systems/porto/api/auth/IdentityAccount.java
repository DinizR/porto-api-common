package systems.porto.api.auth;

import java.time.Instant;
import java.util.Map;

/**
 * Local login account. The secret hash stays in the store; this record never carries it.
 */
public record IdentityAccount(
    String subject,
    String username,
    String email,
    boolean enabled,
    Instant passwordExpiresAt,
    Map<String, String> attributes
) {

    public IdentityAccount {
        attributes = attributes == null ? Map.of() : Map.copyOf(attributes);
    }

    public boolean passwordExpired() {
        return passwordExpiresAt != null && !passwordExpiresAt.isAfter(Instant.now());
    }
}
