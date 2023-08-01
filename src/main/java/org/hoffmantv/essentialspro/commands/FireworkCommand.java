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

public class FireworkCommand implements CommandExecutor {

    private Random random = new Random();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            Bukkit.getConsoleSender().sendMessage("This command can only be used by players!");
            return true;
        }

        Player player = (Player) sender;

        // Check for the appropriate permission
        if (!player.hasPermission("essentialspro.firework")) {
            player.sendMessage("You do not have permission to use this command!");
            return true;
        }

        // Create a firework at the player's location
        Firework firework = (Firework) player.getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK);
        FireworkMeta meta = firework.getFireworkMeta();

        // Get a random firework type
        Type[] types = Type.values();
        Type type = types[random.nextInt(types.length)];

        // Generate random colors for the firework
        Color color = getRandomColor();
        Color fade = getRandomColor();

        // Define the firework effect
        FireworkEffect effect = FireworkEffect.builder()
                .flicker(random.nextBoolean())
                .trail(random.nextBoolean())
                .with(type)
                .withColor(color)
                .withFade(fade)
                .build();

        // Apply the firework effect to the firework
        meta.addEffect(effect);

        // Set the power level of the firework (this will affect how high it flies)
        meta.setPower(random.nextInt(3) + 1);

        // Apply the meta to the firework
        firework.setFireworkMeta(meta);

        return true;
    }

    // Method to get a random color
    private Color getRandomColor() {
        return Color.fromBGR(random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }
}
