package com.shanebeestudios.wse.data;

import com.shanebeestudios.wse.util.After;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Locations {

    public static Locations SPAWN = get(-15.5, 69, 25.5);

    private static Locations get(double x, double y, double z) {
        Location location = new Location(Bukkit.getWorlds().get(0), x, y, z);
        return new Locations(location);
    }

    private static Locations get(double x, double y, double z, String world) {
        World w = Bukkit.getWorld(world);
        if (w == null) {
            throw new IllegalArgumentException(String.format("Invalid world: '%s'", world));
        }
        Location location = new Location(w, x, y, z);
        return new Locations(location);
    }

    private final Location location;

    public Locations(Location location) {
        this.location = location;
    }

    public void teleport(@NotNull Player player) {
        teleport(player, null);
    }

    public void teleport(@NotNull Player player, @Nullable After<Player> after) {
        player.teleportAsync(location).thenApply(fn -> {
            if (after != null) {
                after.run(player);
            }
            return null;
        });
    }

}
