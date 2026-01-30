package org.hoffmantv.essentialspro.managers;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * BackManager tracks recent locations for players.
 */
public class BackManager {

    private final Map<UUID, Deque<Location>> locationHistory = new HashMap<>();
    private int maxHistory = 5;

    public void setMaxHistory(int maxHistory) {
        if (maxHistory > 0) {
            this.maxHistory = maxHistory;
        }
    }

    public void pushLocation(Player player, Location location) {
        if (player == null || location == null) {
            return;
        }
        Deque<Location> history = locationHistory.computeIfAbsent(player.getUniqueId(), id -> new ArrayDeque<>());
        history.addFirst(location.clone());
        while (history.size() > maxHistory) {
            history.removeLast();
        }
    }

    public Location getLocation(Player player, int stepsBack) {
        if (player == null) {
            return null;
        }
        Deque<Location> history = locationHistory.get(player.getUniqueId());
        if (history == null || history.isEmpty()) {
            return null;
        }
        int index = Math.max(1, stepsBack) - 1;
        int i = 0;
        for (Location location : history) {
            if (i == index) {
                return location.clone();
            }
            i++;
        }
        return null;
    }
}
