package org.sqlite.core;

import com.formdev.flatlaf.demo.DemoPrefs;
import java.sql.BatchUpdateException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import org.sqlite.BusyHandler;
import org.sqlite.Function;
import org.sqlite.ProgressHandler;
import org.sqlite.SQLiteCommitListener;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteErrorCode;
import org.sqlite.SQLiteException;
import org.sqlite.SQLiteUpdateListener;

public abstract class DB implements Codes {
    long begin = 0;
    private final AtomicBoolean closed = new AtomicBoolean(true);
    long commit = 0;
    private final Set<SQLiteCommitListener> commitListeners = new HashSet();
    private final SQLiteConfig config;
    private final String fileName;
    private final Map<Long, CoreStatement> stmts = new HashMap();
    private final Set<SQLiteUpdateListener> updateListeners = new HashSet();
    private final String url;

    public interface ProgressObserver {
        void progress(int i, int i2);
    }

    /* access modifiers changed from: protected */
    public abstract void _close() throws SQLException;

    public abstract int _exec(String str) throws SQLException;

    /* access modifiers changed from: protected */
    public abstract void _open(String str, int i) throws SQLException;

    public abstract int backup(String str, String str2, ProgressObserver progressObserver) throws SQLException;

    /* access modifiers changed from: package-private */
    public abstract int bind_blob(long j, int i, byte[] bArr) throws SQLException;

    /* access modifiers changed from: package-private */
    public abstract int bind_double(long j, int i, double d) throws SQLException;

    /* access modifiers changed from: package-private */
    public abstract int bind_int(long j, int i, int i2) throws SQLException;

    /* access modifiers changed from: package-private */
    public abstract int bind_long(long j, int i, long j2) throws SQLException;

    /* access modifiers changed from: package-private */
    public abstract int bind_null(long j, int i) throws SQLException;

    /* access modifiers changed from: package-private */
    public abstract int bind_parameter_count(long j) throws SQLException;

    /* access modifiers changed from: package-private */
    public abstract int bind_text(long j, int i, String str) throws SQLException;

    public abstract void busy_handler(BusyHandler busyHandler) throws SQLException;

    public abstract void busy_timeout(int i) throws SQLException;

    public abstract int changes() throws SQLException;

    public abstract int clear_bindings(long j) throws SQLException;

    public abstract void clear_progress_handler() throws SQLException;

    public abstract byte[] column_blob(long j, int i) throws SQLException;

    public abstract int column_count(long j) throws SQLException;

    public abstract String column_decltype(long j, int i) throws SQLException;

    public abstract double column_double(long j, int i) throws SQLException;

    public abstract int column_int(long j, int i) throws SQLException;

    public abstract long column_long(long j, int i) throws SQLException;

    /* access modifiers changed from: package-private */
    public abstract boolean[][] column_metadata(long j) throws SQLException;

    public abstract String column_name(long j, int i) throws SQLException;

    public abstract String column_table_name(long j, int i) throws SQLException;

    public abstract String column_text(long j, int i) throws SQLException;

    public abstract int column_type(long j, int i) throws SQLException;

    public abstract int create_function(String str, Function function, int i, int i2) throws SQLException;

    public abstract int destroy_function(String str, int i) throws SQLException;

    public abstract int enable_load_extension(boolean z) throws SQLException;

    /* access modifiers changed from: package-private */
    public abstract String errmsg() throws SQLException;

    /* access modifiers changed from: protected */
    public abstract int finalize(long j) throws SQLException;

    /* access modifiers changed from: package-private */
    public abstract void free_functions() throws SQLException;

    public abstract void interrupt() throws SQLException;

    public abstract String libversion() throws SQLException;

    public abstract int limit(int i, int i2) throws SQLException;

    /* access modifiers changed from: protected */
    public abstract long prepare(String str) throws SQLException;

    public abstract void register_progress_handler(int i, ProgressHandler progressHandler) throws SQLException;

