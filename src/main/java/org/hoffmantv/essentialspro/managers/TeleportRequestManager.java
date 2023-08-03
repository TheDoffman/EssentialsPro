package org.hoffmantv.essentialspro.managers;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class TeleportRequestManager {
    private Map<Player, Player> teleportRequests = new HashMap<>();

    public void addRequest(Player requester, Player target) {
        teleportRequests.put(target, requester);
    }

    public Player getRequest(Player target) {
        return teleportRequests.get(target);
    }

    public void removeRequest(Player target) {
        teleportRequests.remove(target);
    }
}
