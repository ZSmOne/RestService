package org.rest.db;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionManager {
    Connection getConnection() throws SQLException;
}
//    public static synchronized ConnectionManager getInstance() {
//        try {
//            if (instance == null)
//                instance = new ConnectiontManagerImpl();
//            //loadDriver(DRIVER_CLASS_KEY);
//            Class.forName(DRIVER_CLASS_KEY);
//
//        }catch (ClassNotFoundException e) {
//            throw new DataBaseDriverLoadException("Database driver not loaded.");
//        }
//        return instance;
//    }
//
//
//    public static synchronized ConnectionManager getInstance() {
//        if (instance == null) {
//            instance = new ConnectiontManagerImpl();
//            loadDriver(DRIVER_CLASS_KEY);
//        }
//        return instance;
//    }
//
//    private static void loadDriver(String driverClass) {
//        try {
//            Class.forName(driverClass);
//        } catch (ClassNotFoundException e) {
//            throw new DataBaseDriverLoadException("Database driver not loaded.");
//        }
//    }
