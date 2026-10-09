package systems.porto.api.auth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

/**
 * Token response for {@code POST /auth/token} and {@code POST /auth/password}.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record AuthTokenResponse(
    @JsonProperty("access_token") String accessToken,
    @JsonProperty("token_type") String tokenType,
    @JsonProperty("expires_in") Long expiresIn,
    @JsonProperty("refresh_token") String refreshToken,
    String subject,
    String username,
    Set<String> profiles,
    Set<String> permissions,
    @JsonProperty("must_change_password") Boolean mustChangePassword
) {
}