    public abstract int reset(long j) throws SQLException;

    public abstract int restore(String str, String str2, ProgressObserver progressObserver) throws SQLException;

    public abstract void result_blob(long j, byte[] bArr) throws SQLException;

    public abstract void result_double(long j, double d) throws SQLException;

    public abstract void result_error(long j, String str) throws SQLException;

    public abstract void result_int(long j, int i) throws SQLException;

    public abstract void result_long(long j, long j2) throws SQLException;

    public abstract void result_null(long j) throws SQLException;

    public abstract void result_text(long j, String str) throws SQLException;

    /* access modifiers changed from: package-private */
    public abstract void set_commit_listener(boolean z);

    /* access modifiers changed from: package-private */
    public abstract void set_update_listener(boolean z);

    public abstract int shared_cache(boolean z) throws SQLException;

    public abstract int step(long j) throws SQLException;

    public abstract int total_changes() throws SQLException;

    public abstract byte[] value_blob(Function function, int i) throws SQLException;

    public abstract double value_double(Function function, int i) throws SQLException;

    public abstract int value_int(Function function, int i) throws SQLException;

    public abstract long value_long(Function function, int i) throws SQLException;

    public abstract String value_text(Function function, int i) throws SQLException;

    public abstract int value_type(Function function, int i) throws SQLException;

    public DB(String url2, String fileName2, SQLiteConfig config2) throws SQLException {
        this.url = url2;
        this.fileName = fileName2;
        this.config = config2;
    }

    public String getUrl() {
        return this.url;
    }

    public boolean isClosed() {
        return this.closed.get();
    }

    public SQLiteConfig getConfig() {
        return this.config;
    }

    public final synchronized void exec(String sql, boolean autoCommit) throws SQLException {
        long pointer = 0;
        try {
            pointer = prepare(sql);
            int rc = step(pointer);
            switch (rc) {
                case 100:
                    finalize(pointer);
                    break;
                case 101:
                    ensureAutoCommit(autoCommit);
                    finalize(pointer);
                    break;
                default:
                    throwex(rc);
                    break;
            }
        } finally {
            finalize(pointer);
        }
    }

    public final synchronized void open(String file, int openFlags) throws SQLException {
        _open(file, openFlags);
        this.closed.set(false);
        if (this.fileName.startsWith(DemoPrefs.FILE_PREFIX) && !this.fileName.contains("cache=")) {
            shared_cache(this.config.isEnabledSharedCache());
        }
        enable_load_extension(this.config.isEnabledLoadExtension());
        busy_timeout(this.config.getBusyTimeout());
    }

    public final synchronized void close() throws SQLException {
        synchronized (this.stmts) {
            Iterator<Map.Entry<Long, CoreStatement>> i = this.stmts.entrySet().iterator();
            while (i.hasNext()) {
                Map.Entry<Long, CoreStatement> entry = i.next();
                CoreStatement stmt = entry.getValue();
                finalize(entry.getKey().longValue());
                if (stmt != null) {
                    stmt.pointer = 0;
                }
                i.remove();
            }
        }
        free_functions();
        if (this.begin != 0) {
            finalize(this.begin);
            this.begin = 0;
        }
        if (this.commit != 0) {
            finalize(this.commit);
            this.commit = 0;
        }
        this.closed.set(true);
        _close();
    }

    public final synchronized void prepare(CoreStatement stmt) throws SQLException {
        if (stmt.sql == null) {
            throw new NullPointerException();
        }
        if (stmt.pointer != 0) {
            finalize(stmt);
        }
        stmt.pointer = prepare(stmt.sql);
        this.stmts.put(new Long(stmt.pointer), stmt);
    }

    public final synchronized int finalize(CoreStatement stmt) throws SQLException {
        int rc;
        if (stmt.pointer == 0) {
            rc = 0;
        } else {
            try {
                rc = finalize(stmt.pointer);
            } finally {
                this.stmts.remove(new Long(stmt.pointer));
                stmt.pointer = 0;
            }
        }
        return rc;
    }

