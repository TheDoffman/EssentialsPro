package me.hoffman.essentialspro.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import me.hoffman.essentialspro.main.Main;

public class PlayerCommandProcess implements Listener {
	
    public PlayerCommandProcess(Main plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
 @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
     String msg = e.getMessage().toLowerCase();
            if (e.getPlayer().isOp()) {
                    return;
            }
           
            if (msg.startsWith("/?") || e.getMessage().startsWith("/pl") || e.getMessage().startsWith("/plugins") || e.getMessage().startsWith("/me") || e.getMessage().startsWith("/help")) {
                e.setCancelled(true);
                e.getPlayer().sendMessage(ChatColor.RED + "This command is for the console only!");
            }
            if (msg.startsWith("/op")) {
                e.setCancelled(true);
                e.getPlayer().sendMessage(ChatColor.RED + "This command is for the console only!");
        }
    }
}

