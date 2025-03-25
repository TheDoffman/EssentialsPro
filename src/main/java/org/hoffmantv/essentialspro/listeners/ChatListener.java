package org.hoffmantv.essentialspro.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.hoffmantv.essentialspro.managers.MuteManager;

/**
 * ChatListener prevents muted players from sending chat messages.
 * If a player is muted, they are informed of the remaining mute duration
 * and the chat event is cancelled.
 */
public class ChatListener implements Listener {

    private final MuteManager muteManager;

    /**
     * Constructs a new ChatListener.
     *
     * @param muteManager the MuteManager handling mute state
     */
    public ChatListener(MuteManager muteManager) {
        this.muteManager = muteManager;
    }

    /**
     * Listens for chat events and prevents muted players from sending messages.
     *
     * @param event the AsyncPlayerChatEvent
     */
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (muteManager.isMuted(player)) {
            long remainingTime = muteManager.getRemainingMuteTime(player);
            // Build the muted message using Adventure API with Unicode icons
            Component mutedMessage = Component.text("✖ You are muted. ", NamedTextColor.RED)
                    .append(Component.text("Time remaining: ", NamedTextColor.WHITE))
                    .append(Component.text(remainingTime + " seconds", NamedTextColor.YELLOW));
            player.sendMessage(mutedMessage);
            event.setCancelled(true);
        }
    }
}