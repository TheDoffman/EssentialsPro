package org.hoffmantv.essentialspro.listeners;

import org.bukkit.ChatColor;
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
            event.getPlayer().sendMessage(ChatColor.RED + "You are muted. Time remaining: " + remainingTime + " seconds.");
            event.setCancelled(true);  // Prevent the message from being sent
        }
    }
}