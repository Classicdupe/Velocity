package xyz.prorickey.velocityplugin;

import com.velocitypowered.api.proxy.ProxyServer;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.Map;

public class Config {

    private static File file;
    private static Yaml yaml = new Yaml();
    private static Config config;

    public static Config getConfig() { return config; }

    public static void init(ProxyServer proxy) {
        try {
            file = new File(VelocityPlugin.class.getResource("/resources/config.yml").toURI());
            config = yaml.load(new FileInputStream(file));
        } catch (URISyntaxException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private DiscordData discord;
    public DiscordData getDiscordData() { return discord; }

    private DatabaseData database;
    public DatabaseData getDatabaseData() { return database; }

    public class DiscordData {
        private String token;
        private long punishChannel;
        private long onlinePlayers;

        public String getToken() { return this.token; }
        public long getPunishChanel() { return this.punishChannel; }
        public long getOnlinePlayers() { return this.onlinePlayers; }
    }

    public class DatabaseData {
        private String host;
        private String port;
        private String database;
        private String username;
        private String password;

        public String getHost() { return this.host; }
        public String getPort() { return this.port; }
        public String getDatabase() { return this.database; }
        public String getUsername() { return this.username; }
        public String getPassword() { return this.password; }
    }
}
