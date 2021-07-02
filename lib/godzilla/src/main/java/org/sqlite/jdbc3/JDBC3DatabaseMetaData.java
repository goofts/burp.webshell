package org.sqlite.jdbc3;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javassist.compiler.TokenId;
import org.sqlite.SQLiteConnection;
import org.sqlite.core.CoreDatabaseMetaData;
import org.sqlite.core.CoreStatement;
import org.sqlite.util.StringUtils;

public abstract class JDBC3DatabaseMetaData extends CoreDatabaseMetaData {
    protected static final Pattern PK_NAMED_PATTERN = Pattern.compile(".*CONSTRAINT\\s*(.*?)\\s*PRIMARY\\s+KEY\\s*\\((.*?)\\).*", 34);
    protected static final Pattern PK_UNNAMED_PATTERN = Pattern.compile(".*PRIMARY\\s+KEY\\s*\\((.*?)\\).*", 34);
    private static final Map<String, Integer> RULE_MAP = new HashMap();
    protected static final Pattern TYPE_FLOAT = Pattern.compile(".*(REAL|FLOA|DOUB|DEC|NUM).*");
    protected static final Pattern TYPE_INTEGER = Pattern.compile(".*(INT|BOOL).*");
    protected static final Pattern TYPE_VARCHAR = Pattern.compile(".*(CHAR|CLOB|TEXT|BLOB).*");
    private static String driverName;
    private static String driverVersion;

    static {
        InputStream sqliteJdbcPropStream = null;
        try {
            InputStream sqliteJdbcPropStream2 = JDBC3DatabaseMetaData.class.getClassLoader().getResourceAsStream("sqlite-jdbc.properties");
            if (sqliteJdbcPropStream2 == null) {
                throw new IOException("Cannot load sqlite-jdbc.properties from jar");
            }
            Properties sqliteJdbcProp = new Properties();
            sqliteJdbcProp.load(sqliteJdbcPropStream2);
            driverName = sqliteJdbcProp.getProperty("name");
            driverVersion = sqliteJdbcProp.getProperty("version");
            if (sqliteJdbcPropStream2 != null) {
                try {
                    sqliteJdbcPropStream2.close();
                } catch (Exception e) {
                }
            }
            RULE_MAP.put("NO ACTION", 3);
            RULE_MAP.put("CASCADE", 0);
            RULE_MAP.put("RESTRICT", 1);
            RULE_MAP.put("SET NULL", 2);
            RULE_MAP.put("SET DEFAULT", 4);
        } catch (Exception e2) {
            driverName = "SQLite JDBC";
            driverVersion = "3.0.0-UNKNOWN";
            if (0 != 0) {
                try {
                    sqliteJdbcPropStream.close();
                } catch (Exception e3) {
                }
            }
        } catch (Throwable th) {
            if (0 != 0) {
                try {
                    sqliteJdbcPropStream.close();
                } catch (Exception e4) {
                }
            }
            throw th;
        }
    }

    protected JDBC3DatabaseMetaData(SQLiteConnection conn) {
        super(conn);
    }

    @Override // java.sql.DatabaseMetaData
    public Connection getConnection() {
        return this.conn;
    }

    @Override // java.sql.DatabaseMetaData
    public int getDatabaseMajorVersion() throws SQLException {
        return Integer.valueOf(this.conn.libversion().split("\\.")[0]).intValue();
    }

    @Override // java.sql.DatabaseMetaData
    public int getDatabaseMinorVersion() throws SQLException {
        return Integer.valueOf(this.conn.libversion().split("\\.")[1]).intValue();
    }

    public int getDriverMajorVersion() {
        return Integer.valueOf(driverVersion.split("\\.")[0]).intValue();
    }

    public int getDriverMinorVersion() {
        return Integer.valueOf(driverVersion.split("\\.")[1]).intValue();
    }

    @Override // java.sql.DatabaseMetaData
    public int getJDBCMajorVersion() {
        return 2;
    }

    @Override // java.sql.DatabaseMetaData
    public int getJDBCMinorVersion() {
        return 1;
    }

    @Override // java.sql.DatabaseMetaData
    public int getDefaultTransactionIsolation() {
        return 8;
    }

    @Override // java.sql.DatabaseMetaData
    public int getMaxBinaryLiteralLength() {
        return 0;
    }

    @Override // java.sql.DatabaseMetaData
    public int getMaxCatalogNameLength() {
        return 0;
    }

    @Override // java.sql.DatabaseMetaData
    public int getMaxCharLiteralLength() {
        return 0;
    }

    @Override // java.sql.DatabaseMetaData
    public int getMaxColumnNameLength() {
        return 0;
    }

    @Override // java.sql.DatabaseMetaData
    public int getMaxColumnsInGroupBy() {
        return 0;
    }

    @Override // java.sql.DatabaseMetaData
    public int getMaxColumnsInIndex() {
        return 0;
    }

    @Override // java.sql.DatabaseMetaData
    public int getMaxColumnsInOrderBy() {
        return 0;
    }

    @Override // java.sql.DatabaseMetaData
    public int getMaxColumnsInSelect() {
        return 0;
    }

    @Override // java.sql.DatabaseMetaData
    public int getMaxColumnsInTable() {
        return 0;
    }

    @Override // java.sql.DatabaseMetaData
    public int getMaxConnections() {
        return 0;
    }

    @Override // java.sql.DatabaseMetaData
    public int getMaxCursorNameLength() {
        return 0;
    }

    @Override // java.sql.DatabaseMetaData
    public int getMaxIndexLength() {
        return 0;
    }

    @Override // java.sql.DatabaseMetaData
    public int getMaxProcedureNameLength() {
        return 0;
    }

    @Override // java.sql.DatabaseMetaData
    public int getMaxRowSize() {
        return 0;
    }

    @Override // java.sql.DatabaseMetaData
    public int getMaxSchemaNameLength() {
        return 0;
    }

    @Override // java.sql.DatabaseMetaData
    public int getMaxStatementLength() {
        return 0;
    }

    @Override // java.sql.DatabaseMetaData
    public int getMaxStatements() {
        return 0;
    }

    @Override // java.sql.DatabaseMetaData
    public int getMaxTableNameLength() {
        return 0;
    }

    @Override // java.sql.DatabaseMetaData
    public int getMaxTablesInSelect() {
        return 0;
    }

    @Override // java.sql.DatabaseMetaData
    public int getMaxUserNameLength() {
        return 0;
    }

    @Override // java.sql.DatabaseMetaData
    public int getResultSetHoldability() {
        return 2;
    }

    @Override // java.sql.DatabaseMetaData
    public int getSQLStateType() {
        return 2;
    }

