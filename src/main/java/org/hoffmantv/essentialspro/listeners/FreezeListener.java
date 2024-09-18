package org.hoffmantv.essentialspro.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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
        // Check if the player is frozen
        if (freezeManager.isPlayerFrozen(event.getPlayer())) {
            // Cancel the player's movement
            event.setCancelled(true);

            // Inform the player that they are frozen
            event.getPlayer().sendMessage(
                    Component.text("You are frozen and cannot move!").color(NamedTextColor.RED)
            );
        }
    }
}