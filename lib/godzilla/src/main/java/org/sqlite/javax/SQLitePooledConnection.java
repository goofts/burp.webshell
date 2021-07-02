package org.sqlite.javax;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.ConnectionEvent;
import javax.sql.ConnectionEventListener;
import org.sqlite.SQLiteConnection;
import org.sqlite.jdbc4.JDBC4PooledConnection;

public class SQLitePooledConnection extends JDBC4PooledConnection {
    protected volatile Connection handleConn;
    protected List<ConnectionEventListener> listeners = new ArrayList();
    protected SQLiteConnection physicalConn;

    protected SQLitePooledConnection(SQLiteConnection physicalConn2) {
        this.physicalConn = physicalConn2;
    }

    public SQLiteConnection getPhysicalConn() {
        return this.physicalConn;
    }

    @Override // javax.sql.PooledConnection
    public void close() throws SQLException {
        if (this.handleConn != null) {
            this.listeners.clear();
            this.handleConn.close();
        }
        if (this.physicalConn != null) {
            try {
                this.physicalConn.close();
            } finally {
                this.physicalConn = null;
            }
        }
    }

    @Override // javax.sql.PooledConnection
    public Connection getConnection() throws SQLException {
        if (this.handleConn != null) {
            this.handleConn.close();
        }
        this.handleConn = (Connection) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{Connection.class}, new InvocationHandler() {
            /* class org.sqlite.javax.SQLitePooledConnection.AnonymousClass1 */
            boolean isClosed;

            @Override // java.lang.reflect.InvocationHandler
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                try {
                    String name = method.getName();
                    if ("close".equals(name)) {
                        ConnectionEvent event = new ConnectionEvent(SQLitePooledConnection.this);
                        for (int i = SQLitePooledConnection.this.listeners.size() - 1; i >= 0; i--) {
                            SQLitePooledConnection.this.listeners.get(i).connectionClosed(event);
                        }
                        if (!SQLitePooledConnection.this.physicalConn.getAutoCommit()) {
                            SQLitePooledConnection.this.physicalConn.rollback();
                        }
                        SQLitePooledConnection.this.physicalConn.setAutoCommit(true);
                        this.isClosed = true;
                        return null;
                    } else if ("isClosed".equals(name)) {
                        if (!this.isClosed) {
                            this.isClosed = ((Boolean) method.invoke(SQLitePooledConnection.this.physicalConn, args)).booleanValue();
                        }
                        return Boolean.valueOf(this.isClosed);
                    } else if (!this.isClosed) {
                        return method.invoke(SQLitePooledConnection.this.physicalConn, args);
                    } else {
                        throw new SQLException("Connection is closed");
                    }
                } catch (SQLException e) {
                    if ("database connection closed".equals(e.getMessage())) {
                        ConnectionEvent event2 = new ConnectionEvent(SQLitePooledConnection.this, e);
                        for (int i2 = SQLitePooledConnection.this.listeners.size() - 1; i2 >= 0; i2--) {
                            SQLitePooledConnection.this.listeners.get(i2).connectionErrorOccurred(event2);
                        }
                    }
                    throw e;
                } catch (InvocationTargetException ex) {
                    throw ex.getCause();
                }
            }
        });
        return this.handleConn;
    }

    public void addConnectionEventListener(ConnectionEventListener listener) {
        this.listeners.add(listener);
    }

    public void removeConnectionEventListener(ConnectionEventListener listener) {
        this.listeners.remove(listener);
    }

    public List<ConnectionEventListener> getListeners() {
        return this.listeners;
    }
}
