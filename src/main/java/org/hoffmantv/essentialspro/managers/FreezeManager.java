package org.hoffmantv.essentialspro.managers;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class FreezeManager {

    private final Map<Player, Boolean> frozenPlayers;

    public FreezeManager() {
        frozenPlayers = new HashMap<>();
    }

    public boolean isPlayerFrozen(Player player) {
        return frozenPlayers.getOrDefault(player, false);
    }

    public void setPlayerFrozen(Player player, boolean frozen) {
        frozenPlayers.put(player, frozen);
    }
}
