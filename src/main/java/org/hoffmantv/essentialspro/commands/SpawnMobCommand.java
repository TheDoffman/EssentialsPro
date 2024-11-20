package org.hoffmantv.essentialspro.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
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

        // Permission check
        if (!player.hasPermission("essentialspro.spawnmob")) {
            player.sendMessage(ChatColor.RED + "✖ You don't have permission to use this command.");
            return true;
        }

        // Validate arguments
        if (args.length < 1) {
            player.sendMessage(ChatColor.RED + "✖ Usage: /spawnmob <mob_type> [amount]");
            return true;
        }

        String mobTypeName = args[0].toUpperCase();
        EntityType entityType;

        try {
            entityType = EntityType.valueOf(mobTypeName);
        } catch (IllegalArgumentException e) {
            player.sendMessage(ChatColor.RED + "✖ Invalid mob type: " + mobTypeName);
            return true;
        }

        // Determine the amount of mobs to spawn
        int amount = 1;
        if (args.length >= 2) {
            try {
                amount = Integer.parseInt(args[1]);
                if (amount <= 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                player.sendMessage(ChatColor.RED + "✖ Invalid amount. Usage: /spawnmob <mob_type> [amount]");
                return true;
            }
        }

        // Spawn the mobs
        Location location = player.getLocation();
        World world = player.getWorld();

        for (int i = 0; i < amount; i++) {
            Entity entity = world.spawnEntity(location, entityType);

            // Add particle effect at spawn location
            world.spawnParticle(Particle.FLAME, location, 50, 0.5, 0.5, 0.5, 0.05);
            world.spawnParticle(Particle.SMOKE_NORMAL, location, 30, 0.5, 0.5, 0.5, 0.05);
        }

        // Confirmation message
        player.sendMessage(ChatColor.GREEN + "✔ Spawned " + amount + " " + mobTypeName.toLowerCase() + "(s) at your location.");
        return true;
    }
}