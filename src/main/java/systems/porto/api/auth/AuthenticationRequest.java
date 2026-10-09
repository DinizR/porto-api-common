package systems.porto.api.auth;

/**
 * Grant presented to {@link IdentityProvider#authenticate}. Unused fields are null.
 */
public record AuthenticationRequest(
    AuthenticationGrant grant,
    String username,
    String secret,
    String authorizationCode,
    String redirectUri,
    String rawAccessToken,
    String rawRefreshToken
) {

    public static AuthenticationRequest password(final String username, final String secret) {
        return new AuthenticationRequest(
            AuthenticationGrant.PASSWORD, username, secret, null, null, null, null
        );
    }

    public static AuthenticationRequest authorizationCode(final String code, final String redirectUri) {
        return new AuthenticationRequest(
            AuthenticationGrant.AUTHORIZATION_CODE, null, null, code, redirectUri, null, null
        );
    }

    public static AuthenticationRequest accessToken(final String rawAccessToken) {
        return new AuthenticationRequest(
            AuthenticationGrant.ACCESS_TOKEN, null, null, null, null, rawAccessToken, null
        );
    }

    public static AuthenticationRequest refresh(final String rawRefreshToken) {
        return new AuthenticationRequest(
            AuthenticationGrant.REFRESH_TOKEN, null, null, null, null, null, rawRefreshToken
        );
    }
}
