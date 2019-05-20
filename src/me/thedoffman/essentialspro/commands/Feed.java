package me.thedoffman.essentialspro.commands;

import me.thedoffman.essentialspro.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Feed implements CommandExecutor {
    private Main plugin = Main.getPlugin(Main.class);

    public Feed(Main plugin) {
        Bukkit.getPluginCommand("feed").setExecutor((CommandExecutor)this);
    }

    @SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player target;
        Player p = (Player)sender;
        if (cmd.getName().equalsIgnoreCase("feed")) {
            if (!sender.hasPermission("ep.feed")) {
                sender.sendMessage(ChatColor.RED + "You do not have permission to use that command!");
                return true;
            }
            if (args.length == 0 && (sender instanceof Player)) {
                p.sendMessage(plugin.prefix + ChatColor.RED + "Error: Please enter a name!");
                return true;
            }
        }
        if (args.length == 0) {
            target = (Player)sender;
        } else {
            target = Bukkit.getServer().getPlayer(args[0]);
            if (target == null) {
                p.sendMessage(plugin.prefix + ChatColor.RED + "Error: Could not find player " + args[0] + "!");
                return true;
            }
        }
        target.setFoodLevel(20);
        p.sendMessage(plugin.prefix + ChatColor.GREEN + "You have been healed!");
        target.sendMessage(plugin.prefix + ChatColor.GREEN + "You have been healed by " + p.getName() + "!");
        return true;
    }
}

