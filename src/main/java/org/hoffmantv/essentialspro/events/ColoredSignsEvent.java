package org.hoffmantv.essentialspro.events;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
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
                // Use LegacyComponentSerializer to translate color codes using '&' character
                Component coloredLine = LegacyComponentSerializer.legacyAmpersand().deserialize(line);
                e.setLine(i, LegacyComponentSerializer.legacySection().serialize(coloredLine));
            }
        }
    }
}