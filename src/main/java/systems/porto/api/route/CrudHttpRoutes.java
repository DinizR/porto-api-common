package systems.porto.api.route;

/**
 * Standard list/search/read/create/update/delete bindings for one entity REST module.
 * Register extra paths (e.g. {@code /{id}/icon}) on the same adapter <em>before</em> this helper
 * when they share a prefix with {@code /{id}}.
 */
public final class CrudHttpRoutes {

    private CrudHttpRoutes() {
    }

    public static void register(
        final RouteRegistrar registrar,
        final String apiBasePath,
        final String resourcePath,
        final String processorId
    ) {
        String base = join(apiBasePath, resourcePath);
        String op = operationPrefix(processorId);
        registrar.register("GET", base, processorId, op + "-list");
        registrar.register("GET", base + "/search", processorId, op + "-search");
        registrar.register("GET", base + "/{id}", processorId, op + "-read");
        registrar.register("POST", base, processorId, op + "-create");
        registrar.register("PUT", base + "/{id}", processorId, op + "-update");
        registrar.register("DELETE", base + "/{id}", processorId, op + "-delete");
    }

    public static String join(final String apiBasePath, final String resourcePath) {
        String base = apiBasePath == null ? "" : apiBasePath.strip();
        while (base.endsWith("/")) {
            base = base.substring(0, base.length() - 1);
        }
        String resource = resourcePath == null ? "" : resourcePath.strip();
        if (!resource.startsWith("/")) {
            resource = "/" + resource;
        }
        return base + resource;
    }

    private static String operationPrefix(final String processorId) {
        if (processorId != null && processorId.endsWith("-crud")) {
            return processorId.substring(0, processorId.length() - "-crud".length());
        }
        return processorId;
    }
}
