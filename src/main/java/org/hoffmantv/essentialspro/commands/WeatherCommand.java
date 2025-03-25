package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * WeatherCommand allows players to change the weather in their current world.
 * Usage: /weather <clear|rain|storm>
 */
public class WeatherCommand implements CommandExecutor {

    // Predefined messages using Adventure API with Unicode symbols
    private static final Component ONLY_PLAYERS_ERROR = Component.text("✖ This command can only be used by players.", NamedTextColor.RED);
    private static final Component NO_PERMISSION_ERROR = Component.text("✖ You don't have permission to use this command.", NamedTextColor.RED);
    private static final Component USAGE_ERROR = Component.text("✖ Usage: /weather <clear|rain|storm>", NamedTextColor.RED);
    private static final Component INVALID_ARG_ERROR = Component.text("✖ Invalid weather argument. Use: clear, rain, or storm.", NamedTextColor.RED);

    // Weather-specific success messages with icons
    private static final Component CLEAR_SUCCESS = Component.text("✔ Weather set to clear ☀", NamedTextColor.GREEN);
    private static final Component RAIN_SUCCESS = Component.text("✔ Weather set to rain ☔", NamedTextColor.GREEN);
    private static final Component STORM_SUCCESS = Component.text("✔ Weather set to storm ⛈", NamedTextColor.GREEN);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Ensure the sender is a player
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ONLY_PLAYERS_ERROR);
            return true;
        }

        // Check for permission
        if (!player.hasPermission("essentialspro.weather")) {
            player.sendMessage(NO_PERMISSION_ERROR);
            return true;
        }

        // Validate arguments
        if (args.length != 1) {
            player.sendMessage(USAGE_ERROR);
            return true;
        }

        // Process the weather argument (in lower case)
        String weatherArg = args[0].toLowerCase();
        boolean valid = applyWeather(player, weatherArg);

        if (!valid) {
            player.sendMessage(INVALID_ARG_ERROR);
        }

        return true;
    }

    /**
     * Attempts to apply the specified weather to the player's world.
     *
     * @param player     The player executing the command.
     * @param weatherArg The weather argument (clear, rain, storm).
     * @return true if the weather was applied successfully; false otherwise.
     */
    private boolean applyWeather(Player player, String weatherArg) {
        World world = player.getWorld();
        switch (weatherArg) {
            case "clear":
                world.setStorm(false);
                world.setThundering(false);
                player.sendMessage(CLEAR_SUCCESS);
                return true;
            case "rain":
                world.setStorm(true);
                world.setThundering(false);
                player.sendMessage(RAIN_SUCCESS);
                return true;
            case "storm":
                world.setStorm(true);
                world.setThundering(true);
                player.sendMessage(STORM_SUCCESS);
                return true;
            default:
                return false;
        }
    }
}