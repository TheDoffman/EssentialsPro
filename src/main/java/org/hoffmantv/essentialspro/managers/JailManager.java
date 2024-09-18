package org.hoffmantv.essentialspro.managers;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class JailManager {

    private final JavaPlugin plugin;
    private final HashSet<UUID> jailedPlayers = new HashSet<>();
    private final HashMap<UUID, Location> previousLocations = new HashMap<>();

    public JailManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    // Check if a player is jailed
    public boolean isPlayerJailed(UUID playerUUID) {
        return jailedPlayers.contains(playerUUID);
    }

    // Unjail a player
    public void unJailPlayer(Player player) {
        UUID playerUUID = player.getUniqueId();
        if (jailedPlayers.contains(playerUUID)) {
            jailedPlayers.remove(playerUUID);
            player.sendMessage(ChatColor.GREEN + "✔ You have been released from jail!");

            // Teleport the player back to their previous location
            Location previousLocation = previousLocations.remove(playerUUID);
            if (previousLocation != null) {
                player.teleport(previousLocation);
            }
        }
    }

    // Other jail-related methods remain the same...
}