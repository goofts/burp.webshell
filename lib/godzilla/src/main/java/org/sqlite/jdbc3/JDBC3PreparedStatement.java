package org.sqlite.jdbc3;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.ParameterMetaData;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import org.sqlite.SQLiteConnection;
import org.sqlite.core.CorePreparedStatement;

public abstract class JDBC3PreparedStatement extends CorePreparedStatement {
    protected JDBC3PreparedStatement(SQLiteConnection conn, String sql) throws SQLException {
        super(conn, sql);
    }

    public void clearParameters() throws SQLException {
        checkOpen();
        this.conn.getDatabase().clear_bindings(this.pointer);
        if (this.batch != null) {
            for (int i = this.batchPos; i < this.batchPos + this.paramCount; i++) {
                this.batch[i] = null;
            }
        }
    }

    public boolean execute() throws SQLException {
        checkOpen();
        this.rs.close();
        this.conn.getDatabase().reset(this.pointer);
        boolean success = false;
        try {
            this.resultsWaiting = this.conn.getDatabase().execute(this, this.batch);
            success = true;
            return this.columnCount != 0;
        } finally {
            if (!success && this.pointer != 0) {
                this.conn.getDatabase().reset(this.pointer);
            }
        }
    }

    public ResultSet executeQuery() throws SQLException {
        checkOpen();
        if (this.columnCount == 0) {
            throw new SQLException("Query does not return results");
        }
        this.rs.close();
        this.conn.getDatabase().reset(this.pointer);
        boolean success = false;
        try {
            this.resultsWaiting = this.conn.getDatabase().execute(this, this.batch);
            success = true;
            return getResultSet();
        } finally {
            if (!success && this.pointer != 0) {
                this.conn.getDatabase().reset(this.pointer);
            }
        }
    }

    public int executeUpdate() throws SQLException {
        checkOpen();
        if (this.columnCount != 0) {
            throw new SQLException("Query returns results");
        }
        this.rs.close();
        this.conn.getDatabase().reset(this.pointer);
        return this.conn.getDatabase().executeUpdate(this, this.batch);
    }

    public void addBatch() throws SQLException {
        checkOpen();
        this.batchPos += this.paramCount;
        this.batchQueryCount++;
        if (this.batch == null) {
            this.batch = new Object[this.paramCount];
        }
        if (this.batchPos + this.paramCount > this.batch.length) {
            Object[] nb = new Object[(this.batch.length * 2)];
            System.arraycopy(this.batch, 0, nb, 0, this.batch.length);
            this.batch = nb;
        }
        System.arraycopy(this.batch, this.batchPos - this.paramCount, this.batch, this.batchPos, this.paramCount);
    }

    public ParameterMetaData getParameterMetaData() {
        return (ParameterMetaData) this;
    }

    public int getParameterCount() throws SQLException {
        checkOpen();
        return this.paramCount;
    }

    public String getParameterClassName(int param) throws SQLException {
        checkOpen();
        return "java.lang.String";
    }

    public String getParameterTypeName(int pos) {
        return "VARCHAR";
    }

    public int getParameterType(int pos) {
        return 12;
    }

    public int getParameterMode(int pos) {
        return 1;
    }

    public int getPrecision(int pos) {
        return 0;
    }

    public int getScale(int pos) {
        return 0;
    }

    public int isNullable(int pos) {
        return 1;
    }

    public boolean isSigned(int pos) {
        return true;
    }

    public Statement getStatement() {
        return this;
    }

    public void setBigDecimal(int pos, BigDecimal value) throws SQLException {
        batch(pos, value == null ? null : value.toString());
    }

    private byte[] readBytes(InputStream istream, int length) throws SQLException {
        if (length < 0) {
            throw new SQLException("Error reading stream. Length should be non-negative");
        }
        byte[] bytes = new byte[length];
        int totalBytesRead = 0;
        while (totalBytesRead < length) {
            try {
                int bytesRead = istream.read(bytes, totalBytesRead, length - totalBytesRead);
                if (bytesRead == -1) {
                    throw new IOException("End of stream has been reached");
                }
                totalBytesRead += bytesRead;
            } catch (IOException cause) {
                SQLException exception = new SQLException("Error reading stream");
                exception.initCause(cause);
                throw exception;
            }
        }
        return bytes;
    }

    public void setBinaryStream(int pos, InputStream istream, int length) throws SQLException {
        if (istream == null && length == 0) {
            setBytes(pos, null);
        }
        setBytes(pos, readBytes(istream, length));
    }

    public void setAsciiStream(int pos, InputStream istream, int length) throws SQLException {
        setUnicodeStream(pos, istream, length);
    }

