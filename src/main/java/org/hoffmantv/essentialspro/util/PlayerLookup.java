package org.hoffmantv.essentialspro.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Helper for consistent online player lookups.
 */
public final class PlayerLookup {

    private PlayerLookup() {
    }

    public static Player findOnlinePlayer(String name) {
        if (name == null || name.isBlank()) {
            return null;
        }
        Player exact = Bukkit.getPlayerExact(name);
        if (exact != null && exact.isOnline()) {
            return exact;
        }
        Player partial = Bukkit.getPlayer(name);
        if (partial != null && partial.isOnline()) {
            return partial;
        }
        return null;
    }
}
