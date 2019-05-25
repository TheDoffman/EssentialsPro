package me.thedoffman.essentialspro.commands;

import me.thedoffman.essentialspro.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Teleport
implements CommandExecutor {
    private Main plugin = Main.getPlugin(Main.class);

    public Teleport(Main plugin) {
        Bukkit.getPluginCommand("teleport").setExecutor(this);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        plugin.prefix = plugin.prefix.replaceAll("&", "\u00A7");

        
        if (cmd.getName().equalsIgnoreCase("teleport")) {
            if (!sender.hasPermission("ep.teleport")) {
                sender.sendMessage(ChatColor.RED + "You do not have permission to use that command!");
                return true;
            } 
            if (sender instanceof Player) {
                Player p = (Player)sender;
                if (args.length == 0) {
                    p.sendMessage(plugin.prefix + ChatColor.RED + "Please specify a player.");
                    return true;
                } else {
                	sender.sendMessage(ChatColor.RED + "Only players can use this command!");
                }
                
                @SuppressWarnings("deprecation")
				Player target = Bukkit.getServer().getPlayer(args[0]);
                if (target == null) {
                    p.sendMessage(plugin.prefix + ChatColor.RED + "Could not find player " + args[0] + "!");
                    return true;
                }
                p.teleport(target.getLocation());
                return true;
            }
            sender.sendMessage(plugin.prefix + ChatColor.RED + "The console cannot use /teleport");
            return true;
        } 
        
        return true;
    }
}

