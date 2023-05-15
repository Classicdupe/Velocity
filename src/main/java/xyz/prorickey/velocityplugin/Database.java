package xyz.prorickey.velocityplugin;

import com.velocitypowered.api.proxy.Player;
import org.jetbrains.annotations.Nullable;

import java.sql.*;
import java.util.UUID;

public class Database {

    private Connection conn;

    public Database() {
        try {
            conn = DriverManager.getConnection("jdbc:mariadb://" + Config.getConfig().getDatabaseData().getHost() +
                    ":" + Config.getConfig().getDatabaseData().getPort() +
                    "/" + Config.getConfig().getDatabaseData().getDatabase() +
                    "?user=" + Config.getConfig().getDatabaseData().getUsername() +
                    "&password=" + Config.getConfig().getDatabaseData().getPassword());

            conn.prepareStatement("CREATE TABLE IF NOT EXISTS players (uuid VARCHAR, name VARCHAR)").execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void dbLoginLogic(Player player) {
        try {
            PreparedStatement stat = conn.prepareStatement(
                    "INSERT INTO players (uuid, name) VALUES(?, ?) " +
                            "ON DUPLICATED KEY UPDATE name=?"
            );
            stat.setString(1, player.getUniqueId().toString());
            stat.setString(2, player.getUsername());
            stat.setString(3, player.getUsername());
            stat.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public @Nullable String getPlayerName(UUID uuid) {
        try {
            ResultSet set = conn.prepareStatement("SELECT name FROM players WHERE uuid='" + uuid + "'").executeQuery();
            if(set.next()) return set.getString("name");
            return null;
        } catch (SQLException e) {
            VelocityPlugin.getLogger().error(e.toString());
            return null;
        }
    }

}
