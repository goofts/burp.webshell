package org.sqlite.core;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.sql.SQLException;
import org.sqlite.BusyHandler;
import org.sqlite.Function;
import org.sqlite.ProgressHandler;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteJDBCLoader;
import org.sqlite.core.DB;

public final class NativeDB extends DB {
    private static boolean isLoaded;
    private static boolean loadSucceeded;
    long pointer = 0;
    private final long udfdatalist = 0;

    /* access modifiers changed from: protected */
    @Override // org.sqlite.core.DB
    public native synchronized void _close() throws SQLException;

    /* access modifiers changed from: package-private */
    public native synchronized int _exec_utf8(byte[] bArr) throws SQLException;

    /* access modifiers changed from: package-private */
    public native synchronized void _open_utf8(byte[] bArr, int i) throws SQLException;

    /* access modifiers changed from: package-private */
    public native synchronized int backup(byte[] bArr, byte[] bArr2, DB.ProgressObserver progressObserver) throws SQLException;

    /* access modifiers changed from: package-private */
    @Override // org.sqlite.core.DB
    public native synchronized int bind_blob(long j, int i, byte[] bArr);

    /* access modifiers changed from: package-private */
    @Override // org.sqlite.core.DB
    public native synchronized int bind_double(long j, int i, double d);

    /* access modifiers changed from: package-private */
    @Override // org.sqlite.core.DB
    public native synchronized int bind_int(long j, int i, int i2);

    /* access modifiers changed from: package-private */
    @Override // org.sqlite.core.DB
    public native synchronized int bind_long(long j, int i, long j2);

    /* access modifiers changed from: package-private */
    @Override // org.sqlite.core.DB
    public native synchronized int bind_null(long j, int i);

    /* access modifiers changed from: package-private */
    @Override // org.sqlite.core.DB
    public native synchronized int bind_parameter_count(long j);

    /* access modifiers changed from: package-private */
    public native synchronized int bind_text_utf8(long j, int i, byte[] bArr);

    @Override // org.sqlite.core.DB
    public native synchronized void busy_handler(BusyHandler busyHandler);

    @Override // org.sqlite.core.DB
    public native synchronized void busy_timeout(int i);

    @Override // org.sqlite.core.DB
    public native synchronized int changes();

    @Override // org.sqlite.core.DB
    public native synchronized int clear_bindings(long j);

    @Override // org.sqlite.core.DB
    public native synchronized void clear_progress_handler() throws SQLException;

    @Override // org.sqlite.core.DB
    public native synchronized byte[] column_blob(long j, int i);

    @Override // org.sqlite.core.DB
    public native synchronized int column_count(long j);

    /* access modifiers changed from: package-private */
    public native synchronized ByteBuffer column_decltype_utf8(long j, int i);

    @Override // org.sqlite.core.DB
    public native synchronized double column_double(long j, int i);

    @Override // org.sqlite.core.DB
    public native synchronized int column_int(long j, int i);

    @Override // org.sqlite.core.DB
    public native synchronized long column_long(long j, int i);

    /* access modifiers changed from: package-private */
    @Override // org.sqlite.core.DB
    public native synchronized boolean[][] column_metadata(long j);

    /* access modifiers changed from: package-private */
    public native synchronized ByteBuffer column_name_utf8(long j, int i);

    /* access modifiers changed from: package-private */
    public native synchronized ByteBuffer column_table_name_utf8(long j, int i);

    /* access modifiers changed from: package-private */
    public native synchronized ByteBuffer column_text_utf8(long j, int i);

    @Override // org.sqlite.core.DB
    public native synchronized int column_type(long j, int i);

    /* access modifiers changed from: package-private */
    public native synchronized int create_function_utf8(byte[] bArr, Function function, int i, int i2);

    /* access modifiers changed from: package-private */
    public native synchronized int destroy_function_utf8(byte[] bArr, int i);

    @Override // org.sqlite.core.DB
    public native synchronized int enable_load_extension(boolean z);

    /* access modifiers changed from: package-private */
    public native synchronized ByteBuffer errmsg_utf8();

    /* access modifiers changed from: protected */
    @Override // org.sqlite.core.DB
    public native synchronized int finalize(long j);

    /* access modifiers changed from: package-private */
    @Override // org.sqlite.core.DB
    public native synchronized void free_functions();

    @Override // org.sqlite.core.DB
    public native void interrupt();

    /* access modifiers changed from: package-private */
    public native ByteBuffer libversion_utf8();

    @Override // org.sqlite.core.DB
    public native synchronized int limit(int i, int i2) throws SQLException;

    /* access modifiers changed from: package-private */
    public native synchronized long prepare_utf8(byte[] bArr) throws SQLException;

    @Override // org.sqlite.core.DB
    public native synchronized void register_progress_handler(int i, ProgressHandler progressHandler) throws SQLException;

    @Override // org.sqlite.core.DB
    public native synchronized int reset(long j);

    /* access modifiers changed from: package-private */
    public native synchronized int restore(byte[] bArr, byte[] bArr2, DB.ProgressObserver progressObserver) throws SQLException;

    @Override // org.sqlite.core.DB
    public native synchronized void result_blob(long j, byte[] bArr);

    @Override // org.sqlite.core.DB
    public native synchronized void result_double(long j, double d);

    /* access modifiers changed from: package-private */
    public native synchronized void result_error_utf8(long j, byte[] bArr);

    @Override // org.sqlite.core.DB
    public native synchronized void result_int(long j, int i);

    @Override // org.sqlite.core.DB
    public native synchronized void result_long(long j, long j2);

    @Override // org.sqlite.core.DB
    public native synchronized void result_null(long j);

    /* access modifiers changed from: package-private */
    public native synchronized void result_text_utf8(long j, byte[] bArr);

