package xyz.prorickey.velocityplugin;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import net.dv8tion.jda.api.JDA;
import org.slf4j.Logger;
import space.arim.libertybans.api.LibertyBans;
import space.arim.omnibus.Omnibus;
import space.arim.omnibus.OmnibusProvider;
import xyz.prorickey.velocityplugin.commands.GotoCMD;
import xyz.prorickey.velocityplugin.discord.ClassicDupeBot;
import xyz.prorickey.velocityplugin.discord.PunishStuff;
import xyz.prorickey.velocityplugin.events.JoinEvent;

import java.nio.file.Path;

@Plugin(id = "velocityplugin",
        name = "VelocityPlugin",
        version = "1.0.0",
        description = "The core plugin for the proxy server",
        authors = {"Prorickey"},
        dependencies = {
                @Dependency(id = "libertybans")
        })
public class VelocityPlugin {

    private static VelocityPlugin plugin;
    private static ProxyServer server;
    private static Logger logger;
    private static Path dataDirectory;
    private static ClassicDupeBot jda;

    private static Database database;

    private static Omnibus lbomnibus;
    private static LibertyBans lbapi;

    @Inject
    public VelocityPlugin(ProxyServer srv, Logger l, @DataDirectory Path dataDir) {
        server = srv;
        logger = l;
        plugin = this;
        dataDirectory = dataDir;

        logger.info("Velocity Core plugin loaded");
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        Config.init(server);

        lbomnibus = OmnibusProvider.getOmnibus();
        lbapi = OmnibusProvider.getOmnibus().getRegistry().getProvider(LibertyBans.class).orElseThrow();

        jda = new ClassicDupeBot();

        CommandManager cmdManager = server.getCommandManager();

        cmdManager.register(
                cmdManager.metaBuilder("goto").aliases("tpserver").plugin(this).build(),
                GotoCMD.createBrgadierCommand(server)
        );

        server.getEventManager().register(this, new JoinEvent());

        logger.info("Registered commands");
    }

    public static VelocityPlugin getPlugin() { return plugin; }
    public static ProxyServer getProxyServer() { return server; }
    public static Omnibus getLbomnibus() { return lbomnibus;  }
    public static LibertyBans getLbapi() { return lbapi;  }
    public static Logger getLogger() { return logger; }
    public static Database getDatabase() { return database; }

}
