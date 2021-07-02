package org.sqlite;

public interface SQLiteUpdateListener {

    public enum Type {
        INSERT,
        DELETE,
        UPDATE
    }

    void onUpdate(Type type, String str, String str2, long j);
}
