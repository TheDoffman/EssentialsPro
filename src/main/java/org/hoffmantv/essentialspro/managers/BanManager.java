package org.hoffmantv.essentialspro.managers;

import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Date;

/**
 * BanManager provides methods for banning and unbanning players.
 */
public class BanManager {

    /**
     * Bans a player permanently using their name.
     *
     * @param playerName the name of the player to ban.
     * @param reason     the reason for the ban.
     */
    public void banPlayer(String playerName, String reason) {
        BanList banList = Bukkit.getBanList(BanList.Type.NAME);
        if (!isBanned(playerName)) {
            // Use explicitly typed null values for expiration and source to avoid ambiguity.
            Date expiration = null;
            String source = null;
            banList.addBan(playerName, reason, expiration, source);
        }
    }

    /**
     * Checks whether the specified player is banned.
     *
     * @param playerName the name of the player.
     * @return true if the player is banned; false otherwise.
     */
    public boolean isBanned(String playerName) {
        BanList banList = Bukkit.getBanList(BanList.Type.NAME);
        return banList.isBanned(playerName);
    }

    /**
     * Unbans the specified player if they are banned.
     *
     * @param playerName the name of the player to unban.
     */
    public void unbanPlayer(String playerName) {
        if (isBanned(playerName)) {
            BanList banList = Bukkit.getBanList(BanList.Type.NAME);
            banList.pardon(playerName);
        }
    }

    /**
     * Temporarily bans a player for a specified duration.
     *
     * @param player             the player to ban.
     * @param reason             the reason for the ban.
     * @param durationInSeconds  duration of the ban in seconds.
     */
    public void banPlayerTemporarily(Player player, String reason, long durationInSeconds) {
        if (!isBanned(player.getName())) {
            BanList banList = Bukkit.getBanList(BanList.Type.NAME);
            // Calculate the expiration date for the temporary ban.
            Date expirationDate = new Date(System.currentTimeMillis() + (durationInSeconds * 1000));
            String source = null; // Source can be set if needed
            banList.addBan(player.getName(), reason, expirationDate, source);
        }
    }

    /**
     * Checks whether the specified player is temporarily banned.
     *
     * @param playerName the name of the player.
     * @return true if the player is temporarily banned; false otherwise.
     */
    public boolean isTemporarilyBanned(String playerName) {
        BanList banList = Bukkit.getBanList(BanList.Type.NAME);
        BanEntry entry = banList.getBanEntry(playerName);
        return entry != null && entry.getExpiration() != null && entry.getExpiration().after(new Date());
    }
}