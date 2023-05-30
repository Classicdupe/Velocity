package xyz.prorickey.velocityplugin.discord;

import com.velocitypowered.api.scheduler.ScheduledTask;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import xyz.prorickey.velocityplugin.Config;
import xyz.prorickey.velocityplugin.VelocityPlugin;

import java.util.EnumSet;
import java.util.concurrent.TimeUnit;

public class ClassicDupeBot {

    public static JDA jda;
    public static ScheduledTask onlinePlayersTask;

    public ClassicDupeBot() {
        jda = JDABuilder
                .createDefault(Config.getConfig().getDiscordData().getToken())
                .enableIntents(EnumSet.allOf(GatewayIntent.class))
                .build();

        PunishStuff.registerListener();

        try {
            jda.awaitReady();
            onlinePlayersTask = VelocityPlugin.getProxyServer().getScheduler()
                    .buildTask(
                            VelocityPlugin.getPlugin(),
                            this::updateOnlinePlayers
                    )
                    .repeat(5, TimeUnit.MINUTES)
                    .schedule();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static JDA getJDA() { return jda; }



    private void updateOnlinePlayers() {
        jda.getVoiceChannelById(Config.getConfig().getDiscordData().getOnlinePlayers())
                .getManager()
                .setName("\uD83D\uDCC8\u30FBPlayers Online: " + VelocityPlugin.getProxyServer().getPlayerCount())
                .queue((sucess) -> VelocityPlugin.getLogger().info("Updated online players count"));
    }
}
