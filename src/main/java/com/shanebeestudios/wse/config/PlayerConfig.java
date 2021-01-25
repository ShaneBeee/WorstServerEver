package com.shanebeestudios.wse.config;

import com.shanebeestudios.wse.WSE;
import com.shanebeestudios.wse.data.Home;
import com.shanebeestudios.wse.data.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PlayerConfig {

    private final WSE PLUGIN;

    public PlayerConfig(WSE plugin) {
        this.PLUGIN = plugin;
        initDataFolder();
        startSaveTimer();
    }

    private void initDataFolder() {
        File file = new File(PLUGIN.getDataFolder(), "playerdata");
        if (!file.exists() || !file.isDirectory()) {
            file.mkdir();
        }
    }

    @Nullable
    public PlayerData loadPlayerData(Player player) {
        File file = new File(PLUGIN.getDataFolder(), "playerdata/" + player.getUniqueId().toString() + ".yml");
        if (file.exists()) {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            ConfigurationSection homes = config.getConfigurationSection("homes");
            Map<String, Home> homeMap = new HashMap<>();
            if (homes != null) {
                for (String key : homes.getKeys(false)) {
                    double x = homes.getDouble(key + ".x");
                    double y = homes.getDouble(key + ".y");
                    double z = homes.getDouble(key + ".z");
                    String w = homes.getString(key + ".w");
                    World world = Bukkit.getWorld(w);
                    if (world == null) {
                        continue;
                    }
                    Location location = new Location(world, x, y, z);
                    homeMap.put(key, new Home(key, location));
                }
            }

            return new PlayerData(player, homeMap);
        }
        return null;
    }

    public void saveAll() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            PLUGIN.getPlayerManager().unloadPlayerData(player);
        });
    }

    public void savePlayerData(PlayerData playerData) {
        File file = new File(PLUGIN.getDataFolder(), "playerdata/" + playerData.getUuid().toString() + ".yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set("last-logout", System.currentTimeMillis());
        playerData.getHomes().forEach((name, home) -> {
            Location location = home.getLocation();
            String path = "homes." + name + ".";
            config.set(path + "x", location.getX());
            config.set(path + "y", location.getY());
            config.set(path + "z", location.getZ());
            config.set(path + "w", location.getWorld().getName());
        });
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startSaveTimer() {
        // Start timer after 10 minutes
        // Run timer every 5 minutes to save all player datas
        Bukkit.getScheduler().runTaskTimerAsynchronously(PLUGIN,
                () -> PLUGIN.getPlayerManager().getPlayerDatas().forEach((uuid, playerData) ->
                        savePlayerData(playerData)), 10 * 60 * 20, 5 * 60 * 20);
    }

}
