package org.rest.db;

import org.rest.exception.DataBaseDriverException;
import org.rest.util.PropertiesUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//    private static final String DRIVER_CLASS_KEY = "org.postgresql.Driver";
//    private static final String URL_KEY = "jdbc:postgresql://localhost:5432/bankaccounts_db";
//    private static final String USER = "postgres";
//    private static final String PASSWORD = "";
//    private Connection connection;
//    private static ConnectionManager instance;
public final class ConnectionManagerImpl implements ConnectionManager {
    private static final String DRIVER_CLASS_KEY = "db.driver-class-name";
    private static String URL_KEY;
    private static String USERNAME_KEY;
    private static String PASSWORD_KEY;

    private static ConnectionManager instance;

    private ConnectionManagerImpl() {
    }

    public ConnectionManagerImpl(String url, String username, String password) {
        URL_KEY = url;
        USERNAME_KEY = username;
        PASSWORD_KEY = password;
    }

    public static synchronized ConnectionManager getInstance() {
        if (instance == null) {
            instance = new ConnectionManagerImpl();
            URL_KEY = PropertiesUtil.getProperties("db.url");
            USERNAME_KEY = PropertiesUtil.getProperties("db.username");
            PASSWORD_KEY = PropertiesUtil.getProperties("db.password");
            loadDriver(PropertiesUtil.getProperties(DRIVER_CLASS_KEY));
        }
        return instance;
    }

    private static void loadDriver(String driverClass) {
        try {
            Class.forName(driverClass);
        } catch (ClassNotFoundException e) {
            throw new DataBaseDriverException("Database driver not loaded.");
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL_KEY, USERNAME_KEY, PASSWORD_KEY);
    }
}
