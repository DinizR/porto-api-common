package systems.porto.api.sql;

import systems.porto.api.spi.HostContext;
import systems.porto.context.Context;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Loads named SQL statements from a client-adapter plugin YAML
 * ({@code plugins/client-adapters/{app}/{pluginId}-{env}.yaml}, then
 * {@code plugins/client-adapters/shared/}, then the flat {@code plugins/client-adapters/} file).
 *
 * <p>Use when helpers shared across adapters need the owning plugin's SQL
 * without embedding statements in Java.
 */
public final class ClientAdapterSql {

    private ClientAdapterSql() {
    }

    public static SqlStatements load(final Context context, final String pluginId) {
        if (context == null) {
            throw new IllegalArgumentException("context is required");
        }
        if (pluginId == null || pluginId.isBlank()) {
            throw new IllegalArgumentException("pluginId is required");
        }
        Path path = resolvePath(context, pluginId.trim());
        ClientAdapterYaml yaml = ClientAdapterYaml.load(path);
        String dialect = yaml.toConfig().getConfigValue("sql.dialect").orElse("h2");
        return yaml.statementsFor(dialect);
    }

    public static Path resolvePath(final Context context, final String pluginId) {
        String fileName = pluginId + "-" + context.getEnvironment() + ".yaml";
        String application = null;
        if (context instanceof HostContext hostContext) {
            application = hostContext.getApplication();
        }
        String home = context.getHomeDirectory();
        if (application != null && !application.isBlank()) {
            Path appScoped = Paths.get(home, "plugins", "client-adapters", application, fileName);
            if (appScoped.toFile().exists()) {
                return appScoped;
            }
        }
        Path shared = Paths.get(home, "plugins", "client-adapters", "shared", fileName);
        if (shared.toFile().exists()) {
            return shared;
        }
        return Paths.get(home, "plugins", "client-adapters", fileName);
    }
}
