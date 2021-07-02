package org.sqlite.jdbc4;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Map;
import org.sqlite.core.CoreStatement;
import org.sqlite.jdbc3.JDBC3ResultSet;

public class JDBC4ResultSet extends JDBC3ResultSet implements ResultSet, ResultSetMetaData {
    public JDBC4ResultSet(CoreStatement stmt) {
        super(stmt);
    }

    @Override // java.sql.ResultSet, org.sqlite.core.CoreResultSet, java.lang.AutoCloseable
    public void close() throws SQLException {
        boolean wasOpen = isOpen();
        super.close();
        if (wasOpen && (this.stmt instanceof JDBC4Statement)) {
            JDBC4Statement stat = (JDBC4Statement) this.stmt;
            if (stat.closeOnCompletion && !stat.isClosed()) {
                stat.close();
            }
        }
    }

    @Override // java.sql.Wrapper
    public <T> T unwrap(Class<T> iface) throws ClassCastException {
        return iface.cast(this);
    }

    @Override // java.sql.Wrapper
    public boolean isWrapperFor(Class<?> iface) {
        return iface.isInstance(this);
    }

    @Override // java.sql.ResultSet
    public RowId getRowId(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override // java.sql.ResultSet
    public RowId getRowId(String columnLabel) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override // java.sql.ResultSet
    public void updateRowId(int columnIndex, RowId x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override // java.sql.ResultSet
    public void updateRowId(String columnLabel, RowId x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override // java.sql.ResultSet
    public int getHoldability() throws SQLException {
        return 0;
    }

    @Override // java.sql.ResultSet
    public boolean isClosed() throws SQLException {
        return !isOpen();
    }

    @Override // java.sql.ResultSet
    public void updateNString(int columnIndex, String nString) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override // java.sql.ResultSet
    public void updateNString(String columnLabel, String nString) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override // java.sql.ResultSet
    public void updateNClob(int columnIndex, NClob nClob) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override // java.sql.ResultSet
    public void updateNClob(String columnLabel, NClob nClob) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override // java.sql.ResultSet
    public NClob getNClob(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override // java.sql.ResultSet
    public NClob getNClob(String columnLabel) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override // java.sql.ResultSet
    public SQLXML getSQLXML(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override // java.sql.ResultSet
    public SQLXML getSQLXML(String columnLabel) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override // java.sql.ResultSet
    public void updateSQLXML(int columnIndex, SQLXML xmlObject) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override // java.sql.ResultSet
    public void updateSQLXML(String columnLabel, SQLXML xmlObject) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override // java.sql.ResultSet
    public String getNString(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override // java.sql.ResultSet
    public String getNString(String columnLabel) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override // java.sql.ResultSet
    public Reader getNCharacterStream(int col) throws SQLException {
        return getNCharacterStreamInternal(getString(col));
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private Reader getNCharacterStreamInternal(String data) {
        if (data == null) {
            return null;
        }
        return new StringReader(data);
    }

    @Override // java.sql.ResultSet
    public Reader getNCharacterStream(String col) throws SQLException {
        return getNCharacterStreamInternal(getString(col));
    }

    @Override // java.sql.ResultSet
    public void updateNCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override // java.sql.ResultSet
    public void updateNCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override // java.sql.ResultSet
    public void updateAsciiStream(int columnIndex, InputStream x, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override // java.sql.ResultSet
    public void updateBinaryStream(int columnIndex, InputStream x, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override // java.sql.ResultSet
    public void updateCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override // java.sql.ResultSet
    public void updateAsciiStream(String columnLabel, InputStream x, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override // java.sql.ResultSet
    public void updateBinaryStream(String columnLabel, InputStream x, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override // java.sql.ResultSet
    public void updateCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override // java.sql.ResultSet
    public void updateBlob(int columnIndex, InputStream inputStream, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override // java.sql.ResultSet
    public void updateBlob(String columnLabel, InputStream inputStream, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override // java.sql.ResultSet
    public void updateClob(int columnIndex, Reader reader, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override // java.sql.ResultSet
    public void updateClob(String columnLabel, Reader reader, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override // java.sql.ResultSet
    public void updateNClob(int columnIndex, Reader reader, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override // java.sql.ResultSet
    public void updateNClob(String columnLabel, Reader reader, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override // java.sql.ResultSet
    public void updateNCharacterStream(int columnIndex, Reader x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override // java.sql.ResultSet
    public void updateNCharacterStream(String columnLabel, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override // java.sql.ResultSet
    public void updateAsciiStream(int columnIndex, InputStream x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override // java.sql.ResultSet
    public void updateBinaryStream(int columnIndex, InputStream x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override // java.sql.ResultSet
    public void updateCharacterStream(int columnIndex, Reader x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override // java.sql.ResultSet
    public void updateAsciiStream(String columnLabel, InputStream x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override // java.sql.ResultSet
    public void updateBinaryStream(String columnLabel, InputStream x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override // java.sql.ResultSet
    public void updateCharacterStream(String columnLabel, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override // java.sql.ResultSet
    public void updateBlob(int columnIndex, InputStream inputStream) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override // java.sql.ResultSet
    public void updateBlob(String columnLabel, InputStream inputStream) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override // java.sql.ResultSet
    public void updateClob(int columnIndex, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override // java.sql.ResultSet
    public void updateClob(String columnLabel, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override // java.sql.ResultSet
    public void updateNClob(int columnIndex, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override // java.sql.ResultSet
    public void updateNClob(String columnLabel, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    public <T> T getObject(int columnIndex, Class<T> cls) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    public <T> T getObject(String columnLabel, Class<T> cls) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /* access modifiers changed from: protected */
    public SQLException unused() {
        return new SQLFeatureNotSupportedException();
    }

    @Override // java.sql.ResultSet
    public Array getArray(int i) throws SQLException {
        throw unused();
    }

    @Override // java.sql.ResultSet
    public Array getArray(String col) throws SQLException {
        throw unused();
    }

    @Override // java.sql.ResultSet
    public InputStream getAsciiStream(int col) throws SQLException {
        return getAsciiStreamInternal(getString(col));
    }

    @Override // java.sql.ResultSet
    public InputStream getAsciiStream(String col) throws SQLException {
        return getAsciiStreamInternal(getString(col));
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private InputStream getAsciiStreamInternal(String data) {
        if (data == null) {
            return null;
        }
        try {
            return new ByteArrayInputStream(data.getBytes("ASCII"));
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    @Override // java.sql.ResultSet
    @Deprecated
    public BigDecimal getBigDecimal(int col, int s) throws SQLException {
        throw unused();
    }

    @Override // java.sql.ResultSet
    @Deprecated
    public BigDecimal getBigDecimal(String col, int s) throws SQLException {
        throw unused();
    }

    @Override // java.sql.ResultSet
    public Blob getBlob(int col) throws SQLException {
        throw unused();
    }

    @Override // java.sql.ResultSet
    public Blob getBlob(String col) throws SQLException {
        throw unused();
    }

    @Override // java.sql.ResultSet
    public Clob getClob(int col) throws SQLException {
        return new SqliteClob(getString(col));
    }

    @Override // java.sql.ResultSet
    public Clob getClob(String col) throws SQLException {
        return new SqliteClob(getString(col));
    }

    @Override // java.sql.ResultSet
    public Object getObject(int col, Map map) throws SQLException {
        throw unused();
    }

    @Override // java.sql.ResultSet
    public Object getObject(String col, Map map) throws SQLException {
        throw unused();
    }

    @Override // java.sql.ResultSet
    public Ref getRef(int i) throws SQLException {
        throw unused();
    }

    @Override // java.sql.ResultSet
    public Ref getRef(String col) throws SQLException {
        throw unused();
    }

    @Override // java.sql.ResultSet
    public InputStream getUnicodeStream(int col) throws SQLException {
        return getAsciiStream(col);
    }

    @Override // java.sql.ResultSet
    public InputStream getUnicodeStream(String col) throws SQLException {
        return getAsciiStream(col);
    }

    @Override // java.sql.ResultSet
    public URL getURL(int col) throws SQLException {
        throw unused();
    }

    @Override // java.sql.ResultSet
    public URL getURL(String col) throws SQLException {
        throw unused();
    }

    @Override // java.sql.ResultSet
    public void insertRow() throws SQLException {
        throw new SQLException("ResultSet is TYPE_FORWARD_ONLY");
    }

    @Override // java.sql.ResultSet
    public void moveToCurrentRow() throws SQLException {
        throw new SQLException("ResultSet is TYPE_FORWARD_ONLY");
    }

    @Override // java.sql.ResultSet
    public void moveToInsertRow() throws SQLException {
        throw new SQLException("ResultSet is TYPE_FORWARD_ONLY");
    }

    @Override // java.sql.ResultSet
    public boolean last() throws SQLException {
        throw new SQLException("ResultSet is TYPE_FORWARD_ONLY");
    }

    @Override // java.sql.ResultSet
    public boolean previous() throws SQLException {
        throw new SQLException("ResultSet is TYPE_FORWARD_ONLY");
    }

    @Override // java.sql.ResultSet
    public boolean relative(int rows) throws SQLException {
        throw new SQLException("ResultSet is TYPE_FORWARD_ONLY");
    }

    @Override // java.sql.ResultSet
    public boolean absolute(int row) throws SQLException {
        throw new SQLException("ResultSet is TYPE_FORWARD_ONLY");
    }

    @Override // java.sql.ResultSet
    public void afterLast() throws SQLException {
        throw new SQLException("ResultSet is TYPE_FORWARD_ONLY");
    }

    @Override // java.sql.ResultSet
    public void beforeFirst() throws SQLException {
        throw new SQLException("ResultSet is TYPE_FORWARD_ONLY");
    }

    @Override // java.sql.ResultSet
    public boolean first() throws SQLException {
        throw new SQLException("ResultSet is TYPE_FORWARD_ONLY");
    }

    @Override // java.sql.ResultSet
    public void cancelRowUpdates() throws SQLException {
        throw unused();
    }

    @Override // java.sql.ResultSet
    public void deleteRow() throws SQLException {
        throw unused();
    }

    @Override // java.sql.ResultSet
    public void updateArray(int col, Array x) throws SQLException {
        throw unused();
    }

    @Override // java.sql.ResultSet
    public void updateArray(String col, Array x) throws SQLException {
        throw unused();
    }

    @Override // java.sql.ResultSet
    public void updateAsciiStream(int col, InputStream x, int l) throws SQLException {
        throw unused();
    }

    @Override // java.sql.ResultSet
    public void updateAsciiStream(String col, InputStream x, int l) throws SQLException {
        throw unused();
    }

    @Override // java.sql.ResultSet
    public void updateBigDecimal(int col, BigDecimal x) throws SQLException {
        throw unused();
    }

    @Override // java.sql.ResultSet
    public void updateBigDecimal(String col, BigDecimal x) throws SQLException {
        throw unused();
    }

    @Override // java.sql.ResultSet
    public void updateBinaryStream(int c, InputStream x, int l) throws SQLException {
        throw unused();
    }

    @Override // java.sql.ResultSet
    public void updateBinaryStream(String c, InputStream x, int l) throws SQLException {
        throw unused();
    }

    @Override // java.sql.ResultSet
    public void updateBlob(int col, Blob x) throws SQLException {
        throw unused();
    }

    @Override // java.sql.ResultSet
    public void updateBlob(String col, Blob x) throws SQLException {
        throw unused();
    }

    @Override // java.sql.ResultSet
    public void updateBoolean(int col, boolean x) throws SQLException {
        throw unused();
    }

    @Override // java.sql.ResultSet
    public void updateBoolean(String col, boolean x) throws SQLException {
        throw unused();
    }

    @Override // java.sql.ResultSet
    public void updateByte(int col, byte x) throws SQLException {
        throw unused();
    }

    @Override // java.sql.ResultSet
    public void updateByte(String col, byte x) throws SQLException {
        throw unused();
    }

    @Override // java.sql.ResultSet
    public void updateBytes(int col, byte[] x) throws SQLException {
        throw unused();
    }

    @Override // java.sql.ResultSet
    public void updateBytes(String col, byte[] x) throws SQLException {
        throw unused();
    }

    @Override // java.sql.ResultSet
    public void updateCharacterStream(int c, Reader x, int l) throws SQLException {
        throw unused();
    }

    @Override // java.sql.ResultSet
    public void updateCharacterStream(String c, Reader r, int l) throws SQLException {
        throw unused();
    }

    @Override // java.sql.ResultSet
    public void updateClob(int col, Clob x) throws SQLException {
        throw unused();
    }

    @Override // java.sql.ResultSet
    public void updateClob(String col, Clob x) throws SQLException {
        throw unused();
    }

    @Override // java.sql.ResultSet
    public void updateDate(int col, Date x) throws SQLException {
        throw unused();
    }

    @Override // java.sql.ResultSet
    public void updateDate(String col, Date x) throws SQLException {
        throw unused();
    }

    @Override // java.sql.ResultSet
    public void updateDouble(int col, double x) throws SQLException {
        throw unused();
    }

    @Override // java.sql.ResultSet
    public void updateDouble(String col, double x) throws SQLException {
        throw unused();
    }

    @Override // java.sql.ResultSet
    public void updateFloat(int col, float x) throws SQLException {
        throw unused();
    }

    @Override // java.sql.ResultSet
    public void updateFloat(String col, float x) throws SQLException {
        throw unused();
    }

    @Override // java.sql.ResultSet
    public void updateInt(int col, int x) throws SQLException {
        throw unused();
    }

    @Override // java.sql.ResultSet
    public void updateInt(String col, int x) throws SQLException {
        throw unused();
    }

    @Override // java.sql.ResultSet
    public void updateLong(int col, long x) throws SQLException {
        throw unused();
    }

    @Override // java.sql.ResultSet
    public void updateLong(String col, long x) throws SQLException {
        throw unused();
    }

    @Override // java.sql.ResultSet
    public void updateNull(int col) throws SQLException {
        throw unused();
    }

    @Override // java.sql.ResultSet
    public void updateNull(String col) throws SQLException {
        throw unused();
    }

    @Override // java.sql.ResultSet
    public void updateObject(int c, Object x) throws SQLException {
        throw unused();
    }

    @Override // java.sql.ResultSet
    public void updateObject(int c, Object x, int s) throws SQLException {
        throw unused();
    }

    @Override // java.sql.ResultSet
    public void updateObject(String col, Object x) throws SQLException {
        throw unused();
    }

    @Override // java.sql.ResultSet
    public void updateObject(String c, Object x, int s) throws SQLException {
        throw unused();
    }

    @Override // java.sql.ResultSet
    public void updateRef(int col, Ref x) throws SQLException {
        throw unused();
    }

    @Override // java.sql.ResultSet
    public void updateRef(String c, Ref x) throws SQLException {
        throw unused();
    }

    @Override // java.sql.ResultSet
    public void updateRow() throws SQLException {
        throw unused();
    }

    @Override // java.sql.ResultSet
    public void updateShort(int c, short x) throws SQLException {
        throw unused();
    }

    @Override // java.sql.ResultSet
    public void updateShort(String c, short x) throws SQLException {
        throw unused();
    }

    @Override // java.sql.ResultSet
    public void updateString(int c, String x) throws SQLException {
        throw unused();
    }

    @Override // java.sql.ResultSet
    public void updateString(String c, String x) throws SQLException {
        throw unused();
    }

    @Override // java.sql.ResultSet
    public void updateTime(int c, Time x) throws SQLException {
        throw unused();
    }

    @Override // java.sql.ResultSet
    public void updateTime(String c, Time x) throws SQLException {
        throw unused();
    }

    @Override // java.sql.ResultSet
    public void updateTimestamp(int c, Timestamp x) throws SQLException {
        throw unused();
    }

    @Override // java.sql.ResultSet
    public void updateTimestamp(String c, Timestamp x) throws SQLException {
        throw unused();
    }

    @Override // java.sql.ResultSet
    public void refreshRow() throws SQLException {
        throw unused();
    }

    class SqliteClob implements NClob {
        private String data;

        protected SqliteClob(String data2) {
            this.data = data2;
        }

        @Override // java.sql.Clob
        public void free() throws SQLException {
            this.data = null;
        }

        @Override // java.sql.Clob
        public InputStream getAsciiStream() throws SQLException {
            return JDBC4ResultSet.this.getAsciiStreamInternal(this.data);
        }

        @Override // java.sql.Clob
        public Reader getCharacterStream() throws SQLException {
            return JDBC4ResultSet.this.getNCharacterStreamInternal(this.data);
        }

        @Override // java.sql.Clob
        public Reader getCharacterStream(long arg0, long arg1) throws SQLException {
            return JDBC4ResultSet.this.getNCharacterStreamInternal(this.data);
        }

        @Override // java.sql.Clob
        public String getSubString(long position, int length) throws SQLException {
            if (this.data == null) {
                throw new SQLException("no data");
            } else if (position < 1) {
                throw new SQLException("Position must be greater than or equal to 1");
            } else if (length < 0) {
                throw new SQLException("Length must be greater than or equal to 0");
            } else {
                int start = ((int) position) - 1;
                return this.data.substring(start, Math.min(start + length, this.data.length()));
            }
        }

        @Override // java.sql.Clob
        public long length() throws SQLException {
            if (this.data != null) {
                return (long) this.data.length();
            }
            throw new SQLException("no data");
        }

        @Override // java.sql.Clob
        public long position(String arg0, long arg1) throws SQLException {
            JDBC4ResultSet.this.unused();
            return -1;
        }

        @Override // java.sql.Clob
        public long position(Clob arg0, long arg1) throws SQLException {
            JDBC4ResultSet.this.unused();
            return -1;
        }

        @Override // java.sql.Clob
        public OutputStream setAsciiStream(long arg0) throws SQLException {
            JDBC4ResultSet.this.unused();
            return null;
        }

        @Override // java.sql.Clob
        public Writer setCharacterStream(long arg0) throws SQLException {
            JDBC4ResultSet.this.unused();
            return null;
        }

        @Override // java.sql.Clob
        public int setString(long arg0, String arg1) throws SQLException {
            JDBC4ResultSet.this.unused();
            return -1;
        }

        @Override // java.sql.Clob
        public int setString(long arg0, String arg1, int arg2, int arg3) throws SQLException {
            JDBC4ResultSet.this.unused();
            return -1;
        }

        @Override // java.sql.Clob
        public void truncate(long arg0) throws SQLException {
            JDBC4ResultSet.this.unused();
        }
    }
}