    @Override // java.sql.DatabaseMetaData
    public String getDatabaseProductName() {
        return "SQLite";
    }

    @Override // java.sql.DatabaseMetaData
    public String getDatabaseProductVersion() throws SQLException {
        return this.conn.libversion();
    }

    @Override // java.sql.DatabaseMetaData
    public String getDriverName() {
        return driverName;
    }

    @Override // java.sql.DatabaseMetaData
    public String getDriverVersion() {
        return driverVersion;
    }

    @Override // java.sql.DatabaseMetaData
    public String getExtraNameCharacters() {
        return "";
    }

    @Override // java.sql.DatabaseMetaData
    public String getCatalogSeparator() {
        return ".";
    }

    @Override // java.sql.DatabaseMetaData
    public String getCatalogTerm() {
        return "catalog";
    }

    @Override // java.sql.DatabaseMetaData
    public String getSchemaTerm() {
        return "schema";
    }

    @Override // java.sql.DatabaseMetaData
    public String getProcedureTerm() {
        return "not_implemented";
    }

    @Override // java.sql.DatabaseMetaData
    public String getSearchStringEscape() {
        return null;
    }

    @Override // java.sql.DatabaseMetaData
    public String getIdentifierQuoteString() {
        return "\"";
    }

    @Override // java.sql.DatabaseMetaData
    public String getSQLKeywords() {
        return "ABORT,ACTION,AFTER,ANALYZE,ATTACH,AUTOINCREMENT,BEFORE,CASCADE,CONFLICT,DATABASE,DEFERRABLE,DEFERRED,DESC,DETACH,EXCLUSIVE,EXPLAIN,FAIL,GLOB,IGNORE,INDEX,INDEXED,INITIALLY,INSTEAD,ISNULL,KEY,LIMIT,NOTNULL,OFFSET,PLAN,PRAGMA,QUERY,RAISE,REGEXP,REINDEX,RENAME,REPLACE,RESTRICT,TEMP,TEMPORARY,TRANSACTION,VACUUM,VIEW,VIRTUAL";
    }

    @Override // java.sql.DatabaseMetaData
    public String getNumericFunctions() {
        return "";
    }

    @Override // java.sql.DatabaseMetaData
    public String getStringFunctions() {
        return "";
    }

    @Override // java.sql.DatabaseMetaData
    public String getSystemFunctions() {
        return "";
    }

    @Override // java.sql.DatabaseMetaData
    public String getTimeDateFunctions() {
        return "DATE,TIME,DATETIME,JULIANDAY,STRFTIME";
    }

    @Override // java.sql.DatabaseMetaData
    public String getURL() {
        return this.conn.getUrl();
    }

