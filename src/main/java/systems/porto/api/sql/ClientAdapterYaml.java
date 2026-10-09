package systems.porto.api.sql;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import systems.porto.adapter.config.Config;
import systems.porto.adapter.config.ConfigEntry;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Full client-adapter plugin YAML: {@code configs} plus dialect {@code queries}.
 *
 * <p>Loaded separately from porto-core {@link Config} so {@code queries} is not dropped
 * (and so unknown properties do not break config parsing).
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientAdapterYaml {
    private List<ConfigItem> configs = new ArrayList<>();
    private List<DialectQueryBlock> queries = new ArrayList<>();

    public List<ConfigItem> getConfigs() {
        return configs;
    }

    public void setConfigs(final List<ConfigItem> configs) {
        this.configs = configs != null ? configs : new ArrayList<>();
    }

    public List<DialectQueryBlock> getQueries() {
        return queries;
    }

    public void setQueries(final List<DialectQueryBlock> queries) {
        this.queries = queries != null ? queries : new ArrayList<>();
    }

    public static ClientAdapterYaml load(final Path path) {
        if (!Files.exists(path)) {
            throw new IllegalStateException("Client adapter YAML not found: " + path.toAbsolutePath());
        }
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            ClientAdapterYaml yaml = mapper.readValue(path.toFile(), ClientAdapterYaml.class);
            return yaml != null ? yaml : new ClientAdapterYaml();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read client adapter YAML: " + path, e);
        }
    }

    public Config toConfig() {
        List<ConfigEntry> entries = new ArrayList<>();
        for (ConfigItem item : configs) {
            if (item == null || item.getKey() == null) {
                continue;
            }
            entries.add(new ConfigEntry(item.getKey(), item.getValue(), item.getDescription()));
        }
        return new Config(entries);
    }

    public SqlStatements statementsFor(final String dialect) {
        String normalized = dialect == null || dialect.isBlank()
            ? "h2"
            : dialect.trim().toLowerCase(Locale.ROOT);
        DialectQueryBlock match = null;
        for (DialectQueryBlock block : queries) {
            if (block.getDialect() != null
                && normalized.equals(block.getDialect().trim().toLowerCase(Locale.ROOT))) {
                match = block;
                break;
            }
        }
        if (match == null) {
            throw new IllegalStateException(
                "No queries block found for sql.dialect='" + normalized + "'"
            );
        }
        Map<String, String> byName = new LinkedHashMap<>();
        for (NamedSqlStatement statement : match.getStatements()) {
            if (statement.getName() == null || statement.getName().isBlank()) {
                continue;
            }
            byName.put(statement.getName().trim(), statement.getSql() != null ? statement.getSql().trim() : "");
        }
        return new SqlStatements(normalized, byName);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ConfigItem {
        private String key;
        private String value;
        private String description;

        public String getKey() {
            return key;
        }

        public void setKey(final String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(final String value) {
            this.value = value;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(final String description) {
            this.description = description;
        }
    }
}
