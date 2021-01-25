package com.shanebeestudios.wse.listener;

import com.shanebeestudios.wse.WSE;
import com.shanebeestudios.wse.data.Locations;
import com.shanebeestudios.wse.data.Message;
import com.shanebeestudios.wse.data.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataListener implements Listener {

    private final WSE PLUGIN;

    public PlayerDataListener(WSE plugin) {
        this.PLUGIN = plugin;
    }
    private final Map<UUID, Integer> COMBAT_TIMER = new HashMap<>();

    @EventHandler
    private void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = PLUGIN.getPlayerManager().getPlayerData(player);
        if (!player.hasPlayedBefore()) {
            Locations.SPAWN.teleport(player);
        }
    }

    @EventHandler
    private void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        // If the player logs out during combat, we kill em!
        if (COMBAT_TIMER.containsKey(uuid)) {
            player.damage(100); // 100 just to be safe
            COMBAT_TIMER.remove(uuid);
        }
        PLUGIN.getPlayerManager().unloadPlayerData(player);
    }

    @EventHandler
    private void onPVP(EntityDamageByEntityEvent event) {
        Entity attacker = event.getDamager();
        Entity victim = event.getEntity();
        if (attacker instanceof Player && victim instanceof Player) {
            startCombatTimer(((Player) attacker));
            startCombatTimer(((Player) victim));
        }
    }

    private void startCombatTimer(Player player) {
        BukkitScheduler scheduler = Bukkit.getScheduler();
        UUID uuid = player.getUniqueId();
        boolean inCombat = false;
        // If the player is already in combat, restart timer
        if (COMBAT_TIMER.containsKey(uuid)) {
            inCombat = true;
            int timer = COMBAT_TIMER.get(uuid);
            scheduler.cancelTask(timer);
        }
        if (inCombat) {
            Message.COMBAT_TIMER_START.send(player);
        }
        int id = scheduler.runTaskLater(PLUGIN, new Runnable() {
            @Override
            public void run() {
                // Double check they're still in there
                // If yes, remove them
                if (COMBAT_TIMER.containsKey(uuid)) {
                    int timer = COMBAT_TIMER.get(uuid);
                    scheduler.cancelTask(timer);
                    Message.COMBAT_TIMER_STOP.send(player);
                }
            }
        }, 15 * 20).getTaskId();
        COMBAT_TIMER.put(uuid, id);
    }

    public void stopAllTimers() {
        BukkitScheduler scheduler = Bukkit.getScheduler();
        COMBAT_TIMER.values().forEach(scheduler::cancelTask);
    }

}
