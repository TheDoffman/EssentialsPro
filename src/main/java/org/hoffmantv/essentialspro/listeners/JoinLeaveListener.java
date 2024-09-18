package org.hoffmantv.essentialspro.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.hoffmantv.essentialspro.EssentialsPro;

public class JoinLeaveListener implements Listener {

    private final EssentialsPro plugin;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public JoinLeaveListener(EssentialsPro plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Get the custom join message from the config with a default message
        String joinMessage = plugin.getConfig().getString("joinMessage", "<yellow>[+] <green>{player} <yellow>has joined the server!");

        // Replace the {player} placeholder with the player's name
        joinMessage = joinMessage.replace("{player}", event.getPlayer().getName());

        // Send the custom join message using Adventure API (with MiniMessage format for colors)
        event.joinMessage(miniMessage.deserialize(joinMessage));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // Get the custom leave message from the config with a default message
        String leaveMessage = plugin.getConfig().getString("leaveMessage", "<red>[-] <gray>{player} <red>has left the server!");

        // Replace the {player} placeholder with the player's name
        leaveMessage = leaveMessage.replace("{player}", event.getPlayer().getName());

        // Send the custom leave message using Adventure API (with MiniMessage format for colors)
        event.quitMessage(miniMessage.deserialize(leaveMessage));
    }
}