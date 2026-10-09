package systems.porto.api.route;

import systems.porto.adapter.config.Config;
import systems.porto.api.spi.HostContext;
import systems.porto.context.Context;

/**
 * Shared start-up helpers for entity REST entry adapters.
 * Processors must not call this — they stay transport-agnostic.
 */
public final class EntryHttpRoutes {

    private EntryHttpRoutes() {
    }

    public static RouteRegistrar registrar(final Context context) {
        if (!(context instanceof HostContext host)) {
            throw new IllegalStateException("Host context is required to register HTTP routes");
        }
        RouteRegistrar registrar = host.getRouteRegistrar();
        if (registrar == null) {
            throw new IllegalStateException("Route registrar is not available");
        }
        return registrar;
    }

    public static String apiBasePath(final Config config) {
        String value = config == null ? null : config.getConfigValue("api.base.path").orElse(null);
        return normalize(value);
    }

    private static String normalize(final String apiBasePath) {
        if (apiBasePath == null || apiBasePath.isBlank()) {
            return "/api/v1.0.0";
        }
        String base = apiBasePath.strip();
        if (!base.startsWith("/")) {
            base = "/" + base;
        }
        while (base.endsWith("/") && base.length() > 1) {
            base = base.substring(0, base.length() - 1);
        }
        return base;
    }
}
