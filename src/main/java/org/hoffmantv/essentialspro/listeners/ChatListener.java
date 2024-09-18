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
            // Send message using Adventure API with NamedTextColor for coloring
            event.getPlayer().sendMessage(Component.text("You are muted. Time remaining: " + remainingTime + " seconds.")
                    .color(NamedTextColor.RED));
            event.setCancelled(true);  // Prevent the message from being sent
        }
    }
}