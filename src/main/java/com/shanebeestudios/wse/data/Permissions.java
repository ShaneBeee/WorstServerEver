package com.shanebeestudios.wse.data;

import org.bukkit.entity.Player;

public class Permissions {

    public static final Permissions WSE_ADMIN_SPAWN = get("wse.admin.spawn");
    public static final Permissions WSE_SPAWN_DELAY_BYPASS = get("wse.spawn.delay.bypass");
    public static final Permissions WSE_SPAWN_OTHER = get("wse.spawn.other");

    private static Permissions get(String permission) {
        return new Permissions(permission);
    }

    private final String permission;

    private Permissions(String permission) {
        this.permission = permission;
    }

    public boolean check(Player player) {
        return player.hasPermission(permission);
    }

}
