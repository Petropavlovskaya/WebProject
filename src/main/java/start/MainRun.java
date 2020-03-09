package start;

import configuration.ApplicationConfiguration;
import db.impl.ConnectionPool;

public class MainRun {


    public static void main(String[] args) {
//        System.out.println(ApplicationConfiguration.INSTANCE.toString());
        System.out.println(ConnectionPool.CONNECTION_POOL_INSTANCE.availableConnectoins.size());
        System.out.println(ConnectionPool.CONNECTION_POOL_INSTANCE.takenConnectoins.size());


    }


}
