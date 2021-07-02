package org.sqlite;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class ProgressHandler {
    /* access modifiers changed from: protected */
    public abstract int progress() throws SQLException;

    public static final void setHandler(Connection conn, int vmCalls, ProgressHandler progressHandler) throws SQLException {
        if (conn == null || !(conn instanceof SQLiteConnection)) {
            throw new SQLException("connection must be to an SQLite db");
        } else if (conn.isClosed()) {
            throw new SQLException("connection closed");
        } else {
            ((SQLiteConnection) conn).getDatabase().register_progress_handler(vmCalls, progressHandler);
        }
    }

    public static final void clearHandler(Connection conn) throws SQLException {
        ((SQLiteConnection) conn).getDatabase().clear_progress_handler();
    }
}
