package org.sqlite.core;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.sqlite.SQLiteConnection;
import org.sqlite.SQLiteConnectionConfig;
import org.sqlite.jdbc4.JDBC4ResultSet;

public abstract class CoreStatement implements Codes {
    protected Object[] batch = null;
    protected int batchPos;
    public final SQLiteConnection conn;
    public long pointer;
    protected boolean resultsWaiting = false;
    protected final CoreResultSet rs;
    protected String sql = null;

    public abstract ResultSet executeQuery(String str, boolean z) throws SQLException;

    protected CoreStatement(SQLiteConnection c) {
        this.conn = c;
        this.rs = new JDBC4ResultSet(this);
    }

    public DB getDatbase() {
        return this.conn.getDatabase();
    }

    public SQLiteConnectionConfig getConnectionConfig() {
        return this.conn.getConnectionConfig();
    }

    /* access modifiers changed from: protected */
    public final void checkOpen() throws SQLException {
        if (this.pointer == 0) {
            throw new SQLException("statement is not executing");
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isOpen() throws SQLException {
        return this.pointer != 0;
    }

    /* access modifiers changed from: protected */
    public boolean exec() throws SQLException {
        if (this.sql == null) {
            throw new SQLException("SQLiteJDBC internal error: sql==null");
        } else if (this.rs.isOpen()) {
            throw new SQLException("SQLite JDBC internal error: rs.isOpen() on exec.");
        } else {
            boolean success = false;
            boolean rc = false;
            try {
                rc = this.conn.getDatabase().execute(this, (Object[]) null);
                success = true;
                return this.conn.getDatabase().column_count(this.pointer) != 0;
            } finally {
                this.resultsWaiting = rc;
                if (!success) {
                    this.conn.getDatabase().finalize(this);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public boolean exec(String sql2) throws SQLException {
        if (sql2 == null) {
            throw new SQLException("SQLiteJDBC internal error: sql==null");
        } else if (this.rs.isOpen()) {
            throw new SQLException("SQLite JDBC internal error: rs.isOpen() on exec.");
        } else {
            boolean rc = false;
            boolean success = false;
            try {
                rc = this.conn.getDatabase().execute(sql2, this.conn.getAutoCommit());
                success = true;
                return this.conn.getDatabase().column_count(this.pointer) != 0;
            } finally {
                this.resultsWaiting = rc;
                if (!success) {
                    this.conn.getDatabase().finalize(this);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void internalClose() throws SQLException {
        if (this.pointer != 0) {
            if (this.conn.isClosed()) {
                throw DB.newSQLException(1, "Connection is closed");
            }
            this.rs.close();
            this.batch = null;
            this.batchPos = 0;
            int resp = this.conn.getDatabase().finalize(this);
            if (resp != 0 && resp != 21) {
                this.conn.getDatabase().throwex(resp);
            }
        }
    }
}
