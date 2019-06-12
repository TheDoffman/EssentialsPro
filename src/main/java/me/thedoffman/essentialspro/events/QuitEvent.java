package me.thedoffman.essentialspro.events;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import me.thedoffman.essentialspro.main.Main;

public class QuitEvent implements Listener {

	private Main plugin = Main.getPlugin(Main.class);
	
    public QuitEvent(Main plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e){
		
		String player = e.getPlayer().getName().toString();
		
        String leave = plugin.getlang().getString("Messages.LeaveIG").replaceAll("%player%", player);
        leave = leave.replaceAll("&", "\u00A7");
        
		
		e.setQuitMessage(leave);
	}
}
