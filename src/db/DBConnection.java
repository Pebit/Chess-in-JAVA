package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
//import db.Config;

public class DBConnection {
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(Config.url, Config.user, Config.password);
    }
}
