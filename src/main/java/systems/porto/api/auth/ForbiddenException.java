package systems.porto.api.auth;

/**
 * Authenticated caller lacks the required profile or permission (HTTP 403).
 */
public class ForbiddenException extends RuntimeException {

    public ForbiddenException(final String message) {
        super(message);
    }
}
