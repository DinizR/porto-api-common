package systems.porto.api.db;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.DirectoryResourceAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;

/**
 * Runs Liquibase for a named database against its own changelog file.
 *
 * <p>The Liquibase history {@code FILENAME} is the path relative to {@code homeDirectory}
 * (for example {@code config/registry/db/registry/changelog/db.changelog-master.yaml}),
 * so it stays stable across absolute machine paths.
 */
public final class DatabaseMigrator {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseMigrator.class);

    private DatabaseMigrator() {
    }

    /**
     * @param dataSource     target database
     * @param changeLogFile  path to {@code db.changelog-master.yaml} (absolute or relative to {@code homeDirectory})
     * @param homeDirectory  used when {@code changeLogFile} is relative; also the Liquibase search root
     */
    public static void migrate(
        final DataSource dataSource,
        final String changeLogFile,
        final String homeDirectory
    ) {
        if (changeLogFile == null || changeLogFile.isBlank()) {
            return;
        }
        Path home = Path.of(homeDirectory != null && !homeDirectory.isBlank() ? homeDirectory : ".")
            .toAbsolutePath()
            .normalize();
        Path file = Path.of(changeLogFile);
        if (!file.isAbsolute()) {
            file = home.resolve(file).normalize();
        } else {
            file = file.toAbsolutePath().normalize();
        }
        if (!Files.isRegularFile(file)) {
            throw new IllegalStateException("Liquibase changelog not found: " + file);
        }
        if (!file.startsWith(home)) {
            throw new IllegalStateException(
                "Liquibase changelog must be under home directory " + home + " but was " + file
            );
        }
        String logicalPath = home.relativize(file).toString().replace('\\', '/');
        logger.info("Running Liquibase changelog {} (home={})", logicalPath, home);
        try (Connection connection = dataSource.getConnection()) {
            Database database = DatabaseFactory.getInstance()
                .findCorrectDatabaseImplementation(new JdbcConnection(connection));
            try (Liquibase liquibase = new Liquibase(
                logicalPath,
                new DirectoryResourceAccessor(home.toFile()),
                database
            )) {
                liquibase.update();
            }
        } catch (Exception e) {
            throw new RuntimeException("Liquibase migration failed for " + logicalPath, e);
        }
    }
}
