package org.hoffmantv.essentialspro.events;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.hoffmantv.essentialspro.EssentialsPro;

public class DeathEvent implements Listener {

    private final EssentialsPro plugin;

    public DeathEvent(EssentialsPro plugin) {
        this.plugin = plugin;
    }

    public String getCustomDeathMessage(String cause, String playerName) {
        String message = plugin.getConfig().getString("death_messages." + cause);
        if (message == null) {
            return ""; // Return default or some other fallback message
        }
        return ChatColor.translateAlternateColorCodes('&', message.replace("[player]", playerName));
    }
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        String cause = event.getEntity().getLastDamageCause().getCause().toString();
        String playerName = event.getEntity().getName();

        String customMessage = getCustomDeathMessage(cause, playerName);
        if (!customMessage.isEmpty()) {
            event.setDeathMessage(customMessage);
        }
    }


}
