package systems.porto.api.client;

import systems.porto.adapter.client.AbstractClientAdapter;
import systems.porto.adapter.config.Config;
import systems.porto.api.sql.ClientAdapterSql;
import systems.porto.api.sql.ClientAdapterYaml;
import systems.porto.context.Context;

import java.nio.file.Path;

/**
 * Client adapter that loads {@code configs} from app-scoped YAML
 * ({@code plugins/client-adapters/{app}/{id}-{env}.yaml}), then
 * {@code plugins/client-adapters/shared/}, same path resolution as JDBC adapters.
 *
 * <p>Use for non-SQL integrations (storage, e-sign, email, HTTP APIs).
 */
public abstract class ConfiguredClientAdapter<K extends Context> extends AbstractClientAdapter<K> {

    private Config loadedConfig;

    @Override
    public void init(final K context) {
        setContext(context);
        Path path = ClientAdapterSql.resolvePath(context, id());
        ClientAdapterYaml yaml = ClientAdapterYaml.load(path);
        this.loadedConfig = yaml.toConfig();
        setConfig(this.loadedConfig);
    }

    @Override
    public Config getConfig() {
        return loadedConfig != null ? loadedConfig : super.getConfig();
    }

    protected String requiredConfig(final String key) {
        return getConfig().getConfigValue(key)
            .filter(v -> v != null && !v.isBlank())
            .orElseThrow(() -> new IllegalStateException(
                id() + " missing required config key: " + key));
    }

    protected String configOr(final String key, final String defaultValue) {
        return getConfig().getConfigValue(key)
            .filter(v -> v != null && !v.isBlank())
            .orElse(defaultValue);
    }
}
