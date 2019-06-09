package me.thedoffman.essentialspro.commands;

import me.thedoffman.essentialspro.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClearChat implements CommandExecutor {
	
    @SuppressWarnings("unused")
	private static Main plugin = Main.getPlugin(Main.class);

    public ClearChat(Main plugin) {
        Bukkit.getPluginCommand("clearchat").setExecutor(this);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        
        if (cmd.getName().equalsIgnoreCase("clearchat")) {
            Player p = (Player) sender;
           
            if(p.hasPermission("clearchat")) {
               
               
            	for (int x = 0; x < 150; x++){
            	    Bukkit.broadcastMessage("");
            	}
               
                Bukkit.broadcastMessage(ChatColor.GREEN + "|-------------------+====+-------------------|");
                Bukkit.broadcastMessage(" The chat has been cleared by " + ChatColor.GREEN + sender.getName());
                Bukkit.broadcastMessage(ChatColor.GREEN + "|-------------------+====+-------------------|");
               
                return true;
               
            }
        }
		return false;
    }
}