    public void setUnicodeStream(int pos, InputStream istream, int length) throws SQLException {
        if (istream == null && length == 0) {
            setString(pos, null);
        }
        try {
            setString(pos, new String(readBytes(istream, length), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            SQLException exception = new SQLException("UTF-8 is not supported");
            exception.initCause(e);
            throw exception;
        }
    }

    public void setBoolean(int pos, boolean value) throws SQLException {
        setInt(pos, value ? 1 : 0);
    }

    public void setByte(int pos, byte value) throws SQLException {
        setInt(pos, value);
    }

    public void setBytes(int pos, byte[] value) throws SQLException {
        batch(pos, value);
    }

    public void setDouble(int pos, double value) throws SQLException {
        batch(pos, new Double(value));
    }

    public void setFloat(int pos, float value) throws SQLException {
        batch(pos, new Float(value));
    }

    public void setInt(int pos, int value) throws SQLException {
        batch(pos, new Integer(value));
    }

    public void setLong(int pos, long value) throws SQLException {
        batch(pos, new Long(value));
    }

    public void setNull(int pos, int u1) throws SQLException {
        setNull(pos, u1, null);
    }

    public void setNull(int pos, int u1, String u2) throws SQLException {
        batch(pos, null);
    }

    public void setObject(int pos, Object value) throws SQLException {
        if (value == null) {
            batch(pos, null);
        } else if (value instanceof Date) {
            setDateByMilliseconds(pos, Long.valueOf(((Date) value).getTime()), Calendar.getInstance());
        } else if (value instanceof Long) {
            batch(pos, value);
        } else if (value instanceof Integer) {
            batch(pos, value);
        } else if (value instanceof Short) {
            batch(pos, new Integer(((Short) value).intValue()));
        } else if (value instanceof Float) {
            batch(pos, value);
        } else if (value instanceof Double) {
            batch(pos, value);
        } else if (value instanceof Boolean) {
            setBoolean(pos, ((Boolean) value).booleanValue());
        } else if (value instanceof byte[]) {
            batch(pos, value);
        } else if (value instanceof BigDecimal) {
            setBigDecimal(pos, (BigDecimal) value);
        } else {
            batch(pos, value.toString());
        }
    }

    public void setObject(int p, Object v, int t) throws SQLException {
        setObject(p, v);
    }

    public void setObject(int p, Object v, int t, int s) throws SQLException {
        setObject(p, v);
    }

    public void setShort(int pos, short value) throws SQLException {
        setInt(pos, value);
    }

    public void setString(int pos, String value) throws SQLException {
        batch(pos, value);
    }

    public void setCharacterStream(int pos, Reader reader, int length) throws SQLException {
        try {
            StringBuffer sb = new StringBuffer();
            char[] cbuf = new char[8192];
            while (true) {
                int cnt = reader.read(cbuf);
                if (cnt > 0) {
                    sb.append(cbuf, 0, cnt);
                } else {
                    setString(pos, sb.toString());
                    return;
                }
            }
        } catch (IOException e) {
            throw new SQLException("Cannot read from character stream, exception message: " + e.getMessage());
        }
    }

    public void setDate(int pos, java.sql.Date x) throws SQLException {
        setDate(pos, x, Calendar.getInstance());
    }

    public void setDate(int pos, java.sql.Date x, Calendar cal) throws SQLException {
        if (x == null) {
            setObject(pos, null);
        } else {
            setDateByMilliseconds(pos, Long.valueOf(x.getTime()), cal);
        }
    }

    public void setTime(int pos, Time x) throws SQLException {
        setTime(pos, x, Calendar.getInstance());
    }

    public void setTime(int pos, Time x, Calendar cal) throws SQLException {
        if (x == null) {
            setObject(pos, null);
        } else {
            setDateByMilliseconds(pos, Long.valueOf(x.getTime()), cal);
        }
    }

    public void setTimestamp(int pos, Timestamp x) throws SQLException {
        setTimestamp(pos, x, Calendar.getInstance());
    }

    public void setTimestamp(int pos, Timestamp x, Calendar cal) throws SQLException {
        if (x == null) {
            setObject(pos, null);
        } else {
            setDateByMilliseconds(pos, Long.valueOf(x.getTime()), cal);
        }
    }

    public ResultSetMetaData getMetaData() throws SQLException {
        checkOpen();
        return (ResultSetMetaData) this.rs;
    }

    /* access modifiers changed from: protected */
    @Override // org.sqlite.jdbc3.JDBC3Statement
    public SQLException unused() {
        return new SQLException("not implemented by SQLite JDBC driver");
    }

    public void setArray(int i, Array x) throws SQLException {
        throw unused();
    }

    public void setBlob(int i, Blob x) throws SQLException {
        throw unused();
    }

    public void setClob(int i, Clob x) throws SQLException {
        throw unused();
    }

    public void setRef(int i, Ref x) throws SQLException {
        throw unused();
    }

    public void setURL(int pos, URL x) throws SQLException {
        throw unused();
    }

    @Override // java.sql.Statement, org.sqlite.jdbc3.JDBC3Statement
    public boolean execute(String sql) throws SQLException {
        throw unused();
    }

    @Override // java.sql.Statement, org.sqlite.jdbc3.JDBC3Statement
    public int executeUpdate(String sql) throws SQLException {
        throw unused();
    }

    @Override // java.sql.Statement, org.sqlite.jdbc3.JDBC3Statement
    public ResultSet executeQuery(String sql) throws SQLException {
        throw unused();
    }

    @Override // java.sql.Statement, org.sqlite.jdbc3.JDBC3Statement
    public void addBatch(String sql) throws SQLException {
        throw unused();
    }
}
