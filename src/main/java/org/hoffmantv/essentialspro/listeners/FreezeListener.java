package org.hoffmantv.essentialspro.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.hoffmantv.essentialspro.managers.FreezeManager;

public class FreezeListener implements Listener {

    private final FreezeManager freezeManager;

    public FreezeListener(FreezeManager freezeManager) {
        this.freezeManager = freezeManager;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (freezeManager.isPlayerFrozen(event.getPlayer())) {
            event.setCancelled(true);
        }
    }
}
