package systems.porto.api.route;

/**
 * Host callback for HTTP route bindings.
 * Each entity REST entry adapter registers only that entity's paths at {@code start()}.
 */
public interface RouteRegistrar {

    void register(String method, String pathPattern, String processorId, String operation);
}
