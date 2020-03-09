package db.impl;

import db.IConnectionPool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import static configuration.ApplicationConfiguration.INSTANCE;

public class ConnectionPool implements IConnectionPool {
    public static final ConnectionPool CONNECTION_POOL_INSTANCE = new ConnectionPool();
    public final BlockingQueue<Connection> availableConnectoins = new LinkedBlockingDeque<>();
    public final BlockingQueue<Connection> takenConnectoins = new LinkedBlockingDeque<>();
//    private final BlockingQueue<Connection$Proxy> availableConnectoins = new LinkedBlockingDeque<>();
//    private final BlockingQueue<Connection$Proxy> takenConnectoins = new LinkedBlockingDeque<>();

    private ConnectionPool() {
        initConnection();
    }

    private void initConnection() {
        addConnections(INSTANCE.getInitPoolSize());
    }

    private void addConnections(int size) {
        for (int i = 0; i < size; i++) {
            try {
                Connection connection = DriverManager.getConnection(INSTANCE.getDbUrl(), INSTANCE.getDbUser(),
                        INSTANCE.getDbPassword());
                availableConnectoins.add(connection);
            } catch (SQLException e) {
                System.out.println("Something went wrong");
            }
        }
    }

    @Override
    public Connection retrieveConnection() {
        Connection connection = null;
        if (availableConnectoins.size() != 0) {
            try {
                connection = availableConnectoins.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else if (availableConnectoins.size() + takenConnectoins.size() < INSTANCE.getMaxPoolSize()) {
            addConnections(INSTANCE.getPoolIncreaseStep());
        }
        if (Objects.nonNull(connection)) {
            availableConnectoins.remove(0);
            takenConnectoins.add(connection);
        }
        return connection;
    }

    @Override
    public boolean releaseConnection(Connection connection) {
        try {
            takenConnectoins.remove(connection);
            availableConnectoins.add(connection);
        } catch (Exception e) {
//            log.error("Sonthing went wrong with releasing connection", e);
            return false;
        }
        return true;
    }
}
