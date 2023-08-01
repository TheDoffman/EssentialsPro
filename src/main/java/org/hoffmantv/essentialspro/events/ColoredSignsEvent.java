package org.hoffmantv.essentialspro.events;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class ColoredSignsEvent implements Listener {

    @EventHandler
    public void onSignChange(SignChangeEvent e) {
        // Check if the player has the "ep.signcolor" permission
        if (e.getPlayer().hasPermission("ep.signcolor")) {
            String[] lines = e.getLines();
            for (int i = 0; i < 4; i++) {
                String line = lines[i];
                // Translate color codes using '&' character
                line = ChatColor.translateAlternateColorCodes('&', line);
                e.setLine(i, line);
            }
        }
    }
}