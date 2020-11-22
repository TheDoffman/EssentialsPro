package me.hoffman.essentialspro.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.hoffman.essentialspro.main.Main;

public class Kick
implements CommandExecutor {
    private static Main plugin = Main.getPlugin(Main.class);

    public Kick(Main plugin) {
        Bukkit.getPluginCommand("kick").setExecutor(this);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        plugin.prefix = plugin.prefix.replaceAll("&", "\u00A7");
        if (label.equalsIgnoreCase("kick")) {
            if (!sender.hasPermission("ep.kick")) {
                sender.sendMessage(ChatColor.RED + "You do not have permission to use that command!");
                return true;
            }
            if (args.length == 0) {
                sender.sendMessage(plugin.prefix + ChatColor.RED + "Please specify a player and reason!");
            } else if (args.length == 1) {
                sender.sendMessage(plugin.prefix + ChatColor.RED + "Please specify a reason!");
            } else {
				Player target = Bukkit.getPlayer((String)args[0]);
                if (target == null) {
                    sender.sendMessage(plugin.prefix + ChatColor.RED + args[0] + " is not online!");
                    return true;
                }
                StringBuilder x = new StringBuilder();
                int i = 1;
                while (i < args.length) {
                    x.append(args[i] + " ");
                    ++i;
                }
                String kicker = "Server";
                if (sender instanceof Player) {
                    kicker = sender.getName();
                }
                target.kickPlayer(plugin.prefix + ChatColor.RED + "You have been kicked from the server!\nBy: " + kicker + "\nReason: " + x.toString().trim());
                sender.sendMessage(plugin.prefix + "Successfully kicked: " + ChatColor.GREEN + target.getName());
            }
        }
        return true;
    }
}

