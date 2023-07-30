package org.hoffmantv.essentialspro.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hoffmantv.essentialspro.EssentialsPro;

public class SpawnCommand implements CommandExecutor {

    private final EssentialsPro plugin;

    public SpawnCommand(EssentialsPro plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("essentialspro.spawn")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
            return true;
        }

        Location spawnLocation = plugin.getSpawnLocation();

        if (spawnLocation == null) {
            player.sendMessage(ChatColor.RED + "The spawn location is not set.");
            return true;
        }

        player.teleport(spawnLocation);
        player.sendMessage(ChatColor.GREEN + "Welcome back to the spawn!");

        return true;
    }
}
