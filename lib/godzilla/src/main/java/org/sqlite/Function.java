package org.sqlite;

import java.sql.Connection;
import java.sql.SQLException;
import org.sqlite.core.DB;

public abstract class Function {
    public static final int FLAG_DETERMINISTIC = 2048;
    int args = 0;
    private SQLiteConnection conn;
    long context = 0;
    private DB db;
    long value = 0;

    public static abstract class Window extends Aggregate {
        /* access modifiers changed from: protected */
        public abstract void xInverse() throws SQLException;

        /* access modifiers changed from: protected */
        public abstract void xValue() throws SQLException;
    }

    /* access modifiers changed from: protected */
    public abstract void xFunc() throws SQLException;

    public static final void create(Connection conn2, String name, Function f) throws SQLException {
        create(conn2, name, f, 0);
    }

    public static final void create(Connection conn2, String name, Function f, int flags) throws SQLException {
        create(conn2, name, f, -1, flags);
    }

    public static final void create(Connection conn2, String name, Function f, int nArgs, int flags) throws SQLException {
        if (conn2 == null || !(conn2 instanceof SQLiteConnection)) {
            throw new SQLException("connection must be to an SQLite db");
        } else if (conn2.isClosed()) {
            throw new SQLException("connection closed");
        } else {
            f.conn = (SQLiteConnection) conn2;
            f.db = f.conn.getDatabase();
            if (nArgs < -1 || nArgs > 127) {
                throw new SQLException("invalid args provided: " + nArgs);
            } else if (name == null || name.length() > 255) {
                throw new SQLException("invalid function name: '" + name + "'");
            } else if (f.db.create_function(name, f, nArgs, flags) != 0) {
                throw new SQLException("error creating function");
            }
        }
    }

    public static final void destroy(Connection conn2, String name, int nArgs) throws SQLException {
        if (conn2 == null || !(conn2 instanceof SQLiteConnection)) {
            throw new SQLException("connection must be to an SQLite db");
        }
        ((SQLiteConnection) conn2).getDatabase().destroy_function(name, nArgs);
    }

    public static final void destroy(Connection conn2, String name) throws SQLException {
        destroy(conn2, name, -1);
    }

    /* access modifiers changed from: protected */
    public final synchronized int args() throws SQLException {
        checkContext();
        return this.args;
    }

    /* access modifiers changed from: protected */
    public final synchronized void result(byte[] value2) throws SQLException {
        checkContext();
        this.db.result_blob(this.context, value2);
    }

    /* access modifiers changed from: protected */
    public final synchronized void result(double value2) throws SQLException {
        checkContext();
        this.db.result_double(this.context, value2);
    }

    /* access modifiers changed from: protected */
    public final synchronized void result(int value2) throws SQLException {
        checkContext();
        this.db.result_int(this.context, value2);
    }

    /* access modifiers changed from: protected */
    public final synchronized void result(long value2) throws SQLException {
        checkContext();
        this.db.result_long(this.context, value2);
    }

    /* access modifiers changed from: protected */
    public final synchronized void result() throws SQLException {
        checkContext();
        this.db.result_null(this.context);
    }

    /* access modifiers changed from: protected */
    public final synchronized void result(String value2) throws SQLException {
        checkContext();
        this.db.result_text(this.context, value2);
    }

    /* access modifiers changed from: protected */
    public final synchronized void error(String err) throws SQLException {
        checkContext();
        this.db.result_error(this.context, err);
    }

    /* access modifiers changed from: protected */
    public final synchronized String value_text(int arg) throws SQLException {
        checkValue(arg);
        return this.db.value_text(this, arg);
    }

    /* access modifiers changed from: protected */
    public final synchronized byte[] value_blob(int arg) throws SQLException {
        checkValue(arg);
        return this.db.value_blob(this, arg);
    }

    /* access modifiers changed from: protected */
    public final synchronized double value_double(int arg) throws SQLException {
        checkValue(arg);
        return this.db.value_double(this, arg);
    }

    /* access modifiers changed from: protected */
    public final synchronized int value_int(int arg) throws SQLException {
        checkValue(arg);
        return this.db.value_int(this, arg);
    }

    /* access modifiers changed from: protected */
    public final synchronized long value_long(int arg) throws SQLException {
        checkValue(arg);
        return this.db.value_long(this, arg);
    }

    /* access modifiers changed from: protected */
    public final synchronized int value_type(int arg) throws SQLException {
        checkValue(arg);
        return this.db.value_type(this, arg);
    }

    private void checkContext() throws SQLException {
        if (this.conn == null || this.conn.getDatabase() == null || this.context == 0) {
            throw new SQLException("no context, not allowed to read value");
        }
    }

    private void checkValue(int arg) throws SQLException {
        if (this.conn == null || this.conn.getDatabase() == null || this.value == 0) {
            throw new SQLException("not in value access state");
        } else if (arg >= this.args) {
            throw new SQLException("arg " + arg + " out bounds [0," + this.args + ")");
        }
    }

    public static abstract class Aggregate extends Function implements Cloneable {
        /* access modifiers changed from: protected */
        public abstract void xFinal() throws SQLException;

        /* access modifiers changed from: protected */
        public abstract void xStep() throws SQLException;

        /* access modifiers changed from: protected */
        @Override // org.sqlite.Function
        public final void xFunc() {
        }

        @Override // java.lang.Object
        public Object clone() throws CloneNotSupportedException {
            return super.clone();
        }
    }
}
