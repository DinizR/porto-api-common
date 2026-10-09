package systems.porto.api.auth;

/**
 * How the caller proves identity. Local JWT typically uses {@link #PASSWORD};
 * Keycloak typically uses {@link #AUTHORIZATION_CODE}.
 */
public enum AuthenticationGrant {
    PASSWORD,
    AUTHORIZATION_CODE,
    ACCESS_TOKEN,
    REFRESH_TOKEN
}
