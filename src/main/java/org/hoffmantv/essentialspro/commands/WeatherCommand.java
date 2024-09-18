package org.hoffmantv.essentialspro.commands;

import org.bukkit.ChatColor;
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
            sender.sendMessage(ChatColor.RED + "\u274C This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("essentialspro.weather")) {
            player.sendMessage(ChatColor.RED + "\u274C You don't have permission to use this command.");
            return true;
        }

        if (args.length != 1) {
            player.sendMessage(ChatColor.RED + "Usage: /weather <clear|rain|storm>");
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
                player.sendMessage(ChatColor.RED + "\u274C Invalid weather argument. Use: clear, rain, or storm.");
                return true;
        }

        player.getWorld().setStorm(!"clear".equals(weatherArg));
        player.getWorld().setThundering(isThundering);

        player.sendMessage(ChatColor.GREEN + "Weather set to " + weatherArg + ".");

        return true;
    }
}
