package systems.porto.api.spi;

import systems.porto.adapter.Adapter;
import systems.porto.adapter.client.ClientAdapter;
import systems.porto.api.auth.AuthenticatedPrincipal;
import systems.porto.api.auth.UnauthorizedException;
import systems.porto.context.Context;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * Per-request context passed from the entry layer to business processors.
 */
public interface RequestContext extends Context, HostContext {

    String getOperation();

    void setResponse(Object response);

    Object getResponse();

    Optional<Integer> getPathId();

    Optional<String> getQueryParam(String name);

    int getIntQueryParam(String name, int defaultValue);

    <T> T getRequestBody(Class<T> type);

    /**
     * HTTP header lookup (case-insensitive). Empty when the invocation has no HTTP envelope.
     */
    default Optional<String> getRequestHeader(String name) {
        if (name == null || name.isBlank()) {
            return Optional.empty();
        }
        for (Map.Entry<String, String> entry : getRequestHeaders().entrySet()) {
            if (name.equalsIgnoreCase(entry.getKey())) {
                return Optional.ofNullable(entry.getValue());
            }
        }
        return Optional.empty();
    }

    /**
     * All HTTP headers for this request. Empty when the invocation has no HTTP envelope.
     */
    default Map<String, String> getRequestHeaders() {
        return Map.of();
    }

    /**
     * Raw request bytes (needed for HMAC verification). Empty when the body was not captured.
     */
    default Optional<byte[]> getRawRequestBody() {
        return Optional.empty();
    }

    ClientAdapter<Context> getClientAdapter(String id);

    /**
     * Binds connector-hook resolution for this request (typically from the processor YAML
     * {@code connectors:} section). Required before {@link #requireClientAdapter(String, Class)}
     * or {@link #requireAdapter(String, Class)}.
     */
    void bindConnectorResolver(Function<String, String> connectorIdToAdapterId);

    /**
     * Resolves a client adapter by connector hook id (e.g. {@code "DB"}) and returns it as
     * the requested capability type (JDBC persistence, REST client, FTP, …).
     */
    <T> T requireClientAdapter(String connectorId, Class<T> type);

    /**
     * Same as {@link #requireClientAdapter(String, Class)} for general {@link Adapter} plugins
     * (for example datasources or other non-client adapters registered on the host).
     */
    <T> T requireAdapter(String connectorId, Class<T> type);

    /**
     * Authenticated caller for this request. Empty when the call is anonymous.
     * Stored on the invocation, not on the shared host context.
     */
    default Optional<AuthenticatedPrincipal> principal() {
        return Optional.empty();
    }

    default void setPrincipal(AuthenticatedPrincipal principal) {
    }

    default AuthenticatedPrincipal requirePrincipal() {
        return principal().orElseThrow(() -> new UnauthorizedException("Authentication is required"));
    }
}
