package com.shanebeestudios.wse;

import com.shanebeestudios.wse.command.CommandHandler;
import com.shanebeestudios.wse.config.Config;
import com.shanebeestudios.wse.config.PlayerConfig;
import com.shanebeestudios.wse.data.Message;
import com.shanebeestudios.wse.listener.ListenerManager;
import com.shanebeestudios.wse.manager.PlayerManager;
import com.shanebeestudios.wse.util.Utils;
import org.bukkit.plugin.java.JavaPlugin;

public class WSE extends JavaPlugin {

    private static WSE PLUGIN;

    private Config config;
    private PlayerManager playerManager;
    private PlayerConfig playerConfig;
    private ListenerManager listenerManager;

    @Override
    public void onEnable() {
        // If we're already running let's not start another instance
        if (PLUGIN != null) {
            Utils.error("Cannot load another instance of WSE");
            return;
        }

        PLUGIN = this;
        long start = System.currentTimeMillis();
        Utils.log("Loading plugin...");

        // Managers
        this.playerManager = new PlayerManager(this);

        // Load configs
        this.config = new Config(this);
        this.playerConfig = new PlayerConfig(this);

        // Register event listeners
        this.listenerManager = new ListenerManager(this);

        // Register commands
        new CommandHandler(this);

        // All done
        // If we made it this far.... YAY!!!
        Message.PLUGIN_LOADED.log((float)(System.currentTimeMillis() - start) / 1000);
    }

    @Override
    public void onDisable() {
        this.listenerManager.cancelTasks();
        this.listenerManager = null;
        this.playerConfig.saveAll();
        this.playerManager = null;
        this.playerConfig = null;
        this.config = null;
        PLUGIN = null;
    }

    public Config getWSEConfig() {
        return this.config;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public PlayerConfig getPlayerConfig() {
        return playerConfig;
    }

    public static WSE getInstance() {
        return PLUGIN;
    }

}
