package org.hoffmantv.essentialspro.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.hoffmantv.essentialspro.managers.BackManager;

/**
 * Tracks last location for /back via teleports and deaths.
 */
public class BackListener implements Listener {

    private final BackManager backManager;

    public BackListener(BackManager backManager) {
        this.backManager = backManager;
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        backManager.pushLocation(event.getPlayer(), event.getFrom());
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        backManager.pushLocation(event.getEntity(), event.getEntity().getLocation());
    }
}
