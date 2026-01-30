package org.hoffmantv.essentialspro.events;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.hoffmantv.essentialspro.util.Permissions;

/**
 * ColoredSignsEvent listens for sign changes and translates color codes.
 * Additionally, it adds a Unicode icon to specific sign keywords (e.g. "[Heal]", "[Spawn]").
 */
public class ColoredSignsEvent implements Listener {

    // Legacy serializer for converting Adventure components to legacy '§' formatted strings
    private final LegacyComponentSerializer legacySerializer = LegacyComponentSerializer.legacyAmpersand();
    private final PlainTextComponentSerializer plainTextSerializer = PlainTextComponentSerializer.plainText();

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        // Check if the player has permission to use colored signs
        if (event.getPlayer().hasPermission(Permissions.SIGNCOLOR)) {
            for (int i = 0; i < 4; i++) {
                String line = plainTextSerializer.serialize(event.line(i));

                // Add Unicode icons for specific keywords (adjust or add as desired)
                if (line.trim().equalsIgnoreCase("[Heal]")) {
                    // Append a medical symbol (⚕) for heal signs
                    line = "[Heal] ⚕";
                } else if (line.trim().equalsIgnoreCase("[Spawn]")) {
                    // Append an airplane symbol (✈) for spawn signs
                    line = "[Spawn] ✈";
                }

                // Deserialize the line using legacy color codes and set the updated line on the sign
                Component coloredLine = legacySerializer.deserialize(line);
                event.line(i, coloredLine);
            }
        }
    }
}