    public final synchronized String[] column_names(long stmt) throws SQLException {
        String[] names;
        names = new String[column_count(stmt)];
        for (int i = 0; i < names.length; i++) {
            names[i] = column_name(stmt, i);
        }
        return names;
    }

    /* access modifiers changed from: package-private */
    public final synchronized int sqlbind(long stmt, int pos, Object v) throws SQLException {
        int bind_blob;
        int pos2 = pos + 1;
        if (v == null) {
            bind_blob = bind_null(stmt, pos2);
        } else if (v instanceof Integer) {
            bind_blob = bind_int(stmt, pos2, ((Integer) v).intValue());
        } else if (v instanceof Short) {
            bind_blob = bind_int(stmt, pos2, ((Short) v).intValue());
        } else if (v instanceof Long) {
            bind_blob = bind_long(stmt, pos2, ((Long) v).longValue());
        } else if (v instanceof Float) {
            bind_blob = bind_double(stmt, pos2, ((Float) v).doubleValue());
        } else if (v instanceof Double) {
            bind_blob = bind_double(stmt, pos2, ((Double) v).doubleValue());
        } else if (v instanceof String) {
            bind_blob = bind_text(stmt, pos2, (String) v);
        } else if (v instanceof byte[]) {
            bind_blob = bind_blob(stmt, pos2, (byte[]) v);
        } else {
            throw new SQLException("unexpected param type: " + v.getClass());
        }
        return bind_blob;
    }

    /* access modifiers changed from: package-private */
    public final synchronized int[] executeBatch(long stmt, int count, Object[] vals, boolean autoCommit) throws SQLException {
        int[] changes;
        if (count < 1) {
            throw new SQLException("count (" + count + ") < 1");
        }
        int params = bind_parameter_count(stmt);
        changes = new int[count];
        for (int i = 0; i < count; i++) {
            try {
                reset(stmt);
                for (int j = 0; j < params; j++) {
                    int rc = sqlbind(stmt, j, vals[(i * params) + j]);
                    if (rc != 0) {
                        throwex(rc);
                    }
                }
                int rc2 = step(stmt);
                if (rc2 != 101) {
                    reset(stmt);
                    if (rc2 == 100) {
                        throw new BatchUpdateException("batch entry " + i + ": query returns results", changes);
                    }
                    throwex(rc2);
                }
                changes[i] = changes();
            } catch (Throwable th) {
                ensureAutoCommit(autoCommit);
                throw th;
            }
        }
        ensureAutoCommit(autoCommit);
        reset(stmt);
        return changes;
    }

    public final synchronized boolean execute(CoreStatement stmt, Object[] vals) throws SQLException {
        boolean z;
        if (vals != null) {
            int params = bind_parameter_count(stmt.pointer);
            if (params > vals.length) {
                throw new SQLException("assertion failure: param count (" + params + ") > value count (" + vals.length + ")");
            }
            for (int i = 0; i < params; i++) {
                int rc = sqlbind(stmt.pointer, i, vals[i]);
                if (rc != 0) {
                    throwex(rc);
                }
            }
        }
        int statusCode = step(stmt.pointer);
        switch (statusCode & 255) {
            case 5:
            case 6:
            case 19:
            case 21:
                throw newSQLException(statusCode);
            case 100:
                z = true;
                break;
            case 101:
                reset(stmt.pointer);
                ensureAutoCommit(stmt.conn.getAutoCommit());
                z = false;
                break;
            default:
                finalize(stmt);
                throw newSQLException(statusCode);
        }
        return z;
    }

    /* access modifiers changed from: package-private */
    public final synchronized boolean execute(String sql, boolean autoCommit) throws SQLException {
        boolean z = false;
        synchronized (this) {
            int statusCode = _exec(sql);
            switch (statusCode) {
                case 0:
                    break;
                case 100:
                    z = true;
                    break;
                case 101:
                    ensureAutoCommit(autoCommit);
                    break;
                default:
                    throw newSQLException(statusCode);
            }
        }
        return z;
    }

