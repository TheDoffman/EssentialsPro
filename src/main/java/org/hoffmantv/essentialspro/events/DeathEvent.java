package org.hoffmantv.essentialspro.events;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.hoffmantv.essentialspro.EssentialsPro;

public class DeathEvent implements Listener {

    private final EssentialsPro plugin;

    public DeathEvent(EssentialsPro plugin) {
        this.plugin = plugin;
    }

    public Component getCustomDeathMessage(String cause, String playerName) {
        String message = plugin.getConfig().getString("death_messages." + cause);
        if (message == null || message.isEmpty()) {
            return Component.empty(); // Return an empty component if no custom message exists
        }
        // Replace [player] placeholder with actual player name and translate color codes
        message = message.replace("[player]", playerName);
        return LegacyComponentSerializer.legacyAmpersand().deserialize(message);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        String cause = event.getEntity().getLastDamageCause().getCause().toString();
        String playerName = event.getEntity().getName();

        Component customMessage = getCustomDeathMessage(cause, playerName);
        if (!customMessage.equals(Component.empty())) {
            event.deathMessage(customMessage); // Set the custom death message using Adventure API
        }
    }
}