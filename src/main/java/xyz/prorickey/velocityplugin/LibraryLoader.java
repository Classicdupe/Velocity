package xyz.prorickey.velocityplugin;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class LibraryLoader {

    private static final List<String> libraries = List.of(
            "net.kyori:adventure-text-minimessage:4.13.1",
            "net.dv8tion:JDA:5.0.0-beta.5",
            "org.yaml:snakeyaml:2.0",
            "org.mariadb.jdbc:mariadb-java-client:3.1.4"
    );

    public static void loadLibraries(Path path) {
        libraries.forEach(lib -> {
            String[] libData = lib.split(":");
            String groupId = libData[0];
            String artifactId = libData[1];
            String version = libData[2];
            String fileUrl = "https://repo1.maven.org/maven2/" + groupId.replace(".", "/") + "/" + artifactId + "/" + version + "/" + artifactId + "-" + version + ".jar";
            Path savePath = Paths.get(path.toString(), "/libs/", artifactId + "-" + version + ".jar");

            try {
                downloadFile(fileUrl, savePath);
                //addToClasspath(savePath);
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

    private static void addToClasspath(Path libraryPath) throws IOException, InvocationTargetException, IllegalAccessException {
        JarFile jarFile = new JarFile(libraryPath.toString());
        JarEntry jarEntry = new JarEntry(libraryPath.toString());
        ClassLoader classLoader = VelocityPlugin.class.getClassLoader();
        Method method = null;

        VelocityPlugin.getProxyServer().getPluginManager().addToClasspath(VelocityPlugin.getPlugin(), libraryPath);

        try {
            method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            method.setAccessible(true);
            //method.invoke(classLoader, url);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        URL url = new URL("jar:file:" + libraryPath + "!/");
        method.invoke(classLoader, url);
        jarFile.close();
        System.out.println("Library added to classpath: " + libraryPath);
    }

}
