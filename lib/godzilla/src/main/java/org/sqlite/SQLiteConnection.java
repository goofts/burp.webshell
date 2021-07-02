package org.sqlite;

import com.formdev.flatlaf.demo.DemoPrefs;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.Executor;
import org.sqlite.SQLiteConfig;
import org.sqlite.core.CoreDatabaseMetaData;
import org.sqlite.core.DB;
import org.sqlite.core.NativeDB;
import org.sqlite.jdbc4.JDBC4DatabaseMetaData;

public abstract class SQLiteConnection implements Connection {
    private static final String RESOURCE_NAME_PREFIX = ":resource:";
    private final SQLiteConnectionConfig connectionConfig;
    private final DB db;
    private CoreDatabaseMetaData meta;

    public SQLiteConnection(DB db2) {
        this.meta = null;
        this.db = db2;
        this.connectionConfig = db2.getConfig().newConnectionConfig();
    }

    public SQLiteConnection(String url, String fileName) throws SQLException {
        this(url, fileName, new Properties());
    }

    public SQLiteConnection(String url, String fileName, Properties prop) throws SQLException {
        this.meta = null;
        this.db = open(url, fileName, prop);
        SQLiteConfig config = this.db.getConfig();
        this.connectionConfig = this.db.getConfig().newConnectionConfig();
        config.apply(this);
    }

    public SQLiteConnectionConfig getConnectionConfig() {
        return this.connectionConfig;
    }

    public CoreDatabaseMetaData getSQLiteDatabaseMetaData() throws SQLException {
        checkOpen();
        if (this.meta == null) {
            this.meta = new JDBC4DatabaseMetaData(this);
        }
        return this.meta;
    }

    @Override // java.sql.Connection
    public DatabaseMetaData getMetaData() throws SQLException {
        return getSQLiteDatabaseMetaData();
    }

    public String getUrl() {
        return this.db.getUrl();
    }

    public void setSchema(String schema) throws SQLException {
    }

    public String getSchema() throws SQLException {
        return null;
    }

