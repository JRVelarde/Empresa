package DAO;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConexionDB {
    private static Connection conn = null;

    public static String typeDB = null;

    private ConexionDB() {
        try {
            Properties prop = new Properties();
            prop.load(new FileReader("config_sqlite_mariadb.properties"));

            typeDB = prop.getProperty("db");
            String driver = prop.getProperty("driver");
            String dsn = prop.getProperty("dsn");
            String user = prop.getProperty("user", "");
            String pass = prop.getProperty("pass", "");

            Class.forName(driver);
            conn = DriverManager.getConnection(dsn, user, pass);
        } catch (ClassNotFoundException | SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Devuelve una conexión a la base de datos
     *
     * @return Conexión a la base de datos
     */
    public static Connection getConnection() {
        if (conn == null) {
            new ConexionDB();
        }
        return conn;
    }

    /**
     * Cierra la conexión
     */
    public static void close() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

