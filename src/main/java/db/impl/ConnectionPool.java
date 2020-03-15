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
    public final BlockingQueue<Connection> availableConnectoin = new LinkedBlockingDeque<>();
    public final BlockingQueue<Connection> takenConnectoin = new LinkedBlockingDeque<>();
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
                Connection$Proxy connection = new Connection$Proxy(DriverManager.getConnection(INSTANCE.getDbUrl(),
                        INSTANCE.getDbUser(), INSTANCE.getDbPassword()));
                availableConnectoin.add(connection);
            } catch (SQLException e) {
                System.out.println("Something went wrong");
            }
        }
    }

    @Override
    public Connection retrieveConnection() {
        Connection connection = null;
        if (availableConnectoin.size() != 0) {
            try {
                connection = availableConnectoin.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else if (availableConnectoin.size() + takenConnectoin.size() < INSTANCE.getMaxPoolSize()) {
            addConnections(INSTANCE.getPoolIncreaseStep());
        }
        if (Objects.nonNull(connection)) {
            availableConnectoin.remove(0);
            takenConnectoin.add(connection);
        }
        return connection;
    }

    @Override
    public boolean releaseConnection(Connection connection) {
        try {
            takenConnectoin.remove(connection);
            availableConnectoin.add(connection);
        } catch (Exception e) {
//            log.error("Sonthing went wrong with releasing connection", e);
            return false;
        }
        return true;
    }

    public void closeRealConnection(BlockingQueue<Connection> availableConnectoin){
        int count=0;
        Connection deletedConnection;
        while(availableConnectoin.size()>0){
            try {
                deletedConnection = (Connection) availableConnectoin.take();
                deletedConnection.close();
                count++;
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        if(count!=INSTANCE.getInitPoolSize()){
            System.out.println("We closed "+count+ " connections.");
        }else
            System.out.println("We closed all connections.");
    }
}
