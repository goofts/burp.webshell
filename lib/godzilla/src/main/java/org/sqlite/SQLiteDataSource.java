package org.sqlite;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.sqlite.SQLiteConfig;

public class SQLiteDataSource implements DataSource {
    private SQLiteConfig config;
    private String databaseName;
    private transient PrintWriter logger;
    private int loginTimeout;
    private String url;

    public SQLiteDataSource() {
        this.loginTimeout = 1;
        this.url = JDBC.PREFIX;
        this.databaseName = "";
        this.config = new SQLiteConfig();
    }

    public SQLiteDataSource(SQLiteConfig config2) {
        this.loginTimeout = 1;
        this.url = JDBC.PREFIX;
        this.databaseName = "";
        this.config = config2;
    }

    public void setConfig(SQLiteConfig config2) {
        this.config = config2;
    }

    public SQLiteConfig getConfig() {
        return this.config;
    }

    public void setUrl(String url2) {
        this.url = url2;
    }

    public String getUrl() {
        return this.url;
    }

    public void setDatabaseName(String databaseName2) {
        this.databaseName = databaseName2;
    }

    public String getDatabaseName() {
        return this.databaseName;
    }

    public void setSharedCache(boolean enable) {
        this.config.setSharedCache(enable);
    }

    public void setLoadExtension(boolean enable) {
        this.config.enableLoadExtension(enable);
    }

    public void setReadOnly(boolean readOnly) {
        this.config.setReadOnly(readOnly);
    }

    public void setCacheSize(int numberOfPages) {
        this.config.setCacheSize(numberOfPages);
    }

    public void setCaseSensitiveLike(boolean enable) {
        this.config.enableCaseSensitiveLike(enable);
    }

    public void setCountChanges(boolean enable) {
        this.config.enableCountChanges(enable);
    }

    public void setDefaultCacheSize(int numberOfPages) {
        this.config.setDefaultCacheSize(numberOfPages);
    }

    public void setEncoding(String encoding) {
        this.config.setEncoding(SQLiteConfig.Encoding.getEncoding(encoding));
    }

    public void setEnforceForeignKeys(boolean enforce) {
        this.config.enforceForeignKeys(enforce);
    }

    public void setFullColumnNames(boolean enable) {
        this.config.enableFullColumnNames(enable);
    }

    public void setFullSync(boolean enable) {
        this.config.enableFullSync(enable);
    }

    public void setIncrementalVacuum(int numberOfPagesToBeRemoved) {
        this.config.incrementalVacuum(numberOfPagesToBeRemoved);
    }

    public void setJournalMode(String mode) {
        this.config.setJournalMode(SQLiteConfig.JournalMode.valueOf(mode));
    }

    public void setJournalSizeLimit(int limit) {
        this.config.setJounalSizeLimit(limit);
    }

    public void setLegacyFileFormat(boolean use) {
        this.config.useLegacyFileFormat(use);
    }

    public void setLockingMode(String mode) {
        this.config.setLockingMode(SQLiteConfig.LockingMode.valueOf(mode));
    }

    public void setPageSize(int numBytes) {
        this.config.setPageSize(numBytes);
    }

    public void setMaxPageCount(int numPages) {
        this.config.setMaxPageCount(numPages);
    }

    public void setReadUncommited(boolean useReadUncommitedIsolationMode) {
        this.config.setReadUncommited(useReadUncommitedIsolationMode);
    }

    public void setRecursiveTriggers(boolean enable) {
        this.config.enableRecursiveTriggers(enable);
    }

    public void setReverseUnorderedSelects(boolean enable) {
        this.config.enableReverseUnorderedSelects(enable);
    }

    public void setShortColumnNames(boolean enable) {
        this.config.enableShortColumnNames(enable);
    }

    public void setSynchronous(String mode) {
        this.config.setSynchronous(SQLiteConfig.SynchronousMode.valueOf(mode));
    }

    public void setTempStore(String storeType) {
        this.config.setTempStore(SQLiteConfig.TempStore.valueOf(storeType));
    }

    public void setTempStoreDirectory(String directoryName) {
        this.config.setTempStoreDirectory(directoryName);
    }

    public void setTransactionMode(String transactionMode) {
        this.config.setTransactionMode(transactionMode);
    }

    public void setUserVersion(int version) {
        this.config.setUserVersion(version);
    }

    @Override // javax.sql.DataSource
    public Connection getConnection() throws SQLException {
        return getConnection((String) null, (String) null);
    }

    @Override // javax.sql.DataSource
    public SQLiteConnection getConnection(String username, String password) throws SQLException {
        Properties p = this.config.toProperties();
        if (username != null) {
            p.put("user", username);
        }
        if (password != null) {
            p.put("pass", password);
        }
        return JDBC.createConnection(this.url, p);
    }

    @Override // javax.sql.CommonDataSource
    public PrintWriter getLogWriter() throws SQLException {
        return this.logger;
    }

    @Override // javax.sql.CommonDataSource
    public int getLoginTimeout() throws SQLException {
        return this.loginTimeout;
    }

    @Override // javax.sql.CommonDataSource
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException("getParentLogger");
    }

    @Override // javax.sql.CommonDataSource
    public void setLogWriter(PrintWriter out) throws SQLException {
        this.logger = out;
    }

    @Override // javax.sql.CommonDataSource
    public void setLoginTimeout(int seconds) throws SQLException {
        this.loginTimeout = seconds;
    }

    @Override // java.sql.Wrapper
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return iface.isInstance(this);
    }

    /* JADX DEBUG: Multi-variable search result rejected for r0v0, resolved type: org.sqlite.SQLiteDataSource */
    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.sql.Wrapper
    public <T> T unwrap(Class<T> cls) throws SQLException {
        return this;
    }
}
