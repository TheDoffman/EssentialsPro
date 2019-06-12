package me.thedoffman.essentialspro.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import me.thedoffman.essentialspro.main.Main;

public class JoinEvent implements Listener {
	
	private Main plugin = Main.getPlugin(Main.class);
	
    public JoinEvent(Main plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();
        
        String join = plugin.getlang().getString("Messages.JoinIG").replaceAll("%player%", e.getPlayer().getName().toString());
        join = join.replaceAll("&", "\u00A7");
		e.setJoinMessage(join);
		
        String system = plugin.getlang().getString("Messages.Join").replaceAll("%player%", e.getPlayer().getName().toString());
        system = system.replaceAll("&", "\u00A7");
        
		e.getPlayer().sendMessage(system);
		e.getPlayer().sendMessage(ChatColor.BLUE + "Type " + ChatColor.GREEN + "/ep " + ChatColor.BLUE + "for list of commands.");
			// TheDoffman Checker
		if(e.getPlayer().getName().equals("TheDoffman")){
				Bukkit.broadcastMessage(ChatColor.GOLD + "The developer of EssentialsPro " + e.getPlayer().getName() + " has joined your server!");
				
			}
        if (plugin.getplayers().getBoolean(player.getName() + ".banned") == true) {
                    player.kickPlayer(ChatColor.RED + "You are banned from the server!\nBy: " + plugin.getplayers().getString(player.getName() + ".banner") + "\nReason: " + plugin.getplayers().getString(player.getName() + ".reason"));
        }
	}
	
    @EventHandler
    public void onPlayerLogin(final PlayerLoginEvent e) {
    	
    	if (e.getResult() == PlayerLoginEvent.Result.KICK_FULL && e.getPlayer().hasPermission("ep.join.full")) {
    	    e.allow();
    	}
    }
    }

