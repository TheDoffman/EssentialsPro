package org.hoffmantv.essentialspro.managers;

import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Map;

/**
 * TeleportRequestManager manages pending teleport requests.
 * It maps target players to the players requesting to teleport to them.
 */
public class TeleportRequestManager {

    // Stores teleport requests as a mapping from target to requester
    private final Map<Player, Player> teleportRequests = new HashMap<>();

    /**
     * Adds a teleport request.
     *
     * @param requester The player who is requesting to teleport.
     * @param target The player to whom the teleport request is sent.
     */
    public void addRequest(Player requester, Player target) {
        teleportRequests.put(target, requester);
    }

    /**
     * Retrieves the teleport request for a target player.
     *
     * @param target The target player.
     * @return The player who requested to teleport to the target, or null if no request exists.
     */
    public Player getRequest(Player target) {
        return teleportRequests.get(target);
    }

    /**
     * Removes the teleport request for a target player.
     *
     * @param target The target player.
     */
    public void removeRequest(Player target) {
        teleportRequests.remove(target);
    }
}