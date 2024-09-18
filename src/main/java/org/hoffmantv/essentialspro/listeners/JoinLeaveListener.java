package org.hoffmantv.essentialspro.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.hoffmantv.essentialspro.EssentialsPro;

public class JoinLeaveListener implements Listener {

    private final EssentialsPro plugin;

    public JoinLeaveListener(EssentialsPro plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Get the custom join message from the config
        String joinMessage = plugin.getConfig().getString("joinMessage", "&e[+] {player} has joined the server!");

        // Replace the {player} placeholder with the player's name
        joinMessage = joinMessage.replace("{player}", event.getPlayer().getName());

        // Send the custom join message (with color codes)
        event.setJoinMessage(ChatColor.translateAlternateColorCodes('&', joinMessage));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // Get the custom leave message from the config
        String leaveMessage = plugin.getConfig().getString("leaveMessage", "&c[-] {player} has left the server!");

        // Replace the {player} placeholder with the player's name
        leaveMessage = leaveMessage.replace("{player}", event.getPlayer().getName());

        // Send the custom leave message (with color codes)
        event.setQuitMessage(ChatColor.translateAlternateColorCodes('&', leaveMessage));
    }
}