package org.hoffmantv.essentialspro.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class SpawnMobCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Check if the sender is a player
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "✖ Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        // Check for permissions
        if (!player.hasPermission("essentialspro.spawnmob")) {
            player.sendMessage(ChatColor.RED + "✖ You do not have permission to use this command.");
            return true;
        }

        // Check for correct usage
        if (args.length < 1 || args.length > 2) {
            player.sendMessage(ChatColor.RED + "Usage: /spawnmob <mob_type> [amount]");
            return true;
        }

        String mobTypeString = args[0].toUpperCase();
        int amount = 1;

        // Parse the amount (if provided)
        if (args.length == 2) {
            try {
                amount = Integer.parseInt(args[1]);
                if (amount < 1 || amount > 50) {
                    player.sendMessage(ChatColor.RED + "✖ Amount must be between 1 and 50.");
                    return true;
                }
            } catch (NumberFormatException e) {
                player.sendMessage(ChatColor.RED + "✖ Invalid number: " + args[1]);
                return true;
            }
        }

        // Check if the mob type is valid
        EntityType entityType;
        try {
            entityType = EntityType.valueOf(mobTypeString);
        } catch (IllegalArgumentException e) {
            player.sendMessage(ChatColor.RED + "✖ Invalid mob type: " + mobTypeString);
            return true;
        }

        // Ensure the entity type is spawnable
        if (!entityType.isSpawnable() || !entityType.isAlive()) {
            player.sendMessage(ChatColor.RED + "✖ You cannot spawn this type of entity.");
            return true;
        }

        // Spawn the mobs at the player's location
        Location location = player.getLocation();
        for (int i = 0; i < amount; i++) {
            player.getWorld().spawnEntity(location, entityType);
        }

        // Feedback to the player
        player.sendMessage(ChatColor.GREEN + "✔ Spawned " + amount + " " + mobTypeString + "(s) at your location.");
        Bukkit.getLogger().info(player.getName() + " spawned " + amount + " " + mobTypeString + "(s).");

        return true;
    }
}