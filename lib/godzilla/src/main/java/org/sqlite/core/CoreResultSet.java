package org.sqlite.core;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import org.sqlite.SQLiteConnectionConfig;

public abstract class CoreResultSet implements Codes {
    public boolean closeStmt;
    public String[] cols = null;
    public String[] colsMeta = null;
    protected Map<String, Integer> columnNameToIndex = null;
    protected int lastCol;
    protected int limitRows;
    public int maxRows;
    protected boolean[][] meta = null;
    public boolean open = false;
    protected int row = 0;
    protected final CoreStatement stmt;

    protected CoreResultSet(CoreStatement stmt2) {
        this.stmt = stmt2;
    }

    /* access modifiers changed from: protected */
    public DB getDatabase() {
        return this.stmt.getDatbase();
    }

    /* access modifiers changed from: protected */
    public SQLiteConnectionConfig getConnectionConfig() {
        return this.stmt.getConnectionConfig();
    }

    public boolean isOpen() {
        return this.open;
    }

    /* access modifiers changed from: protected */
    public void checkOpen() throws SQLException {
        if (!this.open) {
            throw new SQLException("ResultSet closed");
        }
    }

    public int checkCol(int col) throws SQLException {
        if (this.colsMeta == null) {
            throw new IllegalStateException("SQLite JDBC: inconsistent internal state");
        } else if (col >= 1 && col <= this.colsMeta.length) {
            return col - 1;
        } else {
            throw new SQLException("column " + col + " out of bounds [1," + this.colsMeta.length + "]");
        }
    }

    /* access modifiers changed from: protected */
    public int markCol(int col) throws SQLException {
        checkOpen();
        checkCol(col);
        this.lastCol = col;
        return col - 1;
    }

    public void checkMeta() throws SQLException {
        checkCol(1);
        if (this.meta == null) {
            this.meta = this.stmt.getDatbase().column_metadata(this.stmt.pointer);
        }
    }

    public void close() throws SQLException {
        this.cols = null;
        this.colsMeta = null;
        this.meta = null;
        this.limitRows = 0;
        this.row = 0;
        this.lastCol = -1;
        this.columnNameToIndex = null;
        if (this.open) {
            DB db = this.stmt.getDatbase();
            synchronized (db) {
                if (this.stmt.pointer != 0) {
                    db.reset(this.stmt.pointer);
                    if (this.closeStmt) {
                        this.closeStmt = false;
                        ((Statement) this.stmt).close();
                    }
                }
            }
            this.open = false;
        }
    }

    /* access modifiers changed from: protected */
    public Integer findColumnIndexInCache(String col) {
        if (this.columnNameToIndex == null) {
            return null;
        }
        return this.columnNameToIndex.get(col);
    }

    /* access modifiers changed from: protected */
    public int addColumnIndexInCache(String col, int index) {
        if (this.columnNameToIndex == null) {
            this.columnNameToIndex = new HashMap(this.cols.length);
        }
        this.columnNameToIndex.put(col, Integer.valueOf(index));
        return index;
    }
}