    /* access modifiers changed from: package-private */
    @Override // org.sqlite.core.DB
    public native synchronized void set_commit_listener(boolean z);

    /* access modifiers changed from: package-private */
    @Override // org.sqlite.core.DB
    public native synchronized void set_update_listener(boolean z);

    @Override // org.sqlite.core.DB
    public native synchronized int shared_cache(boolean z);

    @Override // org.sqlite.core.DB
    public native synchronized int step(long j);

    @Override // org.sqlite.core.DB
    public native synchronized int total_changes();

    @Override // org.sqlite.core.DB
    public native synchronized byte[] value_blob(Function function, int i);

    @Override // org.sqlite.core.DB
    public native synchronized double value_double(Function function, int i);

    @Override // org.sqlite.core.DB
    public native synchronized int value_int(Function function, int i);

    @Override // org.sqlite.core.DB
    public native synchronized long value_long(Function function, int i);

    /* access modifiers changed from: package-private */
    public native synchronized ByteBuffer value_text_utf8(Function function, int i);

    @Override // org.sqlite.core.DB
    public native synchronized int value_type(Function function, int i);

    static {
        if ("The Android Project".equals(System.getProperty("java.vm.vendor"))) {
            System.loadLibrary("sqlitejdbc");
            isLoaded = true;
            loadSucceeded = true;
            return;
        }
        isLoaded = false;
        loadSucceeded = false;
    }

    public NativeDB(String url, String fileName, SQLiteConfig config) throws SQLException {
        super(url, fileName, config);
    }

    public static boolean load() throws Exception {
        if (!isLoaded) {
            loadSucceeded = SQLiteJDBCLoader.initialize();
            isLoaded = true;
            return loadSucceeded;
        } else if (loadSucceeded) {
            return true;
        } else {
            return false;
        }
    }

    /* access modifiers changed from: protected */
    @Override // org.sqlite.core.DB
    public synchronized void _open(String file, int openFlags) throws SQLException {
        _open_utf8(stringToUtf8ByteArray(file), openFlags);
    }

    @Override // org.sqlite.core.DB
    public synchronized int _exec(String sql) throws SQLException {
        return _exec_utf8(stringToUtf8ByteArray(sql));
    }

    /* access modifiers changed from: protected */
    @Override // org.sqlite.core.DB
    public synchronized long prepare(String sql) throws SQLException {
        return prepare_utf8(stringToUtf8ByteArray(sql));
    }

    /* access modifiers changed from: package-private */
    @Override // org.sqlite.core.DB
    public synchronized String errmsg() {
        return utf8ByteBufferToString(errmsg_utf8());
    }

    @Override // org.sqlite.core.DB
    public synchronized String libversion() {
        return utf8ByteBufferToString(libversion_utf8());
    }

    @Override // org.sqlite.core.DB
    public synchronized String column_decltype(long stmt, int col) {
        return utf8ByteBufferToString(column_decltype_utf8(stmt, col));
    }

    @Override // org.sqlite.core.DB
    public synchronized String column_table_name(long stmt, int col) {
        return utf8ByteBufferToString(column_table_name_utf8(stmt, col));
    }

    @Override // org.sqlite.core.DB
    public synchronized String column_name(long stmt, int col) {
        return utf8ByteBufferToString(column_name_utf8(stmt, col));
    }

    @Override // org.sqlite.core.DB
    public synchronized String column_text(long stmt, int col) {
        return utf8ByteBufferToString(column_text_utf8(stmt, col));
    }

    /* access modifiers changed from: package-private */
    @Override // org.sqlite.core.DB
    public synchronized int bind_text(long stmt, int pos, String v) {
        return bind_text_utf8(stmt, pos, stringToUtf8ByteArray(v));
    }

    @Override // org.sqlite.core.DB
    public synchronized void result_text(long context, String val) {
        result_text_utf8(context, stringToUtf8ByteArray(val));
    }

    @Override // org.sqlite.core.DB
    public synchronized void result_error(long context, String err) {
        result_error_utf8(context, stringToUtf8ByteArray(err));
    }

    @Override // org.sqlite.core.DB
    public synchronized String value_text(Function f, int arg) {
        return utf8ByteBufferToString(value_text_utf8(f, arg));
    }

    @Override // org.sqlite.core.DB
    public synchronized int create_function(String name, Function func, int nArgs, int flags) {
        return create_function_utf8(stringToUtf8ByteArray(name), func, nArgs, flags);
    }

    @Override // org.sqlite.core.DB
    public synchronized int destroy_function(String name, int nArgs) {
        return destroy_function_utf8(stringToUtf8ByteArray(name), nArgs);
    }

    @Override // org.sqlite.core.DB
    public int backup(String dbName, String destFileName, DB.ProgressObserver observer) throws SQLException {
        return backup(stringToUtf8ByteArray(dbName), stringToUtf8ByteArray(destFileName), observer);
    }

    @Override // org.sqlite.core.DB
    public synchronized int restore(String dbName, String sourceFileName, DB.ProgressObserver observer) throws SQLException {
        return restore(stringToUtf8ByteArray(dbName), stringToUtf8ByteArray(sourceFileName), observer);
    }

    static void throwex(String msg) throws SQLException {
        throw new SQLException(msg);
    }

    static byte[] stringToUtf8ByteArray(String str) {
        if (str == null) {
            return null;
        }
        try {
            return str.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 is not supported", e);
        }
    }

    static String utf8ByteBufferToString(ByteBuffer buffer) {
        if (buffer == null) {
            return null;
        }
        try {
            return Charset.forName("UTF-8").decode(buffer).toString();
        } catch (UnsupportedCharsetException e) {
            throw new RuntimeException("UTF-8 is not supported", e);
        }
    }
}
