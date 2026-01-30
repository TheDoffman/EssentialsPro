package org.hoffmantv.essentialspro.managers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * VanishManager tracks vanished players and handles visibility.
 */
public class VanishManager {

    private final Set<UUID> vanishedPlayers = new HashSet<>();
    private final Plugin plugin;

    public VanishManager(Plugin plugin) {
        this.plugin = plugin;
    }

    public boolean isVanished(Player player) {
        return player != null && vanishedPlayers.contains(player.getUniqueId());
    }

    public void setVanished(Player player, boolean vanished) {
        if (player == null) {
            return;
        }
        if (vanished) {
            vanishedPlayers.add(player.getUniqueId());
            hideFromAll(player);
            player.setInvisible(true);
        } else {
            vanishedPlayers.remove(player.getUniqueId());
            showToAll(player);
            player.setInvisible(false);
        }
    }

    public void handleJoin(Player player) {
        for (UUID uuid : vanishedPlayers) {
            Player vanished = Bukkit.getPlayer(uuid);
            if (vanished != null && vanished.isOnline()) {
                player.hidePlayer(plugin, vanished);
            }
        }
    }

    private void hideFromAll(Player player) {
        for (Player online : Bukkit.getOnlinePlayers()) {
            if (!online.equals(player)) {
                online.hidePlayer(plugin, player);
            }
        }
    }

    private void showToAll(Player player) {
        for (Player online : Bukkit.getOnlinePlayers()) {
            if (!online.equals(player)) {
                online.showPlayer(plugin, player);
            }
        }
    }
}
