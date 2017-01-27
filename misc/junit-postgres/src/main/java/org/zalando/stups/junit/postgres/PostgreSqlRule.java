/**
 * Copyright (C) 2015 Zalando SE (http://tech.zalando.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.zalando.stups.junit.postgres;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.sql.DataSource;

import org.junit.rules.ExternalResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import de.flapdoodle.embed.process.config.IRuntimeConfig;
import ru.yandex.qatools.embed.postgresql.Command;
import ru.yandex.qatools.embed.postgresql.PostgresExecutable;
import ru.yandex.qatools.embed.postgresql.PostgresProcess;
import ru.yandex.qatools.embed.postgresql.PostgresStarter;
import ru.yandex.qatools.embed.postgresql.config.AbstractPostgresConfig.Credentials;
import ru.yandex.qatools.embed.postgresql.config.AbstractPostgresConfig.Net;
import ru.yandex.qatools.embed.postgresql.config.AbstractPostgresConfig.Storage;
import ru.yandex.qatools.embed.postgresql.config.AbstractPostgresConfig.Timeout;
import ru.yandex.qatools.embed.postgresql.config.PostgresConfig;
import ru.yandex.qatools.embed.postgresql.distribution.Version;

/**
 * @author jbellmann
 *
 */
public class PostgreSqlRule extends ExternalResource {

    private final Logger log = LoggerFactory.getLogger(PostgreSqlRule.class);

    public static final String SKIP_POSTGRE_SQL_RULE = "skipPostgreSqlRule";

    private PostgresProcess process;

    private Builder builder;

    private PostgreSqlRule(Builder builder) {
        this.builder = builder;
    }

    @Override
    protected void before() throws Throwable {
        if (System.getProperty(builder.skipProperty) != null) {
            log.info("Skip PostgreSqlRule because of existing property '" + builder.skipProperty + "'");
            return;
        }
        IRuntimeConfig runtimeConfig = new RuleRuntimeConfigBuilder()
                .defaults(Command.Postgres, builder.fullExtractOutput).build();

        PostgresStarter<PostgresExecutable, PostgresProcess> runtime = new PostgresStarter(RulePostgresExecutable.class,
                runtimeConfig);

        Net net = new PostgresConfig.Net("localhost", builder.port);
        Credentials c = new PostgresConfig.Credentials(builder.username, builder.password);
        Storage storage = new PostgresConfig.Storage(builder.dbName);
        PostgresConfig config = new PostgresConfig(builder.version, net, storage, new Timeout(), c);
        PostgresExecutable exec = runtime.prepare(config);
        process = exec.start();
        log.info("PostgreSQL started");
        try {
            applyScripts(config);
        } catch (IOException e) {
            e.printStackTrace();
            stopPostgres();
            throw e;
        } catch (SQLException e) {
            e.printStackTrace();
            stopPostgres();
            // rethrow exception to fail the test
            throw e;
        }
    }

    private void stopPostgres() {
        log.info("Stopping PostgreSQL ...");
        process.stop();
        log.info("PostgreSQL-Process stopped");
        process = null;
    }

    private void applyScripts(PostgresConfig config) throws SQLException, IOException {
        log.info("Apply Scripts ...");
        Connection connection = getConnection(config);
        DataSource ds = new SingleConnectionDataSource(connection, false);
        FileSystemScanner scanner = new FileSystemScanner();
        for (String location : builder.locations) {
            File directory = new File(location);
            if (directory.exists() && directory.isDirectory()) {
                Resource[] resources = scanner.scanForResources(location, "", ".sql");
                ResourceDatabasePopulator populator = new ResourceDatabasePopulator(resources);
                populator.setSeparator(builder.separator);
                populator.execute(ds);
            } else {
                // log not existing directory
            }
        }
        log.info("Scripts applied!");
    }

    protected Connection getConnection(PostgresConfig config) throws SQLException {
        // connecting to a running Postgres
        String url = String.format("jdbc:postgresql://%s:%s/%s?user=%s&password=%s", config.net().host(),
                config.net().port(), config.storage().dbName(), config.credentials().username(),
                config.credentials().password());
        return DriverManager.getConnection(url);
    }

    @Override
    protected void after() {
        if (process != null) {
            process.stop();
        }
    }

    public static class Builder {


        private int port = 5432;
        private String username = "postgres";
        private String password = "postgres";
        private String dbName = "test";
        private Version version = Version.V9_6_1;
        private List<String> locations = new LinkedList<String>();
        private boolean fullExtractOutput = false;
        private String separator = ScriptUtils.EOF_STATEMENT_SEPARATOR;
        private String skipProperty = SKIP_POSTGRE_SQL_RULE;

        public Builder withPort(int port) {
            this.port = port;
            return this;
        }

        public Builder withUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder withPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder withDbName(String dbName) {
            this.dbName = dbName;
            return this;
        }

        public Builder addScriptLocation(String location) {
            this.locations.add(location);
            return this;
        }

