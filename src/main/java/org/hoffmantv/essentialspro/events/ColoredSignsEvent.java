package org.hoffmantv.essentialspro.events;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class ColoredSignsEvent implements Listener {

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        // Check if the player has the "ep.signcolor" permission
        if (event.getPlayer().hasPermission("ep.signcolor")) {
            for (int i = 0; i < 4; i++) {
                String line = event.getLine(i);
                if (line == null) {
                    line = "";
                }

                // Convert legacy '&' color codes to Adventure Components
                Component coloredLine = LegacyComponentSerializer.legacyAmpersand().deserialize(line);

                // Set the line directly as a Component (Paper API)
                event.line(i, coloredLine);
            }
        }
    }
}