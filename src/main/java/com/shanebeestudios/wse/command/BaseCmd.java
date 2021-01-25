package com.shanebeestudios.wse.command;

import com.shanebeestudios.wse.WSE;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public abstract class BaseCmd {

    final WSE PLUGIN;
    String command = null;
    String usage = "";
    boolean requirePlayer = false;

    BaseCmd(WSE plugin) {
        this.PLUGIN = plugin;
    }

    abstract boolean execute(CommandSender sender, String[] args);

    List<String> tab(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }

    public String getUsage() {
        return this.usage.replace("<", "&r<&b").replace(">", "&r>&7");
    }

}