        public Builder withFullExtractionOutput() {
            this.fullExtractOutput = true;
            return this;
        }

        /**
         * Define a separator to use while processing the script.
         * 
         * @param separator
         *            ScriptUtils#EOF_STATEMENT_SEPARATOR
         * @return
         * @see ScriptUtils#EOF_STATEMENT_SEPARATOR
         */
        public Builder withSeparator(String separator) {
            this.separator = separator;
            return this;
        }

        public Builder skipOnProperty(String skipProperty) {
            this.skipProperty = skipProperty;
            return this;
        }

        public PostgreSqlRule build() {
            return new PostgreSqlRule(this);
        }

    }

    /**
     * taken from flyway.
     * 
     * FileSystem scanner.
     */
    private static class FileSystemScanner {
        private static final Logger LOG = LoggerFactory.getLogger(FileSystemScanner.class);

        /**
         * Scans the FileSystem for resources under the specified location,
         * starting with the specified prefix and ending with the specified
         * suffix.
         *
         * @param path
         *            The path in the filesystem to start searching.
         *            Subdirectories are also searched.
         * @param prefix
         *            The prefix of the resource names to match.
         * @param suffix
         *            The suffix of the resource names to match.
         * @return The resources that were found.
         * @throws java.io.IOException
         *             when the location could not be scanned.
         */
        Resource[] scanForResources(String path, String prefix, String suffix) throws IOException {
            LOG.debug("Scanning for filesystem resources at '" + path + "' (Prefix: '" + prefix + "', Suffix: '"
                    + suffix + "')");

            if (!new File(path).isDirectory()) {
                throw new IOException("Invalid filesystem path: " + path);
            }

            Set<Resource> resources = new TreeSet<Resource>();

            Set<String> resourceNames = findResourceNames(path, prefix, suffix);
            for (String resourceName : resourceNames) {
                resources.add(new ExtFileSystemResource(resourceName));
                LOG.debug("Found filesystem resource: " + resourceName);
            }

            return resources.toArray(new Resource[resources.size()]);
        }

        /**
         * Finds the resources names present at this location and below on the
         * classpath starting with this prefix and ending with this suffix.
         *
         * @param path
         *            The path on the classpath to scan.
         * @param prefix
         *            The filename prefix to match.
         * @param suffix
         *            The filename suffix to match.
         * @return The resource names.
         * @throws java.io.IOException
         *             when scanning this location failed.
         */
        private Set<String> findResourceNames(String path, String prefix, String suffix) throws IOException {
            Set<String> resourceNames = findResourceNamesFromFileSystem(path, new File(path));
            return filterResourceNames(resourceNames, prefix, suffix);
        }

        /**
         * Finds all the resource names contained in this file system folder.
         *
         * @param scanRootLocation
         *            The root location of the scan on disk.
         * @param folder
         *            The folder to look for resources under on disk.
         * @return The resource names;
         * @throws IOException
         *             when the folder could not be read.
         */
        @SuppressWarnings("ConstantConditions")
        private Set<String> findResourceNamesFromFileSystem(String scanRootLocation, File folder) throws IOException {
            LOG.debug("Scanning for resources in path: " + folder.getPath() + " (" + scanRootLocation + ")");

            Set<String> resourceNames = new TreeSet<String>();

            File[] files = folder.listFiles();
            for (File file : files) {
                if (file.canRead()) {
                    if (file.isDirectory()) {
                        resourceNames.addAll(findResourceNamesFromFileSystem(scanRootLocation, file));
                    } else {
                        resourceNames.add(file.getPath());
                    }
                }
            }

            return resourceNames;
        }

        /**
         * Filters this list of resource names to only include the ones whose
         * filename matches this prefix and this suffix.
         *
         * @param resourceNames
         *            The names to filter.
         * @param prefix
         *            The prefix to match.
         * @param suffix
         *            The suffix to match.
         * @return The filtered names set.
         */
        private Set<String> filterResourceNames(Set<String> resourceNames, String prefix, String suffix) {
            Set<String> filteredResourceNames = new TreeSet<String>();
            for (String resourceName : resourceNames) {
                String fileName = resourceName.substring(resourceName.lastIndexOf(File.separator) + 1);
                if (fileName.startsWith(prefix) && fileName.endsWith(suffix)
                        && (fileName.length() > (prefix + suffix).length())) {
                    filteredResourceNames.add(resourceName);
                } else {
                    LOG.debug("Filtering out resource: " + resourceName + " (filename: " + fileName + ")");
                }
            }
            return filteredResourceNames;
        }
    }

    static class ExtFileSystemResource extends FileSystemResource implements Comparable<ExtFileSystemResource> {

        ExtFileSystemResource(File file) {
            super(file);
        }

        ExtFileSystemResource(String path) {
            super(path);
        }

        // for now compare by path
        @Override
        public int compareTo(ExtFileSystemResource o) {
            return getPath().compareTo(o.getPath());
        }
    }
}
