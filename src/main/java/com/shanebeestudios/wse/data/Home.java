package com.shanebeestudios.wse.data;

import com.shanebeestudios.wse.util.After;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Home {

    private String name;
    private Location location;

    public Home(String name, Location location) {
        this.name = name;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
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
