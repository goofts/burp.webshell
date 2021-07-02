package org.sqlite.jdbc4;

import java.sql.SQLException;
import java.sql.Statement;
import org.sqlite.SQLiteConnection;
import org.sqlite.jdbc3.JDBC3Statement;

public class JDBC4Statement extends JDBC3Statement implements Statement {
    boolean closeOnCompletion;
    private boolean closed = false;

    public JDBC4Statement(SQLiteConnection conn) {
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

    @Override // java.sql.Statement, org.sqlite.jdbc3.JDBC3Statement, java.lang.AutoCloseable
    public void close() throws SQLException {
        super.close();
        this.closed = true;
    }

    @Override // java.sql.Statement
    public boolean isClosed() {
        return this.closed;
    }

    public void closeOnCompletion() throws SQLException {
        if (this.closed) {
            throw new SQLException("statement is closed");
        }
        this.closeOnCompletion = true;
    }

    public boolean isCloseOnCompletion() throws SQLException {
        if (!this.closed) {
            return this.closeOnCompletion;
        }
        throw new SQLException("statement is closed");
    }

    @Override // java.sql.Statement
    public void setPoolable(boolean poolable) throws SQLException {
    }

    @Override // java.sql.Statement
    public boolean isPoolable() throws SQLException {
        return false;
    }
}
