package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

/**
 * SpawnMobCommand allows players to spawn a specified type of mob at their location.
 * Usage: /spawnmob <mob_type> [amount]
 *
 * <p>The command will check for proper permissions, validate input, and spawn the desired number
 * of mobs with some particle effects for visual flair.</p>
 */
public class SpawnMobCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Ensure the sender is a player
        if (!(sender instanceof Player)) {
            sender.sendMessage(Component.text("✖ Only players can use this command.", NamedTextColor.RED));
            return true;
        }
        Player player = (Player) sender;

        // Check if the player has the required permission
        if (!player.hasPermission("essentialspro.spawnmob")) {
            player.sendMessage(Component.text("✖ You don't have permission to use this command.", NamedTextColor.RED));
            return true;
        }

        // Validate the number of arguments
        if (args.length < 1) {
            player.sendMessage(Component.text("✖ Usage: /spawnmob <mob_type> [amount]", NamedTextColor.RED));
            return true;
        }

        // Parse the mob type argument (case-insensitive)
        String mobTypeName = args[0].toUpperCase();
        EntityType entityType;
        try {
            entityType = EntityType.valueOf(mobTypeName);
        } catch (IllegalArgumentException e) {
            player.sendMessage(Component.text("✖ Invalid mob type: " + mobTypeName, NamedTextColor.RED));
            return true;
        }

        // Determine the amount of mobs to spawn (default is 1)
        int amount = 1;
        if (args.length >= 2) {
            try {
                amount = Integer.parseInt(args[1]);
                if (amount <= 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                player.sendMessage(Component.text("✖ Invalid amount. Usage: /spawnmob <mob_type> [amount]", NamedTextColor.RED));
                return true;
            }
        }

        // Spawn the specified number of mobs at the player's location and display particle effects
        Location location = player.getLocation();
        World world = player.getWorld();
        for (int i = 0; i < amount; i++) {
            world.spawnEntity(location, entityType);
            // Display flame and smoke particles for visual flair
            world.spawnParticle(Particle.FLAME, location, 50, 0.5, 0.5, 0.5, 0.05);
            world.spawnParticle(Particle.SMOKE_NORMAL, location, 30, 0.5, 0.5, 0.5, 0.05);
        }

        // Send confirmation message with a Unicode checkmark
        Component successMessage = Component.text("✔ Spawned " + amount + " " + mobTypeName.toLowerCase() + "(s) at your location.", NamedTextColor.GREEN);
        player.sendMessage(successMessage);

        return true;
    }
}