package systems.porto.api.auth;

/**
 * Outcome of validating a JWT. Invalid results must not carry an identity.
 */
public record TokenValidationResult(
    boolean valid,
    Identity identity,
    String failureReason
) {

    public static TokenValidationResult valid(final Identity identity) {
        if (identity == null) {
            throw new IllegalArgumentException("identity is required for a valid token");
        }
        return new TokenValidationResult(true, identity, null);
    }

    public static TokenValidationResult invalid(final String reason) {
        return new TokenValidationResult(false, null, reason);
    }
}
