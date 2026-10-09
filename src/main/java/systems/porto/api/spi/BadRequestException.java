package systems.porto.api.spi;

/**
 * Client error (HTTP 400) for invalid input or failed business validation.
 */
public class BadRequestException extends RuntimeException {
    public BadRequestException(final String message) {
        super(message);
    }

    public BadRequestException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
