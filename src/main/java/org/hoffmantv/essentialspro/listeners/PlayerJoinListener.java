package org.hoffmantv.essentialspro.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;

public class PlayerJoinListener implements Listener {
    private JavaPlugin plugin;
    private FileConfiguration config;

    public PlayerJoinListener( JavaPlugin plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();

        // Get the custom join message from the config.yml or use the default message
        String joinMessage = config.getString("join_message", "&aWelcome back, [PLAYER]! Enjoy your stay on the server!");

        // Replace the [PLAYER] placeholder with the actual player name
        joinMessage = joinMessage.replace("[PLAYER]", playerName);

        // Send the custom join message to all players with color codes
        plugin.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', joinMessage));
    }
}
