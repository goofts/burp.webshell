package org.sqlite;

import java.util.EnumMap;
import java.util.Map;
import java.util.Properties;
import org.sqlite.SQLiteConfig;
import org.sqlite.date.FastDateFormat;

public class SQLiteConnectionConfig implements Cloneable {
    private static final Map<SQLiteConfig.TransactionMode, String> beginCommandMap = new EnumMap(SQLiteConfig.TransactionMode.class);
    private boolean autoCommit = true;
    private SQLiteConfig.DateClass dateClass = SQLiteConfig.DateClass.INTEGER;
    private FastDateFormat dateFormat = FastDateFormat.getInstance(this.dateStringFormat);
    private SQLiteConfig.DatePrecision datePrecision = SQLiteConfig.DatePrecision.MILLISECONDS;
    private String dateStringFormat = SQLiteConfig.DEFAULT_DATE_STRING_FORMAT;
    private int transactionIsolation = 8;
    private SQLiteConfig.TransactionMode transactionMode = SQLiteConfig.TransactionMode.DEFERRED;

    public static SQLiteConnectionConfig fromPragmaTable(Properties pragmaTable) {
        return new SQLiteConnectionConfig(SQLiteConfig.DateClass.getDateClass(pragmaTable.getProperty(SQLiteConfig.Pragma.DATE_CLASS.pragmaName, SQLiteConfig.DateClass.INTEGER.name())), SQLiteConfig.DatePrecision.getPrecision(pragmaTable.getProperty(SQLiteConfig.Pragma.DATE_PRECISION.pragmaName, SQLiteConfig.DatePrecision.MILLISECONDS.name())), pragmaTable.getProperty(SQLiteConfig.Pragma.DATE_STRING_FORMAT.pragmaName, SQLiteConfig.DEFAULT_DATE_STRING_FORMAT), 8, SQLiteConfig.TransactionMode.getMode(pragmaTable.getProperty(SQLiteConfig.Pragma.TRANSACTION_MODE.pragmaName, SQLiteConfig.TransactionMode.DEFERRED.name())), true);
    }

    public SQLiteConnectionConfig(SQLiteConfig.DateClass dateClass2, SQLiteConfig.DatePrecision datePrecision2, String dateStringFormat2, int transactionIsolation2, SQLiteConfig.TransactionMode transactionMode2, boolean autoCommit2) {
        setDateClass(dateClass2);
        setDatePrecision(datePrecision2);
        setDateStringFormat(dateStringFormat2);
        setTransactionIsolation(transactionIsolation2);
        setTransactionMode(transactionMode2);
        setAutoCommit(autoCommit2);
    }

    public SQLiteConnectionConfig copyConfig() {
        return new SQLiteConnectionConfig(this.dateClass, this.datePrecision, this.dateStringFormat, this.transactionIsolation, this.transactionMode, this.autoCommit);
    }

    public long getDateMultiplier() {
        return this.datePrecision == SQLiteConfig.DatePrecision.MILLISECONDS ? 1 : 1000;
    }

    public SQLiteConfig.DateClass getDateClass() {
        return this.dateClass;
    }

    public void setDateClass(SQLiteConfig.DateClass dateClass2) {
        this.dateClass = dateClass2;
    }

    public SQLiteConfig.DatePrecision getDatePrecision() {
        return this.datePrecision;
    }

    public void setDatePrecision(SQLiteConfig.DatePrecision datePrecision2) {
        this.datePrecision = datePrecision2;
    }

    public String getDateStringFormat() {
        return this.dateStringFormat;
    }

    public void setDateStringFormat(String dateStringFormat2) {
        this.dateStringFormat = dateStringFormat2;
        this.dateFormat = FastDateFormat.getInstance(dateStringFormat2);
    }

    public FastDateFormat getDateFormat() {
        return this.dateFormat;
    }

    public boolean isAutoCommit() {
        return this.autoCommit;
    }

    public void setAutoCommit(boolean autoCommit2) {
        this.autoCommit = autoCommit2;
    }

    public int getTransactionIsolation() {
        return this.transactionIsolation;
    }

    public void setTransactionIsolation(int transactionIsolation2) {
        this.transactionIsolation = transactionIsolation2;
    }

    public SQLiteConfig.TransactionMode getTransactionMode() {
        return this.transactionMode;
    }

    public void setTransactionMode(SQLiteConfig.TransactionMode transactionMode2) {
        if (transactionMode2 == SQLiteConfig.TransactionMode.DEFFERED) {
            transactionMode2 = SQLiteConfig.TransactionMode.DEFERRED;
        }
        this.transactionMode = transactionMode2;
    }

    static {
        beginCommandMap.put(SQLiteConfig.TransactionMode.DEFERRED, "begin;");
        beginCommandMap.put(SQLiteConfig.TransactionMode.IMMEDIATE, "begin immediate;");
        beginCommandMap.put(SQLiteConfig.TransactionMode.EXCLUSIVE, "begin exclusive;");
    }

    /* access modifiers changed from: package-private */
    public String transactionPrefix() {
        return beginCommandMap.get(this.transactionMode);
    }
}
