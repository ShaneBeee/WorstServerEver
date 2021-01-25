package com.shanebeestudios.wse.listener;

import com.shanebeestudios.wse.data.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.BoundingBox;

public class SpawnListener implements Listener {

    private final World MAIN_WORLD = Bukkit.getWorlds().get(0);
    private final BoundingBox SPAWN_BOUND = new BoundingBox(1, 1, 1, 10, 10, 10); // TODO numbers

    public SpawnListener() {
    }

    // Protect breaking blocks in spawn
    @EventHandler
    private void onBreak(BlockBreakEvent event) {
        Location location = event.getBlock().getLocation();
        Player player = event.getPlayer();

        if (isInSpawn(location) && !Permissions.WSE_ADMIN_SPAWN.check(player)) {
            event.setCancelled(true);
        }
    }

    // Protect placing blocks in spawn
    @EventHandler
    private void onPlace(BlockPlaceEvent event) {
        Location location = event.getBlock().getLocation();
        Player player = event.getPlayer();

        if (isInSpawn(location) && !Permissions.WSE_ADMIN_SPAWN.check(player)) {
            event.setCancelled(true);
        }
    }

    // Prevent PvP/PvE in spawn
    @EventHandler
    private void onDamage(EntityDamageByEntityEvent event) {
        Location location = event.getEntity().getLocation();
        if (isInSpawn(location)) {
            event.setCancelled(true);
        }
    }

    private boolean isInSpawn(Location location) {
        if (location.getWorld() == MAIN_WORLD) {
            return SPAWN_BOUND.contains(location.toVector());
        }
        return false;
    }

}
