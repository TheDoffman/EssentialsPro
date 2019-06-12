package me.thedoffman.essentialspro.signs;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import me.thedoffman.essentialspro.main.Main;

public class ColoredSignsEvent implements Listener {
	
    public ColoredSignsEvent(Main plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
	
	@EventHandler
	public void onSignChange(SignChangeEvent e) {
		if(e.getPlayer().hasPermission("ep.signcolor")){
	String[] lines = e.getLines();
	for(int i = 0; i < 4; i++) {
	String line = lines[ i ];
	line = ChatColor.translateAlternateColorCodes('&', line);
	e.setLine(i, line);
	}
	}
	}
	}
