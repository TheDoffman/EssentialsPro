package me.thedoffman.essentialspro.commands;

import me.thedoffman.essentialspro.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Weather
implements CommandExecutor {
    private Main plugin = Main.getPlugin(Main.class);

    public Weather(Main plugin) {
        Bukkit.getPluginCommand("weather").setExecutor(this);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        plugin.prefix = plugin.prefix.replaceAll("&", "\u00A7");
        if (cmd.getName().equalsIgnoreCase("weather") && !sender.hasPermission("ep.weather")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use that command!");
            return true;
        }
        if (sender instanceof Player) {
            Player player = (Player)sender;
            if (args.length == 0) {
                player.sendMessage(plugin.prefix + ChatColor.RED + "usage: /weather [clear:rain]");
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("clear")) {
                    player.getWorld().setStorm(false);
                    player.sendMessage(plugin.prefix + ChatColor.GRAY + "Weather set to" + ChatColor.GREEN + " Clear" + ChatColor.GRAY + " in " + ChatColor.GREEN + player.getWorld().getName());
                } else if (args[0].equalsIgnoreCase("rain")) {
                    player.getWorld().setStorm(true);
                    player.sendMessage(plugin.prefix + ChatColor.GRAY + "Time set to" + ChatColor.GREEN + " Rain"+ ChatColor.GRAY + " in " + ChatColor.GREEN + player.getWorld().getName());
                } else {
                    player.sendMessage(plugin.prefix + ChatColor.RED + "usage: /weather [clear:rain]");
                    return true;
                }
            }
        }
        return true;
    }
}

