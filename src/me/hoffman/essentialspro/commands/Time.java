package me.hoffman.essentialspro.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.hoffman.essentialspro.main.Main;

public class Time
implements CommandExecutor {
    private Main plugin = Main.getPlugin(Main.class);

    public Time(Main plugin) {
        Bukkit.getPluginCommand("time").setExecutor(this);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        plugin.prefix = plugin.prefix.replaceAll("&", "\u00A7");
        if (cmd.getName().equalsIgnoreCase("time") && !sender.hasPermission("ep.time")) {
            sender.sendMessage((Object)ChatColor.RED + "You do not have permission to use that command!");
            return true;
        }
        if (sender instanceof Player) {
            Player player = (Player)sender;
            if (args.length == 0) {
                player.sendMessage(plugin.prefix + ChatColor.RED + "usage: /time [day:night:noon]");
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("day")) {
                    player.getWorld().setTime(0L);
                    player.sendMessage(plugin.prefix + ChatColor.GREEN + "Time set to Day.");
                } else if (args[0].equalsIgnoreCase("night")) {
                    player.getWorld().setTime(14000L);
                    player.sendMessage(plugin.prefix + ChatColor.GREEN + "Time set to Night.");
                } else if (args[0].equalsIgnoreCase("noon")) {
                    player.getWorld().setTime(6000L);
                    player.sendMessage(plugin.prefix + ChatColor.GREEN + "Time set to Noon.");
                } else {
                    player.sendMessage(plugin.prefix + ChatColor.RED + "usage: /time [day:night]");
                    return true;
                }
            }
        }
        return true;
    }
}

