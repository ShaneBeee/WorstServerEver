package com.shanebeestudios.wse.listener;

import com.shanebeestudios.wse.WSE;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

public class ListenerManager {

    private final WSE PLUGIN;
    private final PluginManager PLUGIN_MANAGER;
    private final PlayerDataListener playerDataListener;

    public ListenerManager(WSE plugin) {
        this.PLUGIN = plugin;
        this.PLUGIN_MANAGER = Bukkit.getPluginManager();

        registerListener(new SpawnListener());
        playerDataListener = new PlayerDataListener(plugin);
        registerListener(playerDataListener);
    }

    private void registerListener(Listener listener) {
        PLUGIN_MANAGER.registerEvents(listener, PLUGIN);
    }

    public void cancelTasks() {
        playerDataListener.stopAllTimers();
    }

}
