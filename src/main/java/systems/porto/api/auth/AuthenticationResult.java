package systems.porto.api.auth;

/**
 * Successful authentication. {@code refreshToken} is null when the provider does not issue one.
 */
public record AuthenticationResult(
    Identity identity,
    AccessToken accessToken,
    RefreshToken refreshToken
) {

    public static AuthenticationResult of(final Identity identity, final AccessToken accessToken) {
        return new AuthenticationResult(identity, accessToken, null);
    }
}
