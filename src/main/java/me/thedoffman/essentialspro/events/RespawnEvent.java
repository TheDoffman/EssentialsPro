package me.thedoffman.essentialspro.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import me.thedoffman.essentialspro.main.Main;

public class RespawnEvent implements Listener {
	
    public RespawnEvent(Main plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e){
        Player player = e.getPlayer();

                player.teleport(player.getWorld().getSpawnLocation());
        	}

    }

