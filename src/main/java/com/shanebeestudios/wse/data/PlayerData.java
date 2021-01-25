package com.shanebeestudios.wse.data;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PlayerData {

    private final UUID uuid;
    private final Map<String, Home> HOMES;

    public PlayerData(Player player) {
        this.uuid = player.getUniqueId();
        this.HOMES = new HashMap<>();
    }

    public PlayerData(Player player, Map<String, Home> homes) {
        this.uuid = player.getUniqueId();
        this.HOMES = homes;
    }

    public UUID getUuid() {
        return uuid;
    }

    @Nullable
    public Home getHome(String home) {
        if (HOMES.containsKey(home)) {
            return HOMES.get(home);
        }
        return null;
    }

    public List<String> getAllHomeNames() {
        return new ArrayList<>(HOMES.keySet());
    }

    public Map<String, Home> getHomes() {
        return new HashMap<>(HOMES);
    }

}
