package db;

import java.sql.Connection;

public interface IConnectionPool {
    Connection retrieveConnection();
    boolean releaseConnection(Connection connection);
}
