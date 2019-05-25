package me.thedoffman.essentialspro.commands;

import me.thedoffman.essentialspro.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Spawn
implements CommandExecutor {
    private Main plugin = Main.getPlugin(Main.class);

    public Spawn(Main plugin) {
        Bukkit.getPluginCommand("spawn").setExecutor(this);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        plugin.prefix = plugin.prefix.replaceAll("&", "\u00A7");
        if (cmd.getName().equalsIgnoreCase("spawn")) {
        	if (!(sender instanceof Player)) {
                sender.sendMessage(plugin.prefix + ChatColor.RED + "The console cannot use /spawn");
                 return true;
             }
            if (!(sender instanceof Player)) {
                sender.sendMessage(plugin.prefix + ChatColor.RED + "Error: Only players can set a home!");
                return true;
            }
        	Player p = (Player) sender;
                String WS = this.plugin.getlang().getString("Messages.WelcomeSpawn");
                WS = WS.replaceAll("&", "\u00A7");
                p.teleport(p.getWorld().getSpawnLocation());
                p.sendMessage(plugin.prefix + WS);
            } 
		return false;
    }
    
}


