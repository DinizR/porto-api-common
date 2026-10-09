package systems.porto.api.http;

/**
 * Processor response that the host writes as raw bytes (not JSON).
 */
public record BinaryHttpResponse(
    byte[] body,
    String contentType,
    String fileName
) {
    public static BinaryHttpResponse of(final byte[] body, final String contentType, final String fileName) {
        return new BinaryHttpResponse(body, contentType, fileName);
    }
}
