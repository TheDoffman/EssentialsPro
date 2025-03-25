package org.hoffmantv.essentialspro.managers;

import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * FreezeManager manages the frozen state of players.
 * It maintains a set of player UUIDs representing those who are frozen.
 */
public class FreezeManager {

    private final Set<UUID> frozenPlayers;

    /**
     * Constructs a new FreezeManager with an empty frozen players set.
     */
    public FreezeManager() {
        frozenPlayers = new HashSet<>();
    }

    /**
     * Checks if the specified player is currently frozen.
     *
     * @param player The player to check.
     * @return true if the player is frozen, false otherwise.
     */
    public boolean isPlayerFrozen(Player player) {
        return frozenPlayers.contains(player.getUniqueId());
    }

    /**
     * Sets the frozen state for a player.
     *
     * @param player The player whose state is to be updated.
     * @param frozen If true, the player is added to the frozen set; if false, removed.
     */
    public void setPlayerFrozen(Player player, boolean frozen) {
        if (frozen) {
            frozenPlayers.add(player.getUniqueId());
        } else {
            frozenPlayers.remove(player.getUniqueId());
        }
    }

    /**
     * Clears all frozen players.
     */
    public void clearAllFrozenPlayers() {
        frozenPlayers.clear();
    }
}