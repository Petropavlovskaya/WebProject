package start;

import db.impl.ConnectionPool;

import static db.impl.ConnectionPool.CONNECTION_POOL_INSTANCE;

public class MainRun {


    public static void main(String[] args) {
//        System.out.println(ApplicationConfiguration.INSTANCE.toString());
        System.out.println(CONNECTION_POOL_INSTANCE.availableConnectoin.size());
        System.out.println(CONNECTION_POOL_INSTANCE.takenConnectoin.size());

        CONNECTION_POOL_INSTANCE.closeRealConnection(CONNECTION_POOL_INSTANCE.availableConnectoin);
        System.out.println(CONNECTION_POOL_INSTANCE.availableConnectoin.size());
        System.out.println(CONNECTION_POOL_INSTANCE.takenConnectoin.size());
    }


}


