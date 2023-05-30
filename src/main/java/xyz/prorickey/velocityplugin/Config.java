package xyz.prorickey.velocityplugin;

import com.velocitypowered.api.proxy.ProxyServer;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.Map;

public class Config {

    private static File file;
    private static ConfigData config;

    public static ConfigData getConfig() { return config; }

    public static void init(ProxyServer proxy) {
        LoaderOptions loaderOptions = new LoaderOptions();
        loaderOptions.setAllowDuplicateKeys(false);
        Yaml yaml = new Yaml();
        try {
            InputStream inputStream = VelocityPlugin.class.getClassLoader().getResourceAsStream("config.yml");
            if (inputStream != null) {
                Map<String, Map<String, Object>> tempConfig = yaml.load(inputStream);
                config = new ConfigData(
                        new DiscordData(
                                (String) tempConfig.get("discord").get("token"),
                                (long) tempConfig.get("discord").get("punishChannel"),
                                (long) tempConfig.get("discord").get("onlinePlayers")
                        ),
                        new DatabaseData(
                                (String) tempConfig.get("database").get("host"),
                                tempConfig.get("database").get("port").toString(),
                                (String) tempConfig.get("database").get("database"),
                                (String) tempConfig.get("database").get("username"),
                                (String) tempConfig.get("database").get("password")
                        )
                );
                inputStream.close();
            } else {
                throw new FileNotFoundException("config.yml file not found");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static class ConfigData {

        public ConfigData(DiscordData discordData, DatabaseData databaseData) {
            this.discord = discordData;
            this.database = databaseData;
        }

        private Config.DiscordData discord;
        public Config.DiscordData getDiscordData() { return discord; }
        public void setDiscordData(Config.DiscordData discord) { this.discord = discord; }

        private Config.DatabaseData database;
        public Config.DatabaseData getDatabaseData() { return database; }
        public void setDatabaseData(Config.DatabaseData database) { this.database = database; }
    }

    public static class DiscordData {

        public DiscordData(String token, long punishChannel, long onlinePlayers) {
            this.token = token;
            this.punishChannel = punishChannel;
            this.onlinePlayers = onlinePlayers;
        }

        private String token;
        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }

        private long punishChannel;
        public long getPunishChannel() { return punishChannel; }
        public void setPunishChannel(long punishChannel) { this.punishChannel = punishChannel; }

        private long onlinePlayers;
        public long getOnlinePlayers() { return onlinePlayers; }
        public void setOnlinePlayers(long onlinePlayers) { this.onlinePlayers = onlinePlayers; }
    }

    public static class DatabaseData {

        public DatabaseData(String host, String port, String database, String username, String password) {
            this.host = host;
            this.port = port;
            this.database = database;
            this.username = username;
            this.password = password;
        }

        private String host;
        public String getHost() { return host; }
        public void setHost(String host) { this.host = host; }

        private String port;
        public String getPort() { return port; }
        public void setPort(String port) { this.port = port; }

        private String database;
        public String getDatabase() { return database; }
        public void setDatabase(String database) { this.database = database; }

        private String username;
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        private String password;
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}
