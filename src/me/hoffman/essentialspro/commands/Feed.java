package me.hoffman.essentialspro.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.hoffman.essentialspro.main.Main;

public class Feed implements CommandExecutor {
    private Main plugin = Main.getPlugin(Main.class);

    public Feed(Main plugin) {
        Bukkit.getPluginCommand("feed").setExecutor((CommandExecutor)this);
    }

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player target;
        target = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("feed")) {
            if (!sender.hasPermission("ep.feed")) {
                sender.sendMessage(ChatColor.RED + "You do not have permission to use that command!");
                return true;
            }
            if (args.length == 0) {
                target.sendMessage(plugin.prefix + ChatColor.RED + "Error: Please enter a name!");
                return true;
            }
        }
        if (args.length == 0) {
        } else {
            target = Bukkit.getServer().getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(plugin.prefix + ChatColor.RED + "Error: Could not find player " + args[0] + "!");
                return true;
            }
        }
        target.setFoodLevel(20);
        target.sendMessage(plugin.prefix + "You're hunger has been repleneshed  by " + ChatColor.GREEN + target.getName() + ChatColor.WHITE + "!");
        return true;
    }
}

