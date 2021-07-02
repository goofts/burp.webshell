package org.sqlite;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class BusyHandler {
    /* access modifiers changed from: protected */
    public abstract int callback(int i) throws SQLException;

    private static void commitHandler(Connection conn, BusyHandler busyHandler) throws SQLException {
        if (conn == null || !(conn instanceof SQLiteConnection)) {
            throw new SQLException("connection must be to an SQLite db");
        } else if (conn.isClosed()) {
            throw new SQLException("connection closed");
        } else {
            ((SQLiteConnection) conn).getDatabase().busy_handler(busyHandler);
        }
    }

    public static final void setHandler(Connection conn, BusyHandler busyHandler) throws SQLException {
        commitHandler(conn, busyHandler);
    }

    public static final void clearHandler(Connection conn) throws SQLException {
        commitHandler(conn, null);
    }
}
