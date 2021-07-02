package org.sqlite.javax;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.sql.ConnectionEvent;
import javax.sql.ConnectionEventListener;
import org.sqlite.SQLiteConnection;
import org.sqlite.core.DB;
import org.sqlite.jdbc4.JDBC4PreparedStatement;
import org.sqlite.jdbc4.JDBC4Statement;

/* compiled from: SQLitePooledConnection */
class SQLitePooledConnectionHandle extends SQLiteConnection {
    private final AtomicBoolean isClosed = new AtomicBoolean(false);
    private final SQLitePooledConnection parent;

    public SQLitePooledConnectionHandle(SQLitePooledConnection parent2) {
        super(parent2.getPhysicalConn().getDatabase());
        this.parent = parent2;
    }

    @Override // java.sql.Connection
    public Statement createStatement() throws SQLException {
        return new JDBC4Statement(this);
    }

    @Override // java.sql.Connection
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return new JDBC4PreparedStatement(this, sql);
    }

    @Override // java.sql.Connection
    public CallableStatement prepareCall(String sql) throws SQLException {
        return null;
    }

    @Override // java.sql.Connection
    public String nativeSQL(String sql) throws SQLException {
        return null;
    }

    @Override // org.sqlite.SQLiteConnection, java.sql.Connection
    public void setAutoCommit(boolean autoCommit) throws SQLException {
    }

    @Override // org.sqlite.SQLiteConnection, java.sql.Connection
    public boolean getAutoCommit() throws SQLException {
        return false;
    }

    @Override // org.sqlite.SQLiteConnection, java.sql.Connection
    public void commit() throws SQLException {
    }

    @Override // org.sqlite.SQLiteConnection, java.sql.Connection
    public void rollback() throws SQLException {
    }

    @Override // org.sqlite.SQLiteConnection, java.sql.Connection, java.lang.AutoCloseable
    public void close() throws SQLException {
        ConnectionEvent event = new ConnectionEvent(this.parent);
        List<ConnectionEventListener> listeners = this.parent.getListeners();
        for (int i = listeners.size() - 1; i >= 0; i--) {
            listeners.get(i).connectionClosed(event);
        }
        if (!this.parent.getPhysicalConn().getAutoCommit()) {
            this.parent.getPhysicalConn().rollback();
        }
        this.parent.getPhysicalConn().setAutoCommit(true);
        this.isClosed.set(true);
    }

    @Override // org.sqlite.SQLiteConnection, java.sql.Connection
    public boolean isClosed() {
        return this.isClosed.get();
    }

    @Override // org.sqlite.SQLiteConnection, java.sql.Connection
    public DatabaseMetaData getMetaData() throws SQLException {
        return null;
    }

    @Override // java.sql.Connection
    public void setReadOnly(boolean readOnly) throws SQLException {
    }

    @Override // java.sql.Connection
    public boolean isReadOnly() throws SQLException {
        return false;
    }

    @Override // java.sql.Connection
    public void setCatalog(String catalog) throws SQLException {
    }

    @Override // java.sql.Connection
    public String getCatalog() throws SQLException {
        return null;
    }

    @Override // org.sqlite.SQLiteConnection, java.sql.Connection
    public void setTransactionIsolation(int level) throws SQLException {
    }

    @Override // org.sqlite.SQLiteConnection, java.sql.Connection
    public int getTransactionIsolation() {
        return 0;
    }

    @Override // java.sql.Connection
    public SQLWarning getWarnings() throws SQLException {
        return null;
    }

    @Override // java.sql.Connection
    public void clearWarnings() throws SQLException {
    }

    @Override // java.sql.Connection
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        return null;
    }

    @Override // java.sql.Connection
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return null;
    }

    @Override // java.sql.Connection
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return null;
    }

    @Override // java.sql.Connection
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        return null;
    }

    @Override // java.sql.Connection
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
    }

    @Override // java.sql.Connection
    public void setHoldability(int holdability) throws SQLException {
    }

    @Override // java.sql.Connection
    public int getHoldability() throws SQLException {
        return 0;
    }

    @Override // java.sql.Connection
    public Savepoint setSavepoint() throws SQLException {
        return null;
    }

    @Override // java.sql.Connection
    public Savepoint setSavepoint(String name) throws SQLException {
        return null;
    }

    @Override // java.sql.Connection
    public void rollback(Savepoint savepoint) throws SQLException {
    }

    @Override // java.sql.Connection
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
    }

    @Override // java.sql.Connection
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return null;
    }

    @Override // java.sql.Connection
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return null;
    }

    @Override // java.sql.Connection
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return null;
    }

    @Override // java.sql.Connection
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        return null;
    }

    @Override // java.sql.Connection
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        return null;
    }

    @Override // java.sql.Connection
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        return null;
    }

    @Override // java.sql.Connection
    public Clob createClob() throws SQLException {
        return null;
    }

    @Override // java.sql.Connection
    public Blob createBlob() throws SQLException {
        return null;
    }

    @Override // java.sql.Connection
    public NClob createNClob() throws SQLException {
        return null;
    }

    @Override // java.sql.Connection
    public SQLXML createSQLXML() throws SQLException {
        return null;
    }

    @Override // java.sql.Connection
    public boolean isValid(int timeout) throws SQLException {
        return false;
    }

    @Override // java.sql.Connection
    public void setClientInfo(String name, String value) throws SQLClientInfoException {
    }

    @Override // java.sql.Connection
    public void setClientInfo(Properties properties) throws SQLClientInfoException {
    }

    @Override // java.sql.Connection
    public String getClientInfo(String name) throws SQLException {
        return null;
    }

    @Override // java.sql.Connection
    public Properties getClientInfo() throws SQLException {
        return null;
    }

    @Override // java.sql.Connection
    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        return null;
    }

    @Override // java.sql.Connection
    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        return null;
    }

    @Override // org.sqlite.SQLiteConnection
    public void setSchema(String schema) throws SQLException {
    }

    @Override // org.sqlite.SQLiteConnection
    public String getSchema() throws SQLException {
        return null;
    }

    @Override // org.sqlite.SQLiteConnection
    public void abort(Executor executor) throws SQLException {
    }

    @Override // org.sqlite.SQLiteConnection
    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
    }

    @Override // org.sqlite.SQLiteConnection
    public int getNetworkTimeout() throws SQLException {
        return 0;
    }

    @Override // java.sql.Wrapper
    public <T> T unwrap(Class<T> cls) throws SQLException {
        return null;
    }

    @Override // java.sql.Wrapper
    public boolean isWrapperFor(Class<?> cls) throws SQLException {
        return false;
    }

    @Override // org.sqlite.SQLiteConnection
    public int getBusyTimeout() {
        return 0;
    }

    @Override // org.sqlite.SQLiteConnection
    public void setBusyTimeout(int timeoutMillis) {
    }

    @Override // org.sqlite.SQLiteConnection
    public DB getDatabase() {
        return null;
    }
}
