package org.hoffmantv.essentialspro.commands;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.Random;

// Handles the '/firework' command, which launches a random firework.
public class FireworkCommand implements CommandExecutor {
    private final Random random = new Random();

    // When the command is executed
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Ensure the command is being used by a player
        if (!(sender instanceof Player)) {
            Bukkit.getConsoleSender().sendMessage("\u274C This command can only be used by players!");
            return true;
        }

        Player player = (Player) sender;

        // Check if the player has the right permission
        if (!player.hasPermission("essentialspro.firework")) {
            player.sendMessage("\u274C You do not have permission to use this command!");
            return true;
        }

        // Generate a firework at the player's location
        Firework firework = (Firework) player.getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK);
        FireworkMeta meta = firework.getFireworkMeta();

        // Randomly decide the type of the firework
        Type type = Type.values()[random.nextInt(Type.values().length)];

        // Randomly generate main and fade colors
        Color color = getRandomColor();
        Color fade = getRandomColor();

        // Construct a firework effect with the randomly generated parameters
        FireworkEffect effect = FireworkEffect.builder()
                .flicker(random.nextBoolean())
                .trail(random.nextBoolean())
                .with(type)
                .withColor(color)
                .withFade(fade)
                .build();

        // Set the firework's effect
        meta.addEffect(effect);

        // Randomly set the power of the firework, affecting its flight duration
        meta.setPower(random.nextInt(3) + 1);

        // Update the firework with the generated meta
        firework.setFireworkMeta(meta);

        player.sendMessage("\uD83C\uDF86 Firework launched!");

        return true;
    }

    // Helper method to generate a random color
    private Color getRandomColor() {
        return Color.fromBGR(random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }
}