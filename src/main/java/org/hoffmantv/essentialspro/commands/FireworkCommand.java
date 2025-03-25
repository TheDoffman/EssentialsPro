package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.Random;

public class FireworkCommand implements CommandExecutor {

    private static final String PERMISSION = "essentialspro.firework";
    private static final Component NO_PERMISSION = Component.text("✖ You do not have permission to use this command!", NamedTextColor.RED);
    private static final Component PLAYER_ONLY = Component.text("✖ This command can only be used by players!", NamedTextColor.RED);
    private static final Component USAGE = Component.text("Usage: /firework [help]", NamedTextColor.YELLOW);
    private static final Component LAUNCH_SUCCESS = Component.text("✔ Firework launched!", NamedTextColor.GOLD);

    private final Random random = new Random();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Ensure that only players can use this command
        if (!(sender instanceof Player)) {
            sender.sendMessage(PLAYER_ONLY);
            // Log to console as well
            Bukkit.getConsoleSender().sendMessage("✖ This command can only be used by players!");
            return true;
        }

        Player player = (Player) sender;

        // Check for permission
        if (!player.hasPermission(PERMISSION)) {
            player.sendMessage(NO_PERMISSION);
            return true;
        }

        // If the user requests help, display the usage information
        if (args.length > 0 && args[0].equalsIgnoreCase("help")) {
            player.sendMessage(USAGE);
            player.sendMessage(Component.text("This command launches a random firework at your location.", NamedTextColor.GREEN));
            return true;
        }

        // Launch a random firework
        launchRandomFirework(player);
        player.sendMessage(LAUNCH_SUCCESS);

        return true;
    }

    /**
     * Spawns a firework with a random effect at the player's location.
     *
     * @param player The player who executed the command.
     */
    private void launchRandomFirework(Player player) {
        Firework firework = (Firework) player.getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK);
        FireworkMeta meta = firework.getFireworkMeta();

        // Randomly choose a firework type
        Type type = Type.values()[random.nextInt(Type.values().length)];

        // Generate random colors for the firework effect
        Color mainColor = getRandomColor();
        Color fadeColor = getRandomColor();

        FireworkEffect effect = FireworkEffect.builder()
                .flicker(random.nextBoolean())
                .trail(random.nextBoolean())
                .with(type)
                .withColor(mainColor)
                .withFade(fadeColor)
                .build();

        meta.addEffect(effect);
        // Set power between 1 and 3 (affects flight duration)
        meta.setPower(random.nextInt(3) + 1);
        firework.setFireworkMeta(meta);
    }

    /**
     * Generates a random color using a random BGR value.
     *
     * @return A random {@link Color} object.
     */
    private Color getRandomColor() {
        return Color.fromBGR(random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }
}