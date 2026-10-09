package systems.porto.api.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Body for {@code POST /auth/password}. Shared by every application that loads {@code auth-token}.
 */
public record UpdateAuthPasswordRequest(
    @JsonProperty("current_password") String currentPassword,
    @JsonProperty("new_password") String newPassword
) {
}