    public final synchronized int executeUpdate(CoreStatement stmt, Object[] vals) throws SQLException {
        try {
            if (execute(stmt, vals)) {
                throw new SQLException("query returns results");
            }
            if (stmt.pointer != 0) {
                reset(stmt.pointer);
            }
        } catch (Throwable th) {
            if (stmt.pointer != 0) {
                reset(stmt.pointer);
            }
            throw th;
        }
        return changes();
    }

    public synchronized void addUpdateListener(SQLiteUpdateListener listener) {
        if (this.updateListeners.add(listener) && this.updateListeners.size() == 1) {
            set_update_listener(true);
        }
    }

    public synchronized void addCommitListener(SQLiteCommitListener listener) {
        if (this.commitListeners.add(listener) && this.commitListeners.size() == 1) {
            set_commit_listener(true);
        }
    }

    public synchronized void removeUpdateListener(SQLiteUpdateListener listener) {
        if (this.updateListeners.remove(listener) && this.updateListeners.isEmpty()) {
            set_update_listener(false);
        }
    }

    public synchronized void removeCommitListener(SQLiteCommitListener listener) {
        if (this.commitListeners.remove(listener) && this.commitListeners.isEmpty()) {
            set_commit_listener(false);
        }
    }

    /* access modifiers changed from: package-private */
    public void onUpdate(int type, String database, String table, long rowId) {
        Set<SQLiteUpdateListener> listeners;
        SQLiteUpdateListener.Type operationType;
        synchronized (this) {
            listeners = new HashSet<>(this.updateListeners);
        }
        for (SQLiteUpdateListener listener : listeners) {
            switch (type) {
                case 9:
                    operationType = SQLiteUpdateListener.Type.DELETE;
                    break;
                case 18:
                    operationType = SQLiteUpdateListener.Type.INSERT;
                    break;
                case 23:
                    operationType = SQLiteUpdateListener.Type.UPDATE;
                    break;
                default:
                    throw new AssertionError("Unknown type: " + type);
            }
            listener.onUpdate(operationType, database, table, rowId);
        }
    }

    /* access modifiers changed from: package-private */
    public void onCommit(boolean commit2) {
        Set<SQLiteCommitListener> listeners;
        synchronized (this) {
            listeners = new HashSet<>(this.commitListeners);
        }
        for (SQLiteCommitListener listener : listeners) {
            if (commit2) {
                listener.onCommit();
            } else {
                listener.onRollback();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public final void throwex() throws SQLException {
        throw new SQLException(errmsg());
    }

    public final void throwex(int errorCode) throws SQLException {
        throw newSQLException(errorCode);
    }

    static final void throwex(int errorCode, String errorMessage) throws SQLiteException {
        throw newSQLException(errorCode, errorMessage);
    }

    public static SQLiteException newSQLException(int errorCode, String errorMessage) {
        SQLiteErrorCode code = SQLiteErrorCode.getErrorCode(errorCode);
        return new SQLiteException(String.format("%s (%s)", code, errorMessage), code);
    }

    private SQLiteException newSQLException(int errorCode) throws SQLException {
        return newSQLException(errorCode, errmsg());
    }

    /* access modifiers changed from: package-private */
    public final void ensureAutoCommit(boolean autoCommit) throws SQLException {
        if (autoCommit) {
            if (this.begin == 0) {
                this.begin = prepare("begin;");
            }
            if (this.commit == 0) {
                this.commit = prepare("commit;");
            }
            try {
                if (step(this.begin) == 101) {
                    int rc = step(this.commit);
                    if (rc != 101) {
                        reset(this.commit);
                        throwex(rc);
                    }
                    reset(this.begin);
                    reset(this.commit);
                }
            } finally {
                reset(this.begin);
                reset(this.commit);
            }
        }
    }
}
