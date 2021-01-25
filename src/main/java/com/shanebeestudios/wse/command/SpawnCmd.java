package com.shanebeestudios.wse.command;

import com.shanebeestudios.wse.WSE;
import com.shanebeestudios.wse.data.Locations;
import com.shanebeestudios.wse.data.Message;
import com.shanebeestudios.wse.data.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class SpawnCmd extends BaseCmd {

    SpawnCmd(WSE plugin) {
        super(plugin);
        command = "spawn";
        usage = "[<player>]";
        requirePlayer = true;
    }

    @Override
    boolean execute(CommandSender sender, String[] args) {
        Player player = ((Player) sender);
        // TP another player to spawn
        if (args.length > 0) {
            String name = args[0];
            Player p = Bukkit.getPlayer(name);
            if (p != null) {
                Message.TP_TO_SPAWN_FROM.send(p, player.getName());
                Message.TP_TO_SPAWN_OTHER.send(player, p.getName());
                Locations.SPAWN.teleport(player, Message.TP_TO_SPAWN_SUCCESS::send);
            } else {
                Message.TP_PLAYER_NOT_FOUND.send(player, name);
                return false;
            }
        }
        // TP self to spawn
        else {
            int delay = Permissions.WSE_SPAWN_DELAY_BYPASS.check(player) ? 0 : (5 * 20);
            Message.TP_TO_SPAWN_WAIT.send(player);
            Bukkit.getScheduler().runTaskLater(PLUGIN, () ->
                    Locations.SPAWN.teleport(player, Message.TP_TO_SPAWN_SUCCESS::send), delay);
        }
        return true;
    }

    @Override
    List<String> tab(CommandSender sender, String[] args) {
        if (args.length == 1 && Permissions.WSE_SPAWN_OTHER.check((Player) sender)) {
            return null;
        }
        return Collections.emptyList();
    }

}
