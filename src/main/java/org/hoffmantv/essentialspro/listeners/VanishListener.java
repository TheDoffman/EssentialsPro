package org.hoffmantv.essentialspro.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.hoffmantv.essentialspro.managers.VanishManager;

/**
 * Keeps vanished players hidden from joiners.
 */
public class VanishListener implements Listener {

    private final VanishManager vanishManager;

    public VanishListener(VanishManager vanishManager) {
        this.vanishManager = vanishManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        vanishManager.handleJoin(event.getPlayer());
    }
}
