package com.shanebeestudios.wse.util;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    private static final String PREFIX = "&7[&bW&3S&bE&7] &7";
    private static final String PREFIX_ERROR = "&7[&bW&3S&bE &cERROR&7] &c";
    private static final Pattern HEX_PATTERN = Pattern.compile("<#([A-Fa-f0-9]){6}>");

    public static String getColString(String string) {
        Matcher matcher = HEX_PATTERN.matcher(string);
        while (matcher.find()) {
            final ChatColor hexColor = ChatColor.of(matcher.group().substring(1, matcher.group().length() - 1));
            final String before = string.substring(0, matcher.start());
            final String after = string.substring(matcher.end());
            string = before + hexColor + after;
            matcher = HEX_PATTERN.matcher(string);
        }
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static void log(String format, Object... objects) {
        Bukkit.getConsoleSender().sendMessage(getColString(PREFIX + String.format(format, objects)));
    }

    public static void error(String format, Object... objects) {
        Bukkit.getConsoleSender().sendMessage(getColString(PREFIX_ERROR + String.format(format, objects)));
    }

}