    public void abort(Executor executor) throws SQLException {
    }

    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
    }

    public int getNetworkTimeout() throws SQLException {
        return 0;
    }

    /* access modifiers changed from: protected */
    public void checkCursor(int rst, int rsc, int rsh) throws SQLException {
        if (rst != 1003) {
            throw new SQLException("SQLite only supports TYPE_FORWARD_ONLY cursors");
        } else if (rsc != 1007) {
            throw new SQLException("SQLite only supports CONCUR_READ_ONLY cursors");
        } else if (rsh != 2) {
            throw new SQLException("SQLite only supports closing cursors at commit");
        }
    }

    /* access modifiers changed from: protected */
    public void setTransactionMode(SQLiteConfig.TransactionMode mode) {
        this.connectionConfig.setTransactionMode(mode);
    }

    @Override // java.sql.Connection
    public int getTransactionIsolation() {
        return this.connectionConfig.getTransactionIsolation();
    }

    @Override // java.sql.Connection
    public void setTransactionIsolation(int level) throws SQLException {
        checkOpen();
        switch (level) {
            case 1:
                getDatabase().exec("PRAGMA read_uncommitted = true;", getAutoCommit());
                break;
            case 8:
                getDatabase().exec("PRAGMA read_uncommitted = false;", getAutoCommit());
                break;
            default:
                throw new SQLException("SQLite supports only TRANSACTION_SERIALIZABLE and TRANSACTION_READ_UNCOMMITTED.");
        }
        this.connectionConfig.setTransactionIsolation(level);
    }

    private static DB open(String url, String origFileName, Properties props) throws SQLException {
        Properties newProps = new Properties();
        newProps.putAll(props);
        String fileName = extractPragmasFromFilename(url, origFileName, newProps);
        SQLiteConfig config = new SQLiteConfig(newProps);
        if (!fileName.isEmpty() && !":memory:".equals(fileName) && !fileName.startsWith(DemoPrefs.FILE_PREFIX) && !fileName.contains("mode=memory")) {
            if (fileName.startsWith(RESOURCE_NAME_PREFIX)) {
                String resourceName = fileName.substring(RESOURCE_NAME_PREFIX.length());
                URL resourceAddr = Thread.currentThread().getContextClassLoader().getResource(resourceName);
                if (resourceAddr == null) {
                    try {
                        resourceAddr = new URL(resourceName);
                    } catch (MalformedURLException e) {
                        throw new SQLException(String.format("resource %s not found: %s", resourceName, e));
                    }
                }
                try {
                    fileName = extractResource(resourceAddr).getAbsolutePath();
                } catch (IOException e2) {
                    throw new SQLException(String.format("failed to load %s: %s", resourceName, e2));
                }
            } else {
                File file = new File(fileName).getAbsoluteFile();
                File parent = file.getParentFile();
                if (parent == null || parent.exists()) {
                    try {
                        if (!file.exists() && file.createNewFile()) {
                            file.delete();
                        }
                        fileName = file.getAbsolutePath();
                    } catch (Exception e3) {
                        throw new SQLException("opening db: '" + fileName + "': " + e3.getMessage());
                    }
                } else {
                    File up = parent;
                    while (up != null && !up.exists()) {
                        parent = up;
                        up = up.getParentFile();
                    }
                    throw new SQLException("path to '" + fileName + "': '" + parent + "' does not exist");
                }
            }
        }
        try {
            NativeDB.load();
            DB db2 = new NativeDB(url, fileName, config);
            db2.open(fileName, config.getOpenModeFlags());
            return db2;
        } catch (Exception e4) {
            SQLException err = new SQLException("Error opening connection");
            err.initCause(e4);
            throw err;
        }
    }

    private static File extractResource(URL resourceAddr) throws IOException {
        if (resourceAddr.getProtocol().equals("file")) {
            try {
                return new File(resourceAddr.toURI());
            } catch (URISyntaxException e) {
                throw new IOException(e.getMessage());
            }
        } else {
            File dbFile = new File(new File(System.getProperty("java.io.tmpdir")).getAbsolutePath(), String.format("sqlite-jdbc-tmp-%d.db", Integer.valueOf(resourceAddr.hashCode())));
            if (dbFile.exists()) {
                if (resourceAddr.openConnection().getLastModified() < dbFile.lastModified()) {
                    return dbFile;
                }
                if (!dbFile.delete()) {
                    throw new IOException("failed to remove existing DB file: " + dbFile.getAbsolutePath());
                }
            }
            byte[] buffer = new byte[8192];
            FileOutputStream writer = new FileOutputStream(dbFile);
            InputStream reader = resourceAddr.openStream();
            while (true) {
                try {
                    int bytesRead = reader.read(buffer);
                    if (bytesRead == -1) {
                        return dbFile;
                    }
                    writer.write(buffer, 0, bytesRead);
                } finally {
                    writer.close();
                    reader.close();
                }
            }
        }
    }

    public DB getDatabase() {
        return this.db;
    }

    @Override // java.sql.Connection
    public boolean getAutoCommit() throws SQLException {
        checkOpen();
        return this.connectionConfig.isAutoCommit();
    }

    @Override // java.sql.Connection
    public void setAutoCommit(boolean ac) throws SQLException {
        checkOpen();
        if (this.connectionConfig.isAutoCommit() != ac) {
            this.connectionConfig.setAutoCommit(ac);
            this.db.exec(this.connectionConfig.isAutoCommit() ? "commit;" : this.connectionConfig.transactionPrefix(), ac);
        }
    }

    public int getBusyTimeout() {
        return this.db.getConfig().getBusyTimeout();
    }

    public void setBusyTimeout(int timeoutMillis) throws SQLException {
        this.db.getConfig().setBusyTimeout(timeoutMillis);
        this.db.busy_timeout(timeoutMillis);
    }

    public void setLimit(SQLiteLimits limit, int value) throws SQLException {
        this.db.limit(limit.getId(), value);
    }

    public void getLimit(SQLiteLimits limit) throws SQLException {
        this.db.limit(limit.getId(), -1);
    }

    @Override // java.sql.Connection
    public boolean isClosed() throws SQLException {
        return this.db.isClosed();
    }

    @Override // java.sql.Connection, java.lang.AutoCloseable
    public void close() throws SQLException {
        if (!isClosed()) {
            if (this.meta != null) {
                this.meta.close();
            }
            this.db.close();
        }
    }

    /* access modifiers changed from: protected */
    public void checkOpen() throws SQLException {
        if (isClosed()) {
            throw new SQLException("database connection closed");
        }
    }

    public String libversion() throws SQLException {
        checkOpen();
        return this.db.libversion();
    }

    @Override // java.sql.Connection
    public void commit() throws SQLException {
        checkOpen();
        if (this.connectionConfig.isAutoCommit()) {
            throw new SQLException("database in auto-commit mode");
        }
        this.db.exec("commit;", getAutoCommit());
        this.db.exec(this.connectionConfig.transactionPrefix(), getAutoCommit());
    }

    @Override // java.sql.Connection
    public void rollback() throws SQLException {
        checkOpen();
        if (this.connectionConfig.isAutoCommit()) {
            throw new SQLException("database in auto-commit mode");
        }
        this.db.exec("rollback;", getAutoCommit());
        this.db.exec(this.connectionConfig.transactionPrefix(), getAutoCommit());
    }

    public void addUpdateListener(SQLiteUpdateListener listener) {
        this.db.addUpdateListener(listener);
    }

    public void removeUpdateListener(SQLiteUpdateListener listener) {
        this.db.removeUpdateListener(listener);
    }

    public void addCommitListener(SQLiteCommitListener listener) {
        this.db.addCommitListener(listener);
    }

    public void removeCommitListener(SQLiteCommitListener listener) {
        this.db.removeCommitListener(listener);
    }

    protected static String extractPragmasFromFilename(String url, String filename, Properties prop) throws SQLException {
        int parameterDelimiter = filename.indexOf(63);
        if (parameterDelimiter == -1) {
            return filename;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(filename.substring(0, parameterDelimiter));
        int nonPragmaCount = 0;
        String[] parameters = filename.substring(parameterDelimiter + 1).split("&");
        for (int i = 0; i < parameters.length; i++) {
            String parameter = parameters[(parameters.length - 1) - i].trim();
            if (!parameter.isEmpty()) {
                String[] kvp = parameter.split("=");
                String key = kvp[0].trim().toLowerCase();
                if (!SQLiteConfig.pragmaSet.contains(key)) {
                    sb.append(nonPragmaCount == 0 ? '?' : '&');
                    sb.append(parameter);
                    nonPragmaCount++;
                } else if (kvp.length == 1) {
                    throw new SQLException(String.format("Please specify a value for PRAGMA %s in URL %s", key, url));
                } else {
                    String value = kvp[1].trim();
                    if (!value.isEmpty() && !prop.containsKey(key)) {
                        prop.setProperty(key, value);
                    }
                }
            }
        }
        return sb.toString();
    }
}
