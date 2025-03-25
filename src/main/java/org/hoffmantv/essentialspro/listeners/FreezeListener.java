package org.hoffmantv.essentialspro.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.hoffmantv.essentialspro.managers.FreezeManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * FreezeListener prevents frozen players from moving and sends them a warning message.
 * A cooldown mechanism is used to avoid spamming the warning message.
 */
public class FreezeListener implements Listener {

    private final FreezeManager freezeManager;
    // Map to track the last time a frozen warning was sent to each player (in milliseconds)
    private final Map<UUID, Long> lastMessageTimes = new HashMap<>();
    // Cooldown period (in milliseconds) between warning messages
    private static final long MESSAGE_COOLDOWN = 2000L; // 2 seconds

    public FreezeListener(FreezeManager freezeManager) {
        this.freezeManager = freezeManager;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        // Check if the player is currently frozen
        if (freezeManager.isPlayerFrozen(event.getPlayer())) {
            event.setCancelled(true); // Cancel movement

            UUID playerId = event.getPlayer().getUniqueId();
            long currentTime = System.currentTimeMillis();

            // Check if a warning message was sent recently
            if (!lastMessageTimes.containsKey(playerId) || (currentTime - lastMessageTimes.get(playerId)) > MESSAGE_COOLDOWN) {
                event.getPlayer().sendMessage(
                        Component.text("✖ You are frozen and cannot move!", NamedTextColor.RED)
                );
                // Update the last message time for this player
                lastMessageTimes.put(playerId, currentTime);
            }
        }
    }
}