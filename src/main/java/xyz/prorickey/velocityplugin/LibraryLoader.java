package xyz.prorickey.velocityplugin;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class LibraryLoader {

    private static final List<String> libraries = List.of(
            "https://repo1.maven.org/maven2/^net.kyori:adventure-text-minimessage:4.13.1",
            "https://repo1.maven.org/maven2/^net.dv8tion:JDA:5.0.0-beta.5",
            "https://repo1.maven.org/maven2/^org.yaml:snakeyaml:2.0",
            "https://repo1.maven.org/maven2/^org.mariadb.jdbc:mariadb-java-client:3.1.4",
            "https://jitpack.io/^com.github.SparklingComet:java-mojang-api:-SNAPSHOT"
    );

    public static void loadLibraries(Path path) {
        libraries.forEach(lib -> {
            String[] fullData = lib.split("\\^");
            String[] libData = fullData[1].split(":");
            String groupId = libData[0];
            String artifactId = libData[1];
            String version = libData[2];
            String fileUrl = fullData[0] + groupId.replace(".", "/") + "/" + artifactId + "/" + version + "/" + artifactId + "-" + version + ".jar";
            Path savePath = Paths.get(path.toString(), "/libs/", artifactId + "-" + version + ".jar");

            try {
                if(!Files.exists(savePath)) downloadFile(fileUrl, savePath);
                VelocityPlugin.getProxyServer().getPluginManager().addToClasspath(VelocityPlugin.getPlugin(), savePath);
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
    }

    private static void downloadFile(String fileUrl, Path savePath) throws IOException {
        URL url = new URL(fileUrl);
        Files.createDirectories(savePath.getParent());
        try (InputStream in = url.openStream()) {
            Files.copy(in, savePath);
        }
        System.out.println("Library downloaded: " + savePath);
    }

}
