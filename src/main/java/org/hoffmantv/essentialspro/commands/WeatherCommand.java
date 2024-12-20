package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hoffmantv.essentialspro.EssentialsPro;

public class WeatherCommand implements CommandExecutor {

    private final EssentialsPro plugin;

    public WeatherCommand(EssentialsPro plugin) {
        this.plugin = plugin;
    }

    private static final Component MSG_ONLY_PLAYERS = Component.text("This command can only be used by players.", NamedTextColor.RED);
    private static final Component MSG_NO_PERMISSION = Component.text("You don't have permission to use this command.", NamedTextColor.RED);
    private static final Component MSG_USAGE = Component.text("Usage: /weather <clear|rain|storm>", NamedTextColor.RED);
    private static final Component MSG_INVALID_ARG = Component.text("Invalid weather argument. Use: clear, rain, or storm.", NamedTextColor.RED);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MSG_ONLY_PLAYERS);
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("essentialspro.weather")) {
            player.sendMessage(MSG_NO_PERMISSION);
            return true;
        }

        if (args.length != 1) {
            player.sendMessage(MSG_USAGE);
            return true;
        }

        String weatherArg = args[0].toLowerCase();
        boolean success = applyWeather(player, weatherArg);

        if (!success) {
            player.sendMessage(MSG_INVALID_ARG);
        }

        return true;
    }

    /**
     * Attempts to apply the desired weather to the player's world.
     * Returns true if successful, false if the weatherArg is invalid.
     */
    private boolean applyWeather(Player player, String weatherArg) {
        World world = player.getWorld();
        boolean isThundering;

        switch (weatherArg) {
            case "clear":
                world.setStorm(false);
                world.setThundering(false);
                break;
            case "rain":
                world.setStorm(true);
                world.setThundering(false);
                break;
            case "storm":
                world.setStorm(true);
                world.setThundering(true);
                break;
            default:
                return false;
        }

        player.sendMessage(Component.text("Weather set to " + weatherArg + ".", NamedTextColor.GREEN));
        return true;
    }
}