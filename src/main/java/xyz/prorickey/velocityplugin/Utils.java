package xyz.prorickey.velocityplugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class Utils {

    public static Component format(String text) { return MiniMessage.miniMessage().deserialize(text); }
    public static Component cmdMsg(String text) { return format( "<dark_gray>[<green>i<dark_gray>] " + text); }

}
