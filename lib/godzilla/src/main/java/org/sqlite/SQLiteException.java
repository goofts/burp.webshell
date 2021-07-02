package org.sqlite;

import java.sql.SQLException;

public class SQLiteException extends SQLException {
    private SQLiteErrorCode resultCode;

    public SQLiteException(String message, SQLiteErrorCode resultCode2) {
        super(message, (String) null, resultCode2.code & 255);
        this.resultCode = resultCode2;
    }

    public SQLiteErrorCode getResultCode() {
        return this.resultCode;
    }
}
