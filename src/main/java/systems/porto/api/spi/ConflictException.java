package systems.porto.api.spi;

public class ConflictException extends RuntimeException {
    public ConflictException(final String message) {
        super(message);
    }
}
