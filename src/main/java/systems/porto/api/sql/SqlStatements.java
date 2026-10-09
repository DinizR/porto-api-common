package systems.porto.api.sql;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Resolved map of statement name → SQL for the active dialect.
 */
public final class SqlStatements {
    private final String dialect;
    private final Map<String, String> byName;

    public SqlStatements(final String dialect, final Map<String, String> byName) {
        this.dialect = Objects.requireNonNull(dialect, "dialect");
        this.byName = Collections.unmodifiableMap(new LinkedHashMap<>(byName));
    }

    public String dialect() {
        return dialect;
    }

    public String require(final String name) {
        String sql = byName.get(name);
        if (sql == null || sql.isBlank()) {
            throw new IllegalStateException(
                "SQL statement '" + name + "' is not defined for dialect '" + dialect + "'"
            );
        }
        return sql;
    }

    public void requireAll(final String... names) {
        for (String name : names) {
            require(name);
        }
    }

    public Map<String, String> asMap() {
        return byName;
    }
}
