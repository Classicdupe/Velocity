package xyz.prorickey.velocityplugin.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import xyz.prorickey.velocityplugin.Config;

public class ClassicDupeBot {

    public static JDA jda;

    public ClassicDupeBot() {
        jda = JDABuilder
                .createDefault(Config.getConfig().getDiscordData().getToken())
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .build();

        PunishStuff.registerListener();
    }

    public static JDA getJDA() { return jda; }

    public class ServerStatsUpdater extends BukkitRunnable {
        @Override
        public void run() {
            jda.getVoiceChannelById(Config.getConfig().getLong("discord.onlineplayers"))
                    .getManager()
                    .setName("\uD83D\uDCC8\u30FBPlayers Online: " + plugin.getServer().getOnlinePlayers().size())
                    .queue();
        }
    }

}
