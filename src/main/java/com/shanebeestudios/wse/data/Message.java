package com.shanebeestudios.wse.data;

import com.shanebeestudios.wse.util.Utils;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;

public class Message {

    // Command messages
    public static final Message CMD_USAGE = get("&6Command usage: &3&l/wse &b%s &7%s");
    public static final Message CMD_NO_PERM = get("You do not have permission to use command '%s'");
    public static final Message CMD_UNKNOWN = get("Unknown command '%s'");
    public static final Message CMD_USAGE_BASE = get("&6Command usage: &3&l/wse &r<&bcommand&r>");
    public static final Message CMD_PLAYER_ONLY = get("Command '%s' can only be run by a player");

    // System messages
    public static final Message PLUGIN_LOADED = get("Finished in %.2f seconds.");

    // Player messages
    public static final Message TP_PLAYER_NOT_FOUND = get("&cPlayer &7'&b%s&7'&c not found");
    public static final Message TP_TO_SPAWN_FROM = get("You are being teleported to spawn by %s");
    public static final Message TP_TO_SPAWN_OTHER = get("You have teleported %s to spawn");
    public static final Message TP_TO_SPAWN_WAIT = get("Teleporting to spawn... hang tight!");
    public static final Message TP_TO_SPAWN_SUCCESS = get("Welcome back to spawn!");

    // Combat
    public static final Message COMBAT_TIMER_START = get("You are now in combat mode for 15 seconds...");
    public static final Message COMBAT_TIMER_STOP = get("Your combat timer has stopped!");

    private static Message get(String message) {
        return new Message(message);
    }

    private final String message;

    public Message(String message) {
        this.message = message;
    }

    /**
     * Send this message
     *
     * @param receiver Who to receive
     * @param params   Params this message may have
     */
    public void send(@Nullable CommandSender receiver, Object... params) {
        Utils.sendMessage(receiver, message, params);
    }

    /**
     * Log this message to console
     *
     * @param params Params this message may have
     */
    public void log(Object... params) {
        Utils.log(message, params);
    }

}
