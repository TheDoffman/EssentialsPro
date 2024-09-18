package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Component.text("This command can only be used by players.", NamedTextColor.RED));
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("essentialspro.weather")) {
            player.sendMessage(Component.text("You don't have permission to use this command.", NamedTextColor.RED));
            return true;
        }

        if (args.length != 1) {
            player.sendMessage(Component.text("Usage: /weather <clear|rain|storm>", NamedTextColor.RED));
            return true;
        }

        String weatherArg = args[0].toLowerCase();
        boolean isThundering = false;

        switch (weatherArg) {
            case "clear":
                break;
            case "rain":
                isThundering = false;
                break;
            case "storm":
                isThundering = true;
                break;
            default:
                player.sendMessage(Component.text("Invalid weather argument. Use: clear, rain, or storm.", NamedTextColor.RED));
                return true;
        }

        player.getWorld().setStorm(!"clear".equals(weatherArg));
        player.getWorld().setThundering(isThundering);

        player.sendMessage(Component.text("Weather set to " + weatherArg + ".", NamedTextColor.GREEN));

        return true;
    }
}