package org.sqlite;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.sqlite.core.DB;

public class ExtendedCommand {

    public interface SQLExtension {
        void execute(DB db) throws SQLException;
    }

    public static SQLExtension parse(String sql) throws SQLException {
        if (sql == null) {
            return null;
        }
        if (sql.length() > 5 && sql.substring(0, 6).toLowerCase().equals("backup")) {
            return BackupCommand.parse(sql);
        }
        if (sql.length() <= 6 || !sql.substring(0, 7).toLowerCase().equals("restore")) {
            return null;
        }
        return RestoreCommand.parse(sql);
    }

    public static String removeQuotation(String s) {
        if (s == null) {
            return s;
        }
        if ((!s.startsWith("\"") || !s.endsWith("\"")) && (!s.startsWith("'") || !s.endsWith("'"))) {
            return s;
        }
        return s.substring(1, s.length() - 1);
    }

    public static class BackupCommand implements SQLExtension {
        private static Pattern backupCmd = Pattern.compile("backup(\\s+(\"[^\"]*\"|'[^']*'|\\S+))?\\s+to\\s+(\"[^\"]*\"|'[^']*'|\\S+)", 2);
        public final String destFile;
        public final String srcDB;

        public BackupCommand(String srcDB2, String destFile2) {
            this.srcDB = srcDB2;
            this.destFile = destFile2;
        }

        public static BackupCommand parse(String sql) throws SQLException {
            if (sql != null) {
                Matcher m = backupCmd.matcher(sql);
                if (m.matches()) {
                    String dbName = ExtendedCommand.removeQuotation(m.group(2));
                    String dest = ExtendedCommand.removeQuotation(m.group(3));
                    if (dbName == null || dbName.length() == 0) {
                        dbName = "main";
                    }
                    return new BackupCommand(dbName, dest);
                }
            }
            throw new SQLException("syntax error: " + sql);
        }

        @Override // org.sqlite.ExtendedCommand.SQLExtension
        public void execute(DB db) throws SQLException {
            db.backup(this.srcDB, this.destFile, null);
        }
    }

    public static class RestoreCommand implements SQLExtension {
        private static Pattern restoreCmd = Pattern.compile("restore(\\s+(\"[^\"]*\"|'[^']*'|\\S+))?\\s+from\\s+(\"[^\"]*\"|'[^']*'|\\S+)", 2);
        public final String srcFile;
        public final String targetDB;

        public RestoreCommand(String targetDB2, String srcFile2) {
            this.targetDB = targetDB2;
            this.srcFile = srcFile2;
        }

        public static RestoreCommand parse(String sql) throws SQLException {
            if (sql != null) {
                Matcher m = restoreCmd.matcher(sql);
                if (m.matches()) {
                    String dbName = ExtendedCommand.removeQuotation(m.group(2));
                    String dest = ExtendedCommand.removeQuotation(m.group(3));
                    if (dbName == null || dbName.length() == 0) {
                        dbName = "main";
                    }
                    return new RestoreCommand(dbName, dest);
                }
            }
            throw new SQLException("syntax error: " + sql);
        }

        @Override // org.sqlite.ExtendedCommand.SQLExtension
        public void execute(DB db) throws SQLException {
            db.restore(this.targetDB, this.srcFile, null);
        }
    }
}
