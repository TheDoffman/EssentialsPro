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
                    p.sendMessage(plugin.prefix + ChatColor.RED + "Please specify a player and or players.");
                    return true;
                } else if (!(sender instanceof Player)) {
                    sender.sendMessage(plugin.prefix + ChatColor.RED + "The console cannot use /teleport");
                    return true;
                }
                
				Player target = Bukkit.getServer().getPlayer(args[0]);
                
				Player target2 = Bukkit.getServer().getPlayer(args[1]);
                
                if (target == null) {
                    p.sendMessage(plugin.prefix + ChatColor.RED + "Could not find player " + args[0] + "!");
                    return true;
                }
                else if (target2 == null) {
                	
                    p.teleport(target.getLocation());
                    p.sendMessage(ChatColor.GRAY + "You have been teleported to " + ChatColor.GREEN + target.getName() + ChatColor.GRAY + "." );
                    return true;
                }
                
                target.teleport(target2.getLocation());
                p.sendMessage(ChatColor.GRAY + "" + p.getName() + " teleported you to " + ChatColor.GREEN + target2.getName() + ChatColor.GRAY + "." );
                return true;
            }
        } 
        
        return true;
    }
}

