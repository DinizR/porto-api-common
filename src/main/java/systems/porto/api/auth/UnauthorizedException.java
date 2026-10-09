package systems.porto.api.auth;

/**
 * Missing or rejected credentials (HTTP 401).
 */
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(final String message) {
        super(message);
    }

    public UnauthorizedException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
