package org.sqlite.core;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Calendar;
import org.sqlite.SQLiteConnection;
import org.sqlite.SQLiteConnectionConfig;
import org.sqlite.date.FastDateFormat;
import org.sqlite.jdbc4.JDBC4Statement;

public abstract class CorePreparedStatement extends JDBC4Statement {
    protected int batchQueryCount = 0;
    protected int columnCount;
    protected int paramCount;

    protected CorePreparedStatement(SQLiteConnection conn, String sql) throws SQLException {
        super(conn);
        this.sql = sql;
        DB db = conn.getDatabase();
        db.prepare(this);
        this.rs.colsMeta = db.column_names(this.pointer);
        this.columnCount = db.column_count(this.pointer);
        this.paramCount = db.bind_parameter_count(this.pointer);
        this.batch = null;
        this.batchPos = 0;
    }

    @Override // java.sql.Statement, org.sqlite.jdbc3.JDBC3Statement
    public int[] executeBatch() throws SQLException {
        if (this.batchQueryCount == 0) {
            return new int[0];
        }
        try {
            return this.conn.getDatabase().executeBatch(this.pointer, this.batchQueryCount, this.batch, this.conn.getAutoCommit());
        } finally {
            clearBatch();
        }
    }

    @Override // java.sql.Statement, org.sqlite.jdbc3.JDBC3Statement
    public void clearBatch() throws SQLException {
        super.clearBatch();
        this.batchQueryCount = 0;
    }

    @Override // java.sql.Statement, org.sqlite.jdbc3.JDBC3Statement
    public int getUpdateCount() throws SQLException {
        if (this.pointer == 0 || this.resultsWaiting || this.rs.isOpen()) {
            return -1;
        }
        return this.conn.getDatabase().changes();
    }

    /* access modifiers changed from: protected */
    public void batch(int pos, Object value) throws SQLException {
        checkOpen();
        if (this.batch == null) {
            this.batch = new Object[this.paramCount];
        }
        this.batch[(this.batchPos + pos) - 1] = value;
    }

    /* access modifiers changed from: protected */
    public void setDateByMilliseconds(int pos, Long value, Calendar calendar) throws SQLException {
        SQLiteConnectionConfig config = this.conn.getConnectionConfig();
        switch (config.getDateClass()) {
            case TEXT:
                batch(pos, FastDateFormat.getInstance(config.getDateStringFormat(), calendar.getTimeZone()).format(new Date(value.longValue())));
                return;
            case REAL:
                batch(pos, new Double((((double) value.longValue()) / 8.64E7d) + 2440587.5d));
                return;
            default:
                batch(pos, new Long(value.longValue() / config.getDateMultiplier()));
                return;
        }
    }
}
