package com.shanebeestudios.wse.command;

import com.shanebeestudios.wse.WSE;
import com.shanebeestudios.wse.data.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CommandHandler implements TabExecutor {

    private final WSE PLUGIN;
    private final Map<String, BaseCmd> COMMANDS = new HashMap<>();
    private final PluginManager PM;

    @SuppressWarnings("ConstantConditions")
    public CommandHandler(WSE plugin) {
        this.PLUGIN = plugin;
        PM = Bukkit.getPluginManager();
        plugin.getCommand("wse").setExecutor(this);
        registerCommands();
    }

    private void registerCommands() {
        registerCommand(SpawnCmd.class, PermissionDefault.TRUE);
    }

    private void registerCommand(@NotNull Class<? extends BaseCmd> commandClass, PermissionDefault permDefault) {
        try {
            Constructor<? extends BaseCmd> constructor = commandClass.getDeclaredConstructor(WSE.class);
            BaseCmd cmd = constructor.newInstance(PLUGIN);
            String cmdName = cmd.command.toLowerCase(Locale.ROOT);
            COMMANDS.put(cmdName, cmd);
            String permName = "wse.command." + cmdName;
            Permission perm = new Permission(permName);
            perm.setDescription(String.format("Grants permission to use WorstServerEver command '%s'", cmd.command));
            perm.setDefault(permDefault);
            if (PM.getPermission(permName) == null) {
                PM.addPermission(perm);
            }
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (args.length > 0) {
            String commandString = args[0].toLowerCase(Locale.ROOT);
            if (COMMANDS.containsKey(commandString)) {
                if (hasPermission(sender, commandString)) {
                    String[] commandArgs = new String[args.length - 1];
                    System.arraycopy(args, 1, commandArgs, 0, args.length - 1);
                    BaseCmd command = COMMANDS.get(commandString);

                    // If console tries to use a player only command, we exit
                    if (!(sender instanceof Player) && command.requirePlayer) {
                        Message.CMD_PLAYER_ONLY.send(sender, commandString);
                        return true;
                    }
                    // Execute command and look for success
                    if (!command.execute(sender, commandArgs)) {
                        Message.CMD_USAGE.send(sender, command.command, command.getUsage());
                    }
                } else {
                    Message.CMD_NO_PERM.send(sender, commandString);
                }
            } else {
                Message.CMD_UNKNOWN.send(sender, commandString);
            }
        } else {
            Message.CMD_USAGE_BASE.send(sender);
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
        if (args.length == 1) {
            List<String> commands = new ArrayList<>();
            COMMANDS.values().forEach(command -> {
                if (hasPermission(sender, command.command)) {
                    if (!(sender instanceof Player) && command.requirePlayer) {
                        return;
                    }
                    commands.add(command.command);
                }
            });
            return StringUtil.copyPartialMatches(args[0], commands, new ArrayList<>());
        } else if (args.length > 1) {
            String commandString = args[0].toLowerCase(Locale.ROOT);
            if (COMMANDS.containsKey(commandString)) {
                String[] commandArgs = new String[args.length - 1];
                System.arraycopy(args, 1, commandArgs, 0, args.length - 1);
                BaseCmd command = COMMANDS.get(commandString);
                if (!(sender instanceof Player) && command.requirePlayer) {
                    return Collections.emptyList();
                }
                return command.tab(sender, commandArgs);
            }
        }
        return null;
    }

    private boolean hasPermission(CommandSender sender, String command) {
        return sender.hasPermission("wse.command." + command);
    }

}
