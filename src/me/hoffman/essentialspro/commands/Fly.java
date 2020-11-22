package me.hoffman.essentialspro.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.hoffman.essentialspro.main.Main;

public class Fly implements CommandExecutor {
    private Main plugin = Main.getPlugin(Main.class);

    public Fly(Main plugin) {
        Bukkit.getPluginCommand("fly").setExecutor((CommandExecutor)this);
    }

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        plugin.prefix = plugin.prefix.replaceAll("&", "\u00A7");
        if (cmd.getName().equalsIgnoreCase("fly"))
            if (!sender.hasPermission("ep.feed")) {
                sender.sendMessage(ChatColor.RED + "You do not have permission to use that command!");
                return true;
            }
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "The console cannot use /fly");
            return true;
    }
          Player p = (Player) sender;  
        {
            if(args.length == 0)
            {
                p.setAllowFlight(!p.getAllowFlight());
            }
            else if(args.length == 1)
            {
                if(args[0].equalsIgnoreCase("on"))
                {
                	p.setAllowFlight(true);
                }
                else if(args[0].equalsIgnoreCase("off"))
                {
                	p.setAllowFlight(false);
                }
                else
                {
                	p.sendMessage("usage: /fly [on:off]");
                    return true;
                }
            }
            p.sendMessage(plugin.prefix + "Fly has been "+(p.getAllowFlight() ? ChatColor.GREEN + "enabled" : "disabled"));
        }
        
        return true;
    } 
        
}

