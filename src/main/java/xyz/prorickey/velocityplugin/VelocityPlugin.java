package xyz.prorickey.velocityplugin;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;
import space.arim.libertybans.api.LibertyBans;
import space.arim.omnibus.Omnibus;
import space.arim.omnibus.OmnibusProvider;
import xyz.prorickey.velocityplugin.commands.GotoCMD;
import xyz.prorickey.velocityplugin.events.JoinEvent;

@Plugin(id = "velocityplugin",
        name = "VelocityPlugin",
        version = "1.0.0",
        description = "The core plugin for the proxy server",
        authors = {"Prorickey"})
public class VelocityPlugin {

    private static ProxyServer server;
    private static Logger logger;

    private static Database database;

    private static Omnibus lbomnibus = OmnibusProvider.getOmnibus();
    private static LibertyBans lbapi = OmnibusProvider.getOmnibus().getRegistry().getProvider(LibertyBans.class).orElseThrow();

    @Inject
    public VelocityPlugin(ProxyServer srv, Logger l) {
        server = srv;
        logger = l;

        database = new Database();

        logger.info("Velocity Core plugin loaded");
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        CommandManager cmdManager = server.getCommandManager();

        cmdManager.register(
                cmdManager.metaBuilder("goto").aliases("tpserver").plugin(this).build(),
                GotoCMD.createBrgadierCommand(server)
        );

        server.getEventManager().register(this, new JoinEvent());

        logger.info("Registered commands");
    }

    public static ProxyServer getProxyServer() { return server; }
    public static Omnibus getLbomnibus() { return lbomnibus; }
    public static LibertyBans getLbapi() { return lbapi; }
    public static Logger getLogger() { return logger; }
    public static Database getDatabase() { return database; }

}
