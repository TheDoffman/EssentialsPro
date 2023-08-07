package org.hoffmantv.essentialspro.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hoffmantv.essentialspro.EssentialsPro;

public class SetSpawnCommand implements CommandExecutor {

    private static final String PERMISSION_SETSPAWN = "essentialspro.setspawn";
    private static final String MSG_ONLY_PLAYERS = ChatColor.RED + "\u274C This command can only be used by players.";
    private static final String MSG_NO_PERMISSION = ChatColor.RED + "\u274C You don't have permission to use this command.";
    private static final String MSG_SPAWN_SET_SUCCESS = ChatColor.GREEN + "Spawn location set successfully!";

    private final EssentialsPro plugin;

    public SetSpawnCommand(EssentialsPro plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MSG_ONLY_PLAYERS);
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission(PERMISSION_SETSPAWN)) {
            player.sendMessage(MSG_NO_PERMISSION);
            return true;
        }

        try {
            plugin.setSpawnLocation(player.getLocation());
            player.sendMessage(MSG_SPAWN_SET_SUCCESS);
        } catch (Exception e) {
            player.sendMessage(ChatColor.RED + "\u274C An error occurred while setting spawn location: " + e.getMessage());
            return true;
        }

        return true;
    }
}
