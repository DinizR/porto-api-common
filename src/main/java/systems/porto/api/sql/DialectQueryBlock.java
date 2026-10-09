package systems.porto.api.sql;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * SQL statements for a single dialect ({@code postgres}, {@code mysql}, {@code h2}, …).
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DialectQueryBlock {
    private String dialect;
    private List<NamedSqlStatement> statements = new ArrayList<>();

    public String getDialect() {
        return dialect;
    }

    public void setDialect(final String dialect) {
        this.dialect = dialect;
    }

    public List<NamedSqlStatement> getStatements() {
        return statements;
    }

    public void setStatements(final List<NamedSqlStatement> statements) {
        this.statements = statements != null ? statements : new ArrayList<>();
    }
}
