package xyz.prorickey.velocityplugin.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import xyz.prorickey.velocityplugin.Config;
import xyz.prorickey.velocityplugin.VelocityPlugin;

import java.util.TimerTask;

public class ClassicDupeBot {

    public static JDA jda;

    public ClassicDupeBot() {
        jda = JDABuilder
                .createDefault(Config.getConfig().getDiscordData().getToken())
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .build();

        PunishStuff.registerListener();

        new java.util.Timer().schedule(new ServerStatsUpdater(), 0, 5000);
    }

    public static JDA getJDA() { return jda; }

    public class ServerStatsUpdater extends TimerTask {
        @Override
        public void run() {
            jda.getVoiceChannelById(Config.getConfig().getDiscordData().getOnlinePlayers())
                    .getManager()
                    .setName("\uD83D\uDCC8\u30FBPlayers Online: " + VelocityPlugin.getProxyServer().getPlayerCount())
                    .queue();
        }
    }

}
