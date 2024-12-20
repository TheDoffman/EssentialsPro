package org.hoffmantv.essentialspro.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.hoffmantv.essentialspro.managers.MuteManager;

public class ChatListener implements Listener {

    private final MuteManager muteManager;

    public ChatListener(MuteManager muteManager) {
        this.muteManager = muteManager;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (muteManager.isMuted(event.getPlayer())) {
            long remainingTime = muteManager.getRemainingMuteTime(event.getPlayer());

            // Build the muted message using Adventure API
            Component mutedMessage = Component.text("You are muted. ")
                    .color(NamedTextColor.RED)
                    .append(Component.text("Time remaining: "))
                    .append(Component.text(remainingTime + " seconds", NamedTextColor.YELLOW));

            // Send the message to the player
            event.getPlayer().sendMessage(mutedMessage);

            // Cancel the chat event to prevent the message from being sent
            event.setCancelled(true);
        }
    }
}