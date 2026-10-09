package systems.porto.api.sql;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * One named SQL statement inside a dialect block of a client-adapter plugin YAML.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class NamedSqlStatement {
    private String name;
    private String sql;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(final String sql) {
        this.sql = sql;
    }
}
