package org.hoffmantv.essentialspro.managers;

import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class FreezeManager {

    private final Set<UUID> frozenPlayers;

    public FreezeManager() {
        frozenPlayers = new HashSet<>();
    }

    public boolean isPlayerFrozen(Player player) {
        return frozenPlayers.contains(player.getUniqueId());
    }

    public void setPlayerFrozen(Player player, boolean frozen) {
        if (frozen) {
            frozenPlayers.add(player.getUniqueId());
        } else {
            frozenPlayers.remove(player.getUniqueId());
        }
    }
}
