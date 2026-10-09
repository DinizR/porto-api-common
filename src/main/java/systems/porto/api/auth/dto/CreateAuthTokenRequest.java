package systems.porto.api.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Body for {@code POST /auth/token}. Shared by every application that loads {@code auth-token}.
 */
public record CreateAuthTokenRequest(
    @JsonProperty("grant_type") String grantType,
    String username,
    String password,
    @JsonProperty("refresh_token") String refreshToken,
    String code,
    @JsonProperty("redirect_uri") String redirectUri
) {
}
