package org.hoffmantv.essentialspro.events;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.hoffmantv.essentialspro.EssentialsPro;

/**
 * DeathEvent handles custom death messages for players.
 * It retrieves a custom message from the plugin configuration based on the damage cause,
 * replaces placeholders (such as {player}), and applies MiniMessage formatting.
 * If no custom message is defined, the default message is left unchanged.
 */
public class DeathEvent implements Listener {

    private final EssentialsPro plugin;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    /**
     * Constructs a new DeathEvent listener.
     *
     * @param plugin the main plugin instance
     */
    public DeathEvent(EssentialsPro plugin) {
        this.plugin = plugin;
    }

    /**
     * Retrieves the custom death message from the config based on the damage cause.
     * Replaces the placeholder "{player}" with the actual player's name.
     *
     * @param cause      The cause of death (as a string).
     * @param playerName The name of the player who died.
     * @return A formatted Component representing the custom death message, or an empty component if none is found.
     */
    public Component getCustomDeathMessage(String cause, String playerName) {
        String message = plugin.getConfig().getString("death_messages." + cause);
        if (message == null || message.isEmpty()) {
            // Return an empty component if no custom message is set.
            return Component.empty();
        }
        // Replace placeholder {player} with the actual player's name.
        message = message.replace("{player}", playerName);
        // Deserialize using MiniMessage and return the component.
        return miniMessage.deserialize(message);
    }

    /**
     * Event handler for player deaths. If a custom death message is defined for the damage cause,
     * it is applied to the event.
     *
     * @param event the PlayerDeathEvent
     */
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        // Ensure there is a valid damage cause before proceeding.
        if (event.getEntity().getLastDamageCause() == null) {
            return;
        }

        // Retrieve the cause and player name.
        String cause = event.getEntity().getLastDamageCause().getCause().toString();
        String playerName = event.getEntity().getName();

        // Attempt to get the custom death message from the config.
        Component customMessage = getCustomDeathMessage(cause, playerName);

        // If a custom message is present, set it.
        if (!customMessage.equals(Component.empty())) {
            event.deathMessage(customMessage);
        } else {
            // Optionally, you can provide a default death message with a Unicode icon:
            Component defaultMessage = Component.text("☠ " + playerName + " died.", NamedTextColor.GRAY);
            event.deathMessage(defaultMessage);
        }
    }
}