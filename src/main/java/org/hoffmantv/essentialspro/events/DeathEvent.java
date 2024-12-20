package org.hoffmantv.essentialspro.events;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.hoffmantv.essentialspro.EssentialsPro;

public class DeathEvent implements Listener {

    private final EssentialsPro plugin;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public DeathEvent(EssentialsPro plugin) {
        this.plugin = plugin;
    }

    /**
     * Retrieves the custom death message based on the damage cause.
     * If no custom message is found, returns an empty component.
     */
    public Component getCustomDeathMessage(String cause, String playerName) {
        String message = plugin.getConfig().getString("death_messages." + cause);
        if (message == null || message.isEmpty()) {
            return Component.empty(); // No custom message found
        }

        // Replace the placeholder {player} with the player's name
        message = message.replace("{player}", playerName);

        // Deserialize MiniMessage format from the config
        return miniMessage.deserialize(message);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (event.getEntity().getLastDamageCause() == null) {
            return; // If no cause is available, do nothing
        }

        String cause = event.getEntity().getLastDamageCause().getCause().toString();
        String playerName = event.getEntity().getName();

        Component customMessage = getCustomDeathMessage(cause, playerName);
        if (!customMessage.equals(Component.empty())) {
            event.deathMessage(customMessage); // Set the custom death message using Adventure API
        }
    }
}