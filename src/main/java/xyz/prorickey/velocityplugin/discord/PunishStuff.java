package xyz.prorickey.velocityplugin.discord;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import org.shanerx.mojang.Mojang;
import space.arim.libertybans.api.*;
import space.arim.libertybans.api.event.PunishEvent;
import space.arim.omnibus.events.EventConsumer;
import space.arim.omnibus.events.ListenerPriorities;
import xyz.prorickey.velocityplugin.Config;
import xyz.prorickey.velocityplugin.VelocityPlugin;

import java.time.Duration;
import java.time.Instant;

public class PunishStuff {

    public static void registerListener() {
        EventConsumer<PunishEvent> listener = evt -> {
            JDA jda = ClassicDupeBot.getJDA();
            EmbedBuilder eb = new EmbedBuilder();
            Mojang api = new Mojang().connect();

            String victim = "Unknown";
            if(evt.getDraftPunishment().getVictim() instanceof PlayerVictim v) victim = api.getPlayerProfile(v.getUUID().toString()).getUsername();
            else if(evt.getDraftPunishment().getVictim() instanceof AddressVictim v) victim = v.getAddress().toString();
            else if(evt.getDraftPunishment().getVictim() instanceof CompositeVictim v) victim = api.getPlayerProfile(v.getUUID().toString()).getUsername();

            String op = "Unknown";
            if(evt.getDraftPunishment().getOperator() instanceof ConsoleOperator) op = "Console";
            else if(evt.getDraftPunishment().getOperator() instanceof PlayerOperator p) op = api.getPlayerProfile(p.getUUID().toString()).getUsername();

            Duration dur = evt.getDraftPunishment().getDuration();
            eb.addField("Operator", op, true);
            eb.addField("Duration",
                    String.format("%d day(s), %02d hour(s), %02d minute(s), %02d second(s)",
                            dur.toDaysPart(),
                            dur.toHoursPart(),
                            dur.toMinutesPart(),
                            dur.toSecondsPart()),
                    true);
            eb.addField("Reason", evt.getDraftPunishment().getReason(), true);
            if(!(evt.getDraftPunishment().getVictim() instanceof AddressVictim)) eb.setThumbnail("https://cravatar.eu/helmavatar/" + victim + "/500");
            eb.setTimestamp(Instant.now());

            switch(evt.getDraftPunishment().getType()) {
                case BAN -> {
                    eb.setTitle(victim + " Banned");
                    eb.setColor(0xFF2020);
                }
                case KICK -> {
                    eb.setTitle(victim + " Kicked");
                    eb.setColor(0xFF9220);
                }
                case MUTE -> {
                    eb.setTitle(victim + " Muted");
                    eb.setColor(0xFFD420);
                }
                case WARN -> {
                    eb.setTitle(victim + " Warned");
                    eb.setColor(0xFFFB20);
                }
            }

            jda.getTextChannelById(Config.getConfig().getDiscordData().getPunishChannel()).sendMessageEmbeds(eb.build()).queue();
        };

        VelocityPlugin.getLbomnibus().getEventBus().registerListener(PunishEvent.class, ListenerPriorities.NORMAL, listener);
    }

}
