package systems.porto.api.auth;

import java.time.Duration;
import java.util.Map;

/**
 * Host-agnostic options for {@link AccessTokenService#issue}.
 */
public record TokenIssueOptions(
    Duration timeToLive,
    String audience,
    Map<String, String> extraClaims
) {

    public TokenIssueOptions {
        extraClaims = extraClaims == null ? Map.of() : Map.copyOf(extraClaims);
    }

    public static TokenIssueOptions of(final Duration timeToLive) {
        return new TokenIssueOptions(timeToLive, null, Map.of());
    }
}
