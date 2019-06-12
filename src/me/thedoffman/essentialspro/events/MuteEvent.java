package me.thedoffman.essentialspro.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.thedoffman.essentialspro.main.Main;

public class MuteEvent implements Listener {

	private Main plugin = Main.getPlugin(Main.class);
	
    public MuteEvent(Main plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
   
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
       
        // DEFINING PLAYER
       
        Player p = e.getPlayer();
       
        // DISABLING CHAT / CHECKING
       
        if(plugin.getplayers().getBoolean(p.getName() + ".mute") == true) {
           
            e.setCancelled(true);
           
            //need to cancel receiving messages
           
            e.getRecipients().remove(p);
           
            p.sendMessage(ChatColor.RED.toString() + ChatColor.ITALIC + "Your chat is disabled!");
           
        }
       
        for(Player pl : e.getRecipients()) {
           
        	if(plugin.getplayers().getBoolean(p.getName() + ".mute") == true) {
               
                e.getRecipients().remove(pl);
               
            }
        }
    }
}
