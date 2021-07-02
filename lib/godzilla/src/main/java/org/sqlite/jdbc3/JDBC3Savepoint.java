package org.sqlite.jdbc3;

import java.sql.SQLException;
import java.sql.Savepoint;

public class JDBC3Savepoint implements Savepoint {
    final int id;
    final String name;

    JDBC3Savepoint(int id2) {
        this.id = id2;
        this.name = null;
    }

    JDBC3Savepoint(int id2, String name2) {
        this.id = id2;
        this.name = name2;
    }

    @Override // java.sql.Savepoint
    public int getSavepointId() throws SQLException {
        return this.id;
    }

    @Override // java.sql.Savepoint
    public String getSavepointName() throws SQLException {
        if (this.name != null) {
            return this.name;
        }
        return String.format("SQLITE_SAVEPOINT_%s", Integer.valueOf(this.id));
    }
}
