package xyz.prorickey.velocityplugin;

import java.sql.*;

public class Database {

    private Connection conn;

    public Database() {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mariadb://" + Config.getConfig().getDatabaseData().getHost() +
                    ":" + Config.getConfig().getDatabaseData().getPort() +
                    "/" + Config.getConfig().getDatabaseData().getDatabase() +
                    "?user=" + Config.getConfig().getDatabaseData().getUsername() +
                    "&password=" + Config.getConfig().getDatabaseData().getPassword());
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