    @Override // java.sql.DatabaseMetaData
    public String getUserName() {
        return null;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean allProceduresAreCallable() {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean allTablesAreSelectable() {
        return true;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean dataDefinitionCausesTransactionCommit() {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean dataDefinitionIgnoredInTransactions() {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean doesMaxRowSizeIncludeBlobs() {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean deletesAreDetected(int type) {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean insertsAreDetected(int type) {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean isCatalogAtStart() {
        return true;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean locatorsUpdateCopy() {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean nullPlusNonNullIsNull() {
        return true;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean nullsAreSortedAtEnd() {
        return !nullsAreSortedAtStart();
    }

    @Override // java.sql.DatabaseMetaData
    public boolean nullsAreSortedAtStart() {
        return true;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean nullsAreSortedHigh() {
        return true;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean nullsAreSortedLow() {
        return !nullsAreSortedHigh();
    }

    @Override // java.sql.DatabaseMetaData
    public boolean othersDeletesAreVisible(int type) {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean othersInsertsAreVisible(int type) {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean othersUpdatesAreVisible(int type) {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean ownDeletesAreVisible(int type) {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean ownInsertsAreVisible(int type) {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean ownUpdatesAreVisible(int type) {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean storesLowerCaseIdentifiers() {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean storesLowerCaseQuotedIdentifiers() {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean storesMixedCaseIdentifiers() {
        return true;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean storesMixedCaseQuotedIdentifiers() {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean storesUpperCaseIdentifiers() {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean storesUpperCaseQuotedIdentifiers() {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsAlterTableWithAddColumn() {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsAlterTableWithDropColumn() {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsANSI92EntryLevelSQL() {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsANSI92FullSQL() {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsANSI92IntermediateSQL() {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsBatchUpdates() {
        return true;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsCatalogsInDataManipulation() {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsCatalogsInIndexDefinitions() {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsCatalogsInPrivilegeDefinitions() {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsCatalogsInProcedureCalls() {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsCatalogsInTableDefinitions() {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsColumnAliasing() {
        return true;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsConvert() {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsConvert(int fromType, int toType) {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsCorrelatedSubqueries() {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsDataDefinitionAndDataManipulationTransactions() {
        return true;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsDataManipulationTransactionsOnly() {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsDifferentTableCorrelationNames() {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsExpressionsInOrderBy() {
        return true;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsMinimumSQLGrammar() {
        return true;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsCoreSQLGrammar() {
        return true;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsExtendedSQLGrammar() {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsLimitedOuterJoins() {
        return true;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsFullOuterJoins() {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsGetGeneratedKeys() {
        return true;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsGroupBy() {
        return true;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsGroupByBeyondSelect() {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsGroupByUnrelated() {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsIntegrityEnhancementFacility() {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsLikeEscapeClause() {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsMixedCaseIdentifiers() {
        return true;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsMixedCaseQuotedIdentifiers() {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsMultipleOpenResults() {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsMultipleResultSets() {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsMultipleTransactions() {
        return true;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsNamedParameters() {
        return true;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsNonNullableColumns() {
        return true;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsOpenCursorsAcrossCommit() {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsOpenCursorsAcrossRollback() {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsOpenStatementsAcrossCommit() {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsOpenStatementsAcrossRollback() {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsOrderByUnrelated() {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsOuterJoins() {
        return true;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsPositionedDelete() {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsPositionedUpdate() {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsResultSetConcurrency(int t, int c) {
        return t == 1003 && c == 1007;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsResultSetHoldability(int h) {
        return h == 2;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsResultSetType(int t) {
        return t == 1003;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsSavepoints() {
        return true;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsSchemasInDataManipulation() {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsSchemasInIndexDefinitions() {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsSchemasInPrivilegeDefinitions() {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsSchemasInProcedureCalls() {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsSchemasInTableDefinitions() {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsSelectForUpdate() {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsStatementPooling() {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsStoredProcedures() {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsSubqueriesInComparisons() {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsSubqueriesInExists() {
        return true;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsSubqueriesInIns() {
        return true;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsSubqueriesInQuantifieds() {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsTableCorrelationNames() {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsTransactionIsolationLevel(int level) {
        return level == 8;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsTransactions() {
        return true;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsUnion() {
        return true;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsUnionAll() {
        return true;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean updatesAreDetected(int type) {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean usesLocalFilePerTable() {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean usesLocalFiles() {
        return true;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean isReadOnly() throws SQLException {
        return this.conn.isReadOnly();
    }

    @Override // java.sql.DatabaseMetaData
    public ResultSet getAttributes(String c, String s, String t, String a) throws SQLException {
        if (this.getAttributes == null) {
            this.getAttributes = this.conn.prepareStatement("select null as TYPE_CAT, null as TYPE_SCHEM, null as TYPE_NAME, null as ATTR_NAME, null as DATA_TYPE, null as ATTR_TYPE_NAME, null as ATTR_SIZE, null as DECIMAL_DIGITS, null as NUM_PREC_RADIX, null as NULLABLE, null as REMARKS, null as ATTR_DEF, null as SQL_DATA_TYPE, null as SQL_DATETIME_SUB, null as CHAR_OCTET_LENGTH, null as ORDINAL_POSITION, null as IS_NULLABLE, null as SCOPE_CATALOG, null as SCOPE_SCHEMA, null as SCOPE_TABLE, null as SOURCE_DATA_TYPE limit 0;");
        }
        return this.getAttributes.executeQuery();
    }

    @Override // java.sql.DatabaseMetaData
    public ResultSet getBestRowIdentifier(String c, String s, String t, int scope, boolean n) throws SQLException {
        if (this.getBestRowIdentifier == null) {
            this.getBestRowIdentifier = this.conn.prepareStatement("select null as SCOPE, null as COLUMN_NAME, null as DATA_TYPE, null as TYPE_NAME, null as COLUMN_SIZE, null as BUFFER_LENGTH, null as DECIMAL_DIGITS, null as PSEUDO_COLUMN limit 0;");
        }
        return this.getBestRowIdentifier.executeQuery();
    }

    @Override // java.sql.DatabaseMetaData
    public ResultSet getColumnPrivileges(String c, String s, String t, String colPat) throws SQLException {
        if (this.getColumnPrivileges == null) {
            this.getColumnPrivileges = this.conn.prepareStatement("select null as TABLE_CAT, null as TABLE_SCHEM, null as TABLE_NAME, null as COLUMN_NAME, null as GRANTOR, null as GRANTEE, null as PRIVILEGE, null as IS_GRANTABLE limit 0;");
        }
        return this.getColumnPrivileges.executeQuery();
    }

    @Override // java.sql.DatabaseMetaData
    public ResultSet getColumns(String c, String s, String tblNamePattern, String colNamePattern) throws SQLException {
        Statement statColAutoinc;
        int colJavaType;
        String escape;
        checkOpen();
        StringBuilder sql = new StringBuilder(700);
        sql.append("select null as TABLE_CAT, null as TABLE_SCHEM, tblname as TABLE_NAME, ").append("cn as COLUMN_NAME, ct as DATA_TYPE, tn as TYPE_NAME, 2000000000 as COLUMN_SIZE, ").append("2000000000 as BUFFER_LENGTH, 10   as DECIMAL_DIGITS, 10   as NUM_PREC_RADIX, ").append("colnullable as NULLABLE, null as REMARKS, colDefault as COLUMN_DEF, ").append("0    as SQL_DATA_TYPE, 0    as SQL_DATETIME_SUB, 2000000000 as CHAR_OCTET_LENGTH, ").append("ordpos as ORDINAL_POSITION, (case colnullable when 0 then 'NO' when 1 then 'YES' else '' end)").append("    as IS_NULLABLE, null as SCOPE_CATLOG, null as SCOPE_SCHEMA, ").append("null as SCOPE_TABLE, null as SOURCE_DATA_TYPE, ").append("(case colautoincrement when 0 then 'NO' when 1 then 'YES' else '' end) as IS_AUTOINCREMENT, ").append("'' as IS_GENERATEDCOLUMN from (");
        boolean colFound = false;
        ResultSet rs = null;
        try {
            ResultSet rs2 = getTables(c, s, tblNamePattern, new String[]{"TABLE", "VIEW"});
            while (rs2.next()) {
                String tableName = rs2.getString(3);
                this.conn.createStatement();
                ResultSet rsColAutoinc = null;
                try {
                    statColAutoinc = this.conn.createStatement();
                    rsColAutoinc = statColAutoinc.executeQuery("SELECT LIKE('%autoincrement%', LOWER(sql)) FROM sqlite_master WHERE LOWER(name) = LOWER('" + escape(tableName) + "') AND TYPE IN ('table', 'view')");
                    rsColAutoinc.next();
                    boolean isAutoIncrement = rsColAutoinc.getInt(1) == 1;
                    if (rsColAutoinc != null) {
                        try {
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (statColAutoinc != null) {
                        try {
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                    Statement colstat = this.conn.createStatement();
                    ResultSet rscol = null;
                    try {
                        rscol = colstat.executeQuery("PRAGMA table_info('" + escape(tableName) + "')");
                        int i = 0;
                        while (rscol.next()) {
                            String colName = rscol.getString(2);
                            String colType = rscol.getString(3);
                            String colNotNull = rscol.getString(4);
                            String colDefault = rscol.getString(5);
                            boolean isPk = "1".equals(rscol.getString(6));
                            int colNullable = 2;
                            if (colNotNull != null) {
                                colNullable = colNotNull.equals("0") ? 1 : 0;
                            }
                            if (colFound) {
                                sql.append(" union all ");
                            }
                            colFound = true;
                            String colType2 = colType == null ? "TEXT" : colType.toUpperCase();
                            int colAutoIncrement = 0;
                            if (isPk && isAutoIncrement) {
                                colAutoIncrement = 1;
                            }
                            if (TYPE_INTEGER.matcher(colType2).find()) {
                                colJavaType = 4;
                            } else if (TYPE_VARCHAR.matcher(colType2).find()) {
                                colJavaType = 12;
                            } else if (TYPE_FLOAT.matcher(colType2).find()) {
                                colJavaType = 6;
                            } else {
                                colJavaType = 12;
                            }
                            StringBuilder append = sql.append("select ").append(i + 1).append(" as ordpos, ").append(colNullable).append(" as colnullable,").append("'").append(colJavaType).append("' as ct, ").append("'").append(tableName).append("' as tblname, ").append("'").append(escape(colName)).append("' as cn, ").append("'").append(escape(colType2)).append("' as tn, ");
                            if (colDefault == null) {
                                escape = null;
                            } else {
                                escape = escape(colDefault);
                            }
                            append.append(quote(escape)).append(" as colDefault,").append(colAutoIncrement).append(" as colautoincrement");
                            if (colNamePattern != null) {
                                sql.append(" where upper(cn) like upper('").append(escape(colNamePattern)).append("')");
                            }
                            i++;
                        }
                    } finally {
                        if (rscol != null) {
                            try {
                                rscol.close();
                            } catch (SQLException e3) {
                            }
                        }
                        if (colstat != null) {
                            try {
                                colstat.close();
                            } catch (SQLException e4) {
                            }
                        }
                    }
                } finally {
                    if (rsColAutoinc != null) {
                        try {
                            rsColAutoinc.close();
                        } catch (Exception e5) {
                            e5.printStackTrace();
                        }
                    }
                    if (statColAutoinc != null) {
                        try {
                            statColAutoinc.close();
                        } catch (Exception e6) {
                            e6.printStackTrace();
                        }
                    }
                }
            }
            if (rs2 != null) {
                try {
                    rs2.close();
                } catch (Exception e7) {
                    e7.printStackTrace();
                }
            }
            if (colFound) {
                sql.append(") order by TABLE_SCHEM, TABLE_NAME, ORDINAL_POSITION;");
            } else {
                sql.append("select null as ordpos, null as colnullable, null as ct, null as tblname, null as cn, null as tn, null as colDefault, null as colautoincrement) limit 0;");
            }
            return ((CoreStatement) this.conn.createStatement()).executeQuery(sql.toString(), true);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e8) {
                    e8.printStackTrace();
                }
            }
        }
    }

    @Override // java.sql.DatabaseMetaData
    public ResultSet getCrossReference(String pc, String ps, String pt, String fc, String fs, String ft) throws SQLException {
        if (pt == null) {
            return getExportedKeys(fc, fs, ft);
        }
        if (ft == null) {
            return getImportedKeys(pc, ps, pt);
        }
        StringBuilder query = new StringBuilder();
        query.append("select ").append(quote(pc)).append(" as PKTABLE_CAT, ").append(quote(ps)).append(" as PKTABLE_SCHEM, ").append(quote(pt)).append(" as PKTABLE_NAME, ").append("'' as PKCOLUMN_NAME, ").append(quote(fc)).append(" as FKTABLE_CAT, ").append(quote(fs)).append(" as FKTABLE_SCHEM, ").append(quote(ft)).append(" as FKTABLE_NAME, ").append("'' as FKCOLUMN_NAME, -1 as KEY_SEQ, 3 as UPDATE_RULE, 3 as DELETE_RULE, '' as FK_NAME, '' as PK_NAME, ").append(Integer.toString(5)).append(" as DEFERRABILITY limit 0 ");
        return ((CoreStatement) this.conn.createStatement()).executeQuery(query.toString(), true);
    }

    @Override // java.sql.DatabaseMetaData
    public ResultSet getSchemas() throws SQLException {
        if (this.getSchemas == null) {
            this.getSchemas = this.conn.prepareStatement("select null as TABLE_SCHEM, null as TABLE_CATALOG limit 0;");
        }
        return this.getSchemas.executeQuery();
    }

    @Override // java.sql.DatabaseMetaData
    public ResultSet getCatalogs() throws SQLException {
        if (this.getCatalogs == null) {
            this.getCatalogs = this.conn.prepareStatement("select null as TABLE_CAT limit 0;");
        }
        return this.getCatalogs.executeQuery();
    }

    @Override // java.sql.DatabaseMetaData
    public ResultSet getPrimaryKeys(String c, String s, String table) throws SQLException {
        PrimaryKeyFinder pkFinder = new PrimaryKeyFinder(table);
        String[] columns = pkFinder.getColumns();
        Statement stat = this.conn.createStatement();
        StringBuilder sql = new StringBuilder(512);
        sql.append("select null as TABLE_CAT, null as TABLE_SCHEM, '").append(escape(table)).append("' as TABLE_NAME, cn as COLUMN_NAME, ks as KEY_SEQ, pk as PK_NAME from (");
        if (columns == null) {
            sql.append("select null as cn, null as pk, 0 as ks) limit 0;");
            return ((CoreStatement) stat).executeQuery(sql.toString(), true);
        }
        String pkName = pkFinder.getName();
        if (pkName != null) {
            pkName = "'" + pkName + "'";
        }
        for (int i = 0; i < columns.length; i++) {
            if (i > 0) {
                sql.append(" union ");
            }
            sql.append("select ").append(pkName).append(" as pk, '").append(escape(unquoteIdentifier(columns[i]))).append("' as cn, ").append(i + 1).append(" as ks");
        }
        return ((CoreStatement) stat).executeQuery(sql.append(") order by cn;").toString(), true);
    }

    @Override // java.sql.DatabaseMetaData
    public ResultSet getExportedKeys(String catalog, String schema, String table) throws SQLException {
        PrimaryKeyFinder pkFinder = new PrimaryKeyFinder(table);
        String[] pkColumns = pkFinder.getColumns();
        Statement stat = this.conn.createStatement();
        String catalog2 = catalog != null ? quote(catalog) : null;
        String schema2 = schema != null ? quote(schema) : null;
        StringBuilder exportedKeysQuery = new StringBuilder(512);
        String target = null;
        int count = 0;
        if (pkColumns != null) {
            ResultSet rs = stat.executeQuery("select name from sqlite_master where type = 'table'");
            ArrayList<String> tableList = new ArrayList<>();
            while (rs.next()) {
                String tblname = rs.getString(1);
                tableList.add(tblname);
                if (tblname.equalsIgnoreCase(table)) {
                    target = tblname;
                }
            }
            rs.close();
            Iterator<String> it = tableList.iterator();
            while (it.hasNext()) {
                String tbl = it.next();
                try {
                    for (ImportedKeyFinder.ForeignKey foreignKey : new ImportedKeyFinder(tbl).getFkList()) {
                        String PKTabName = foreignKey.getPkTableName();
                        if (PKTabName != null && PKTabName.equalsIgnoreCase(target)) {
                            for (int j = 0; j < foreignKey.getColumnMappingCount(); j++) {
                                int keySeq = j + 1;
                                String[] columnMapping = foreignKey.getColumnMapping(j);
                                String PKColName = columnMapping[1];
                                if (PKColName == null) {
                                    PKColName = "";
                                }
                                String FKColName = columnMapping[0];
                                if (FKColName == null) {
                                    FKColName = "";
                                }
                                boolean usePkName = false;
                                int k = 0;
                                while (true) {
                                    if (k >= pkColumns.length) {
                                        break;
                                    }
                                    if (pkColumns[k] != null && pkColumns[k].equalsIgnoreCase(PKColName)) {
                                        usePkName = true;
                                        break;
                                    }
                                    k++;
                                }
                                exportedKeysQuery.append(count > 0 ? " union all select " : "select ").append(Integer.toString(keySeq)).append(" as ks, '").append(escape(tbl)).append("' as fkt, '").append(escape(FKColName)).append("' as fcn, '").append(escape(PKColName)).append("' as pcn, '").append(escape((!usePkName || pkFinder.getName() == null) ? "" : pkFinder.getName())).append("' as pkn, ").append(RULE_MAP.get(foreignKey.getOnUpdate())).append(" as ur, ").append(RULE_MAP.get(foreignKey.getOnDelete())).append(" as dr, ");
                                String fkName = foreignKey.getFkName();
                                if (fkName != null) {
                                    exportedKeysQuery.append("'").append(escape(fkName)).append("' as fkn");
                                } else {
                                    exportedKeysQuery.append("'' as fkn");
                                }
                                count++;
                            }
                        }
                    }
                } finally {
                    if (rs != null) {
                        try {
                            rs.close();
                        } catch (SQLException e) {
                        }
                    }
                }
            }
        }
        boolean hasImportedKey = count > 0;
        StringBuilder sql = new StringBuilder(512);
        sql.append("select ").append(catalog2).append(" as PKTABLE_CAT, ").append(schema2).append(" as PKTABLE_SCHEM, ").append(quote(target)).append(" as PKTABLE_NAME, ").append(hasImportedKey ? "pcn" : "''").append(" as PKCOLUMN_NAME, ").append(catalog2).append(" as FKTABLE_CAT, ").append(schema2).append(" as FKTABLE_SCHEM, ").append(hasImportedKey ? "fkt" : "''").append(" as FKTABLE_NAME, ").append(hasImportedKey ? "fcn" : "''").append(" as FKCOLUMN_NAME, ").append(hasImportedKey ? "ks" : "-1").append(" as KEY_SEQ, ").append(hasImportedKey ? "ur" : "3").append(" as UPDATE_RULE, ").append(hasImportedKey ? "dr" : "3").append(" as DELETE_RULE, ").append(hasImportedKey ? "fkn" : "''").append(" as FK_NAME, ").append(hasImportedKey ? "pkn" : "''").append(" as PK_NAME, ").append(Integer.toString(5)).append(" as DEFERRABILITY ");
        if (hasImportedKey) {
            sql.append("from (").append((CharSequence) exportedKeysQuery).append(") ORDER BY FKTABLE_CAT, FKTABLE_SCHEM, FKTABLE_NAME, KEY_SEQ");
        } else {
            sql.append("limit 0");
        }
        return ((CoreStatement) stat).executeQuery(sql.toString(), true);
    }

    private StringBuilder appendDummyForeignKeyList(StringBuilder sql) {
        sql.append("select -1 as ks, '' as ptn, '' as fcn, '' as pcn, ").append(3).append(" as ur, ").append(3).append(" as dr, ").append(" '' as fkn, ").append(" '' as pkn ").append(") limit 0;");
        return sql;
    }

    @Override // java.sql.DatabaseMetaData
    public ResultSet getImportedKeys(String catalog, String schema, String table) throws SQLException {
        String quote;
        String quote2;
        Statement stat = this.conn.createStatement();
        StringBuilder sql = new StringBuilder(700);
        sql.append("select ").append(quote(catalog)).append(" as PKTABLE_CAT, ").append(quote(schema)).append(" as PKTABLE_SCHEM, ").append("ptn as PKTABLE_NAME, pcn as PKCOLUMN_NAME, ").append(quote(catalog)).append(" as FKTABLE_CAT, ").append(quote(schema)).append(" as FKTABLE_SCHEM, ").append(quote(table)).append(" as FKTABLE_NAME, ").append("fcn as FKCOLUMN_NAME, ks as KEY_SEQ, ur as UPDATE_RULE, dr as DELETE_RULE, fkn as FK_NAME, pkn as PK_NAME, ").append(Integer.toString(5)).append(" as DEFERRABILITY from (");
        try {
            ResultSet rs = stat.executeQuery("pragma foreign_key_list('" + escape(table) + "');");
            List<ImportedKeyFinder.ForeignKey> fkNames = new ImportedKeyFinder(table).getFkList();
            int i = 0;
            while (rs.next()) {
                int keySeq = rs.getInt(2) + 1;
                int keyId = rs.getInt(1);
                String PKTabName = rs.getString(3);
                String FKColName = rs.getString(4);
                String PKColName = rs.getString(5);
                PrimaryKeyFinder pkFinder = new PrimaryKeyFinder(PKTabName);
                String pkName = pkFinder.getName();
                if (PKColName == null) {
                    PKColName = pkFinder.getColumns()[0];
                }
                String updateRule = rs.getString(6);
                String deleteRule = rs.getString(7);
                if (i > 0) {
                    sql.append(" union all ");
                }
                String fkName = null;
                if (fkNames.size() > keyId) {
                    fkName = fkNames.get(keyId).getFkName();
                }
                StringBuilder append = sql.append("select ").append(keySeq).append(" as ks,").append("'").append(escape(PKTabName)).append("' as ptn, '").append(escape(FKColName)).append("' as fcn, '").append(escape(PKColName)).append("' as pcn,").append("case '").append(escape(updateRule)).append("'").append(" when 'NO ACTION' then ").append(3).append(" when 'CASCADE' then ").append(0).append(" when 'RESTRICT' then ").append(1).append(" when 'SET NULL' then ").append(2).append(" when 'SET DEFAULT' then ").append(4).append(" end as ur, ").append("case '").append(escape(deleteRule)).append("'").append(" when 'NO ACTION' then ").append(3).append(" when 'CASCADE' then ").append(0).append(" when 'RESTRICT' then ").append(1).append(" when 'SET NULL' then ").append(2).append(" when 'SET DEFAULT' then ").append(4).append(" end as dr, ");
                if (fkName == null) {
                    quote = "''";
                } else {
                    quote = quote(fkName);
                }
                StringBuilder append2 = append.append(quote).append(" as fkn, ");
                if (pkName == null) {
                    quote2 = "''";
                } else {
                    quote2 = quote(pkName);
                }
                append2.append(quote2).append(" as pkn");
                i++;
            }
            rs.close();
            if (i == 0) {
                sql = appendDummyForeignKeyList(sql);
            }
            sql.append(") ORDER BY PKTABLE_CAT, PKTABLE_SCHEM, PKTABLE_NAME, KEY_SEQ;");
            return ((CoreStatement) stat).executeQuery(sql.toString(), true);
        } catch (SQLException e) {
            return ((CoreStatement) stat).executeQuery(appendDummyForeignKeyList(sql).toString(), true);
        }
    }

    @Override // java.sql.DatabaseMetaData
    public ResultSet getIndexInfo(String c, String s, String table, boolean u, boolean approximate) throws SQLException {
        Statement stat = this.conn.createStatement();
        StringBuilder sql = new StringBuilder((int) TokenId.BadToken);
        sql.append("select null as TABLE_CAT, null as TABLE_SCHEM, '").append(escape(table)).append("' as TABLE_NAME, un as NON_UNIQUE, null as INDEX_QUALIFIER, n as INDEX_NAME, ").append(Integer.toString(3)).append(" as TYPE, op as ORDINAL_POSITION, ").append("cn as COLUMN_NAME, null as ASC_OR_DESC, 0 as CARDINALITY, 0 as PAGES, null as FILTER_CONDITION from (");
        ResultSet rs = stat.executeQuery("pragma index_list('" + escape(table) + "');");
        ArrayList<ArrayList<Object>> indexList = new ArrayList<>();
        while (rs.next()) {
            indexList.add(new ArrayList<>());
            indexList.get(indexList.size() - 1).add(rs.getString(2));
            indexList.get(indexList.size() - 1).add(Integer.valueOf(rs.getInt(3)));
        }
        rs.close();
        if (indexList.size() == 0) {
            sql.append("select null as un, null as n, null as op, null as cn) limit 0;");
            return ((CoreStatement) stat).executeQuery(sql.toString(), true);
        }
        Iterator<ArrayList<Object>> indexIterator = indexList.iterator();
        ArrayList<String> unionAll = new ArrayList<>();
        while (indexIterator.hasNext()) {
            ArrayList<Object> currentIndex = indexIterator.next();
            String indexName = currentIndex.get(0).toString();
            ResultSet rs2 = stat.executeQuery("pragma index_info('" + escape(indexName) + "');");
            while (rs2.next()) {
                StringBuilder sqlRow = new StringBuilder();
                String colName = rs2.getString(3);
                sqlRow.append("select ").append(Integer.toString(1 - ((Integer) currentIndex.get(1)).intValue())).append(" as un,'").append(escape(indexName)).append("' as n,").append(Integer.toString(rs2.getInt(1) + 1)).append(" as op,");
                if (colName == null) {
                    sqlRow.append("null");
                } else {
                    sqlRow.append("'").append(escape(colName)).append("'");
                }
                sqlRow.append(" as cn");
                unionAll.add(sqlRow.toString());
            }
            rs2.close();
        }
        return ((CoreStatement) stat).executeQuery(sql.append(StringUtils.join(unionAll, " union all ")).append(");").toString(), true);
    }

    @Override // java.sql.DatabaseMetaData
    public ResultSet getProcedureColumns(String c, String s, String p, String colPat) throws SQLException {
        if (this.getProcedureColumns == null) {
            this.getProcedureColumns = this.conn.prepareStatement("select null as PROCEDURE_CAT, null as PROCEDURE_SCHEM, null as PROCEDURE_NAME, null as COLUMN_NAME, null as COLUMN_TYPE, null as DATA_TYPE, null as TYPE_NAME, null as PRECISION, null as LENGTH, null as SCALE, null as RADIX, null as NULLABLE, null as REMARKS limit 0;");
        }
        return this.getProcedureColumns.executeQuery();
    }

    @Override // java.sql.DatabaseMetaData
    public ResultSet getProcedures(String c, String s, String p) throws SQLException {
        if (this.getProcedures == null) {
            this.getProcedures = this.conn.prepareStatement("select null as PROCEDURE_CAT, null as PROCEDURE_SCHEM, null as PROCEDURE_NAME, null as UNDEF1, null as UNDEF2, null as UNDEF3, null as REMARKS, null as PROCEDURE_TYPE limit 0;");
        }
        return this.getProcedures.executeQuery();
    }

    @Override // java.sql.DatabaseMetaData
    public ResultSet getSuperTables(String c, String s, String t) throws SQLException {
        if (this.getSuperTables == null) {
            this.getSuperTables = this.conn.prepareStatement("select null as TABLE_CAT, null as TABLE_SCHEM, null as TABLE_NAME, null as SUPERTABLE_NAME limit 0;");
        }
        return this.getSuperTables.executeQuery();
    }

    @Override // java.sql.DatabaseMetaData
    public ResultSet getSuperTypes(String c, String s, String t) throws SQLException {
        if (this.getSuperTypes == null) {
            this.getSuperTypes = this.conn.prepareStatement("select null as TYPE_CAT, null as TYPE_SCHEM, null as TYPE_NAME, null as SUPERTYPE_CAT, null as SUPERTYPE_SCHEM, null as SUPERTYPE_NAME limit 0;");
        }
        return this.getSuperTypes.executeQuery();
    }

    @Override // java.sql.DatabaseMetaData
    public ResultSet getTablePrivileges(String c, String s, String t) throws SQLException {
        if (this.getTablePrivileges == null) {
            this.getTablePrivileges = this.conn.prepareStatement("select  null as TABLE_CAT, null as TABLE_SCHEM, null as TABLE_NAME, null as GRANTOR, null GRANTEE,  null as PRIVILEGE, null as IS_GRANTABLE limit 0;");
        }
        return this.getTablePrivileges.executeQuery();
    }

    @Override // java.sql.DatabaseMetaData
    public synchronized ResultSet getTables(String c, String s, String tblNamePattern, String[] types) throws SQLException {
        StringBuilder sql;
        checkOpen();
        String tblNamePattern2 = (tblNamePattern == null || "".equals(tblNamePattern)) ? "%" : escape(tblNamePattern);
        sql = new StringBuilder();
        sql.append("SELECT").append("\n");
        sql.append("  NULL AS TABLE_CAT,").append("\n");
        sql.append("  NULL AS TABLE_SCHEM,").append("\n");
        sql.append("  NAME AS TABLE_NAME,").append("\n");
        sql.append("  TYPE AS TABLE_TYPE,").append("\n");
        sql.append("  NULL AS REMARKS,").append("\n");
        sql.append("  NULL AS TYPE_CAT,").append("\n");
        sql.append("  NULL AS TYPE_SCHEM,").append("\n");
        sql.append("  NULL AS TYPE_NAME,").append("\n");
        sql.append("  NULL AS SELF_REFERENCING_COL_NAME,").append("\n");
        sql.append("  NULL AS REF_GENERATION").append("\n");
        sql.append("FROM").append("\n");
        sql.append("  (").append("\n");
        sql.append("    SELECT").append("\n");
        sql.append("      NAME,").append("\n");
        sql.append("      UPPER(TYPE) AS TYPE").append("\n");
        sql.append("    FROM").append("\n");
        sql.append("      sqlite_master").append("\n");
        sql.append("    WHERE").append("\n");
        sql.append("      NAME NOT LIKE 'sqlite\\_%' ESCAPE '\\'").append("\n");
        sql.append("      AND UPPER(TYPE) IN ('TABLE', 'VIEW')").append("\n");
        sql.append("    UNION ALL").append("\n");
        sql.append("    SELECT").append("\n");
        sql.append("      NAME,").append("\n");
        sql.append("      'GLOBAL TEMPORARY' AS TYPE").append("\n");
        sql.append("    FROM").append("\n");
        sql.append("      sqlite_temp_master").append("\n");
        sql.append("    UNION ALL").append("\n");
        sql.append("    SELECT").append("\n");
        sql.append("      NAME,").append("\n");
        sql.append("      'SYSTEM TABLE' AS TYPE").append("\n");
        sql.append("    FROM").append("\n");
        sql.append("      sqlite_master").append("\n");
        sql.append("    WHERE").append("\n");
        sql.append("      NAME LIKE 'sqlite\\_%' ESCAPE '\\'").append("\n");
        sql.append("  )").append("\n");
        sql.append(" WHERE TABLE_NAME LIKE '").append(tblNamePattern2).append("' AND TABLE_TYPE IN (");
        if (types == null || types.length == 0) {
            sql.append("'TABLE','VIEW'");
        } else {
            sql.append("'").append(types[0].toUpperCase()).append("'");
            for (int i = 1; i < types.length; i++) {
                sql.append(",'").append(types[i].toUpperCase()).append("'");
            }
        }
        sql.append(") ORDER BY TABLE_TYPE, TABLE_NAME;");
        return ((CoreStatement) this.conn.createStatement()).executeQuery(sql.toString(), true);
    }

    @Override // java.sql.DatabaseMetaData
    public ResultSet getTableTypes() throws SQLException {
        checkOpen();
        if (this.getTableTypes == null) {
            this.getTableTypes = this.conn.prepareStatement("SELECT 'TABLE' AS TABLE_TYPE UNION SELECT 'VIEW' AS TABLE_TYPE UNION SELECT 'SYSTEM TABLE' AS TABLE_TYPE UNION SELECT 'GLOBAL TEMPORARY' AS TABLE_TYPE;");
        }
        this.getTableTypes.clearParameters();
        return this.getTableTypes.executeQuery();
    }

    @Override // java.sql.DatabaseMetaData
    public ResultSet getTypeInfo() throws SQLException {
        if (this.getTypeInfo == null) {
            this.getTypeInfo = this.conn.prepareStatement("select tn as TYPE_NAME, dt as DATA_TYPE, 0 as PRECISION, null as LITERAL_PREFIX, null as LITERAL_SUFFIX, null as CREATE_PARAMS, 1 as NULLABLE, 1 as CASE_SENSITIVE, 3 as SEARCHABLE, 0 as UNSIGNED_ATTRIBUTE, 0 as FIXED_PREC_SCALE, 0 as AUTO_INCREMENT, null as LOCAL_TYPE_NAME, 0 as MINIMUM_SCALE, 0 as MAXIMUM_SCALE, 0 as SQL_DATA_TYPE, 0 as SQL_DATETIME_SUB, 10 as NUM_PREC_RADIX from (    select 'BLOB' as tn, 2004 as dt union    select 'NULL' as tn, 0 as dt union    select 'REAL' as tn, 7 as dt union    select 'TEXT' as tn, 12 as dt union    select 'INTEGER' as tn, 4 as dt) order by TYPE_NAME;");
        }
        this.getTypeInfo.clearParameters();
        return this.getTypeInfo.executeQuery();
    }

    @Override // java.sql.DatabaseMetaData
    public ResultSet getUDTs(String c, String s, String t, int[] types) throws SQLException {
        if (this.getUDTs == null) {
            this.getUDTs = this.conn.prepareStatement("select  null as TYPE_CAT, null as TYPE_SCHEM, null as TYPE_NAME,  null as CLASS_NAME,  null as DATA_TYPE, null as REMARKS, null as BASE_TYPE limit 0;");
        }
        this.getUDTs.clearParameters();
        return this.getUDTs.executeQuery();
    }

    @Override // java.sql.DatabaseMetaData
    public ResultSet getVersionColumns(String c, String s, String t) throws SQLException {
        if (this.getVersionColumns == null) {
            this.getVersionColumns = this.conn.prepareStatement("select null as SCOPE, null as COLUMN_NAME, null as DATA_TYPE, null as TYPE_NAME, null as COLUMN_SIZE, null as BUFFER_LENGTH, null as DECIMAL_DIGITS, null as PSEUDO_COLUMN limit 0;");
        }
        return this.getVersionColumns.executeQuery();
    }

    @Override // org.sqlite.core.CoreDatabaseMetaData
    public ResultSet getGeneratedKeys() throws SQLException {
        if (this.getGeneratedKeys == null) {
            this.getGeneratedKeys = this.conn.prepareStatement("select last_insert_rowid();");
        }
        return this.getGeneratedKeys.executeQuery();
    }

    public Struct createStruct(String t, Object[] attr) throws SQLException {
        throw new SQLException("Not yet implemented by SQLite JDBC driver");
    }

    @Override // java.sql.DatabaseMetaData
    public ResultSet getFunctionColumns(String a, String b, String c, String d) throws SQLException {
        throw new SQLException("Not yet implemented by SQLite JDBC driver");
    }

    /* access modifiers changed from: package-private */
    public class PrimaryKeyFinder {
        String[] pkColumns = null;
        String pkName = null;
        String table;

        public PrimaryKeyFinder(String table2) throws SQLException {
            this.table = table2;
            if (table2 == null || table2.trim().length() == 0) {
                throw new SQLException("Invalid table name: '" + this.table + "'");
            }
            Statement stat = null;
            ResultSet rs = null;
            try {
                stat = JDBC3DatabaseMetaData.this.conn.createStatement();
                ResultSet rs2 = stat.executeQuery("select sql from sqlite_master where lower(name) = lower('" + JDBC3DatabaseMetaData.this.escape(table2) + "') and type in ('table', 'view')");
                if (!rs2.next()) {
                    throw new SQLException("Table not found: '" + table2 + "'");
                }
                Matcher matcher = JDBC3DatabaseMetaData.PK_NAMED_PATTERN.matcher(rs2.getString(1));
                if (matcher.find()) {
                    this.pkName = JDBC3DatabaseMetaData.this.unquoteIdentifier(JDBC3DatabaseMetaData.this.escape(matcher.group(1)));
                    this.pkColumns = matcher.group(2).split(",");
                } else {
                    Matcher matcher2 = JDBC3DatabaseMetaData.PK_UNNAMED_PATTERN.matcher(rs2.getString(1));
                    if (matcher2.find()) {
                        this.pkColumns = matcher2.group(1).split(",");
                    }
                }
                if (this.pkColumns == null) {
                    rs2 = stat.executeQuery("pragma table_info('" + JDBC3DatabaseMetaData.this.escape(table2) + "');");
                    while (rs2.next()) {
                        if (rs2.getBoolean(6)) {
                            this.pkColumns = new String[]{rs2.getString(2)};
                        }
                    }
                }
                if (this.pkColumns != null) {
                    for (int i = 0; i < this.pkColumns.length; i++) {
                        this.pkColumns[i] = JDBC3DatabaseMetaData.this.unquoteIdentifier(this.pkColumns[i]);
                    }
                }
            } finally {
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (Exception e) {
                    }
                }
                if (stat != null) {
                    try {
                        stat.close();
                    } catch (Exception e2) {
                    }
                }
            }
        }

        public String getName() {
            return this.pkName;
        }

        public String[] getColumns() {
            return this.pkColumns;
        }
    }

    /* access modifiers changed from: package-private */
    public class ImportedKeyFinder {
        private final Pattern FK_NAMED_PATTERN = Pattern.compile("CONSTRAINT\\s*([A-Za-z_][A-Za-z\\d_]*)?\\s*FOREIGN\\s+KEY\\s*\\((.*?)\\)", 34);
        private List<ForeignKey> fkList = new ArrayList();
        private String fkTableName;

        public ImportedKeyFinder(String table) throws SQLException {
            if (table == null || table.trim().length() == 0) {
                throw new SQLException("Invalid table name: '" + table + "'");
            }
            this.fkTableName = table;
            List<String> fkNames = getForeignKeyNames(this.fkTableName);
            Statement stat = null;
            ResultSet rs = null;
            try {
                stat = JDBC3DatabaseMetaData.this.conn.createStatement();
                rs = stat.executeQuery("pragma foreign_key_list('" + JDBC3DatabaseMetaData.this.escape(this.fkTableName.toLowerCase()) + "')");
                int prevFkId = -1;
                int count = 0;
                ForeignKey fk = null;
                while (rs.next()) {
                    int fkId = rs.getInt(1);
                    rs.getInt(2);
                    String pkTableName = rs.getString(3);
                    String fkColName = rs.getString(4);
                    String pkColName = rs.getString(5);
                    String onUpdate = rs.getString(6);
                    String onDelete = rs.getString(7);
                    String match = rs.getString(8);
                    String fkName = fkNames.size() > count ? fkNames.get(count) : null;
                    if (fkId != prevFkId) {
                        fk = new ForeignKey(fkName, pkTableName, this.fkTableName, onUpdate, onDelete, match);
                        this.fkList.add(fk);
                        prevFkId = fkId;
                        count++;
                    }
                    fk.addColumnMapping(fkColName, pkColName);
                }
            } finally {
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (Exception e) {
                    }
                }
                if (stat != null) {
                    try {
                        stat.close();
                    } catch (Exception e2) {
                    }
                }
            }
        }

        private List<String> getForeignKeyNames(String tbl) throws SQLException {
            List<String> fkNames = new ArrayList<>();
            if (tbl != null) {
                Statement stat2 = null;
                ResultSet rs = null;
                try {
                    stat2 = JDBC3DatabaseMetaData.this.conn.createStatement();
                    rs = stat2.executeQuery("select sql from sqlite_master where lower(name) = lower('" + JDBC3DatabaseMetaData.this.escape(tbl) + "')");
                    if (rs.next()) {
                        Matcher matcher = this.FK_NAMED_PATTERN.matcher(rs.getString(1));
                        while (matcher.find()) {
                            fkNames.add(matcher.group(1));
                        }
                    }
                    Collections.reverse(fkNames);
                } finally {
                    if (rs != null) {
                        try {
                            rs.close();
                        } catch (SQLException e) {
                        }
                    }
                    if (stat2 != null) {
                        try {
                            stat2.close();
                        } catch (SQLException e2) {
                        }
                    }
                }
            }
            return fkNames;
        }

        public String getFkTableName() {
            return this.fkTableName;
        }

        public List<ForeignKey> getFkList() {
            return this.fkList;
        }

        /* access modifiers changed from: package-private */
        public class ForeignKey {
            private List<String> fkColNames = new ArrayList();
            private String fkName;
            private String fkTableName;
            private String match;
            private String onDelete;
            private String onUpdate;
            private List<String> pkColNames = new ArrayList();
            private String pkTableName;

            ForeignKey(String fkName2, String pkTableName2, String fkTableName2, String onUpdate2, String onDelete2, String match2) {
                this.fkName = fkName2;
                this.pkTableName = pkTableName2;
                this.fkTableName = fkTableName2;
                this.onUpdate = onUpdate2;
                this.onDelete = onDelete2;
                this.match = match2;
            }

            public String getFkName() {
                return this.fkName;
            }

            /* access modifiers changed from: package-private */
            public void addColumnMapping(String fkColName, String pkColName) {
                this.fkColNames.add(fkColName);
                this.pkColNames.add(pkColName);
            }

            public String[] getColumnMapping(int colSeq) {
                return new String[]{this.fkColNames.get(colSeq), this.pkColNames.get(colSeq)};
            }

            public int getColumnMappingCount() {
                return this.fkColNames.size();
            }

            public String getPkTableName() {
                return this.pkTableName;
            }

            public String getFkTableName() {
                return this.fkTableName;
            }

            public String getOnUpdate() {
                return this.onUpdate;
            }

            public String getOnDelete() {
                return this.onDelete;
            }

            public String getMatch() {
                return this.match;
            }

            public String toString() {
                return "ForeignKey [fkName=" + this.fkName + ", pkTableName=" + this.pkTableName + ", fkTableName=" + this.fkTableName + ", pkColNames=" + this.pkColNames + ", fkColNames=" + this.fkColNames + "]";
            }
        }
    }

    /* access modifiers changed from: protected */
    @Override // org.sqlite.core.CoreDatabaseMetaData, java.lang.Object
    public void finalize() throws Throwable {
        close();
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private String unquoteIdentifier(String name) {
        if (name == null) {
            return name;
        }
        String name2 = name.trim();
        if (name2.length() > 2 && ((name2.startsWith("`") && name2.endsWith("`")) || ((name2.startsWith("\"") && name2.endsWith("\"")) || (name2.startsWith("[") && name2.endsWith("]"))))) {
            name2 = name2.substring(1, name2.length() - 1);
        }
        return name2;
    }
}
