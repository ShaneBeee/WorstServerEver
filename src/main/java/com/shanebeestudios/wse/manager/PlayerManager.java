package com.shanebeestudios.wse.manager;

import com.shanebeestudios.wse.WSE;
import com.shanebeestudios.wse.data.PlayerData;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerManager {

    private final WSE PLUGIN;
    private final Map<UUID, PlayerData> PLAYER_DATAS = new HashMap<>();

    public PlayerManager(WSE plugin) {
        this.PLUGIN = plugin;
    }

    @NotNull
    public PlayerData getPlayerData(@NotNull Player player) {
        UUID uuid = player.getUniqueId();
        // If already online, grab their data
        if (PLAYER_DATAS.containsKey(uuid)) {
            return PLAYER_DATAS.get(uuid);
        }
        // If just joining, load their data
        else {
            PlayerData playerData = PLUGIN.getPlayerConfig().loadPlayerData(player);
            if (playerData != null) {
                PLAYER_DATAS.put(uuid, playerData);
                return playerData;
            }
        }
        // If no data is found, create new data
        PlayerData playerData = new PlayerData(player);
        PLAYER_DATAS.put(uuid, playerData);
        return playerData;
    }

    public void unloadPlayerData(@NotNull Player player) {
        UUID uuid = player.getUniqueId();
        if (PLAYER_DATAS.containsKey(uuid)) {
            PlayerData playerData = PLAYER_DATAS.get(uuid);
            PLUGIN.getPlayerConfig().savePlayerData(playerData);
            PLAYER_DATAS.remove(uuid);
        }
    }

    public Map<UUID, PlayerData> getPlayerDatas() {
        return new HashMap<>(PLAYER_DATAS);
    }
}
