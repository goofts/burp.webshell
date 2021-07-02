package org.sqlite.jdbc4;

import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLXML;
import java.sql.Statement;
import java.util.Properties;
import org.sqlite.jdbc3.JDBC3Connection;

public class JDBC4Connection extends JDBC3Connection {
    public JDBC4Connection(String url, String fileName, Properties prop) throws SQLException {
        super(url, fileName, prop);
    }

    @Override // java.sql.Connection, org.sqlite.jdbc3.JDBC3Connection
    public Statement createStatement(int rst, int rsc, int rsh) throws SQLException {
        checkOpen();
        checkCursor(rst, rsc, rsh);
        return new JDBC4Statement(this);
    }

    @Override // java.sql.Connection, org.sqlite.jdbc3.JDBC3Connection
    public PreparedStatement prepareStatement(String sql, int rst, int rsc, int rsh) throws SQLException {
        checkOpen();
        checkCursor(rst, rsc, rsh);
        return new JDBC4PreparedStatement(this, sql);
    }

    @Override // org.sqlite.SQLiteConnection, java.sql.Connection
    public boolean isClosed() throws SQLException {
        return super.isClosed();
    }

    @Override // java.sql.Wrapper
    public <T> T unwrap(Class<T> iface) throws ClassCastException {
        return iface.cast(this);
    }

    @Override // java.sql.Wrapper
    public boolean isWrapperFor(Class<?> iface) {
        return iface.isInstance(this);
    }

    @Override // java.sql.Connection
    public Clob createClob() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override // java.sql.Connection
    public Blob createBlob() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override // java.sql.Connection
    public NClob createNClob() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override // java.sql.Connection
    public SQLXML createSQLXML() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override // java.sql.Connection
    public boolean isValid(int timeout) throws SQLException {
        if (isClosed()) {
            return false;
        }
        Statement statement = createStatement();
        try {
            return statement.execute("select 1");
        } finally {
            statement.close();
        }
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
}
