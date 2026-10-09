package systems.porto.api.sql;

import systems.porto.adapter.client.AbstractClientAdapter;
import systems.porto.api.spi.HostContext;
import systems.porto.context.Context;

import javax.sql.DataSource;
import java.nio.file.Path;

/**
 * Client adapter base that loads {@code configs} + dialect {@code queries} from plugin YAML
 * and resolves a named {@link DataSource}.
 */
public abstract class AbstractJdbcClientAdapter<K extends Context> extends AbstractClientAdapter<K> {
    private DataSource dataSource;
    private SqlStatements sql;

    @Override
    public void init(final K context) {
        Path configPath = configPath(context);
        ClientAdapterYaml yaml = ClientAdapterYaml.load(configPath);
        setConfig(yaml.toConfig());
        setContext(context);

        String dialect = getConfig().getConfigValue("sql.dialect").orElse("h2");
        this.sql = yaml.statementsFor(dialect);
        sql.requireAll(requiredStatements());

        String datasourceId = getConfig().getConfigValue("datasource").orElse("registry");
        if (context instanceof HostContext hostContext) {
            this.dataSource = hostContext.getDataSource(datasourceId);
        }
        if (dataSource == null) {
            throw new IllegalStateException(
                "DataSource '" + datasourceId + "' is required for " + id()
            );
        }
    }

    protected DataSource dataSource() {
        return dataSource;
    }

    protected String sql(final String name) {
        return sql.require(name);
    }

    protected SqlStatements sqlStatements() {
        return sql;
    }

    /** Statement names that must exist for the configured dialect. */
    protected abstract String[] requiredStatements();

    protected Path configPath(final K context) {
        return ClientAdapterSql.resolvePath(context, id());
    }
}
