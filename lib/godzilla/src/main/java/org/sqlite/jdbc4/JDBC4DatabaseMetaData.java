package org.sqlite.jdbc4;

import java.sql.ResultSet;
import java.sql.RowIdLifetime;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import org.sqlite.SQLiteConnection;
import org.sqlite.jdbc3.JDBC3DatabaseMetaData;

public class JDBC4DatabaseMetaData extends JDBC3DatabaseMetaData {
    public JDBC4DatabaseMetaData(SQLiteConnection conn) {
        super(conn);
    }

    @Override // java.sql.Wrapper
    public <T> T unwrap(Class<T> iface) throws ClassCastException {
        return iface.cast(this);
    }

    @Override // java.sql.Wrapper
    public boolean isWrapperFor(Class<?> iface) {
        return iface.isInstance(this);
    }

    @Override // java.sql.DatabaseMetaData
    public RowIdLifetime getRowIdLifetime() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override // java.sql.DatabaseMetaData
    public ResultSet getSchemas(String catalog, String schemaPattern) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsStoredFunctionsUsingCallSyntax() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override // java.sql.DatabaseMetaData
    public boolean autoCommitFailureClosesAllResultSets() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override // java.sql.DatabaseMetaData
    public ResultSet getClientInfoProperties() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override // java.sql.DatabaseMetaData
    public ResultSet getFunctions(String catalog, String schemaPattern, String functionNamePattern) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    public ResultSet getPseudoColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    public boolean generatedKeyAlwaysReturned() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }
}
