package org.hoffmantv.essentialspro.managers;

import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Date;

public class BanManager {

    public void banPlayer(String playerName, String reason) {
        BanList banList = Bukkit.getBanList(BanList.Type.NAME);
        banList.addBan(playerName, reason, null, null);
    }

    public boolean isBanned(String playerName) {
        BanList banList = Bukkit.getBanList(BanList.Type.NAME);
        return banList.isBanned(playerName);
    }

    public void unbanPlayer(String playerName) {
        BanList banList = Bukkit.getBanList(BanList.Type.NAME);
        banList.pardon(playerName);
    }

    public void unbanPlayerFromConsole(String playerName) {
        if (isBanned(playerName)) {
            BanList banList = Bukkit.getBanList(BanList.Type.NAME);
            banList.pardon(playerName);
        } else {
            throw new IllegalArgumentException("Player is not banned: " + playerName);
        }
    }

    public void unbanPlayer(org.bukkit.entity.Player player, String playerName) {
        if (isBanned(playerName)) {
            BanList banList = Bukkit.getBanList(BanList.Type.NAME);
            banList.pardon(playerName);
        } else {
            throw new IllegalArgumentException("Player is not banned: " + playerName);
        }
    }
    public void banPlayerTemporarily(Player player, String reason, long durationInSeconds) {
        BanList banList = Bukkit.getBanList(BanList.Type.NAME);
        Date expirationDate = new Date(System.currentTimeMillis() + (durationInSeconds * 1000));
        banList.addBan(player.getName(), reason, expirationDate, null);
        player.kickPlayer(ChatColor.RED + "You have been temporarily banned for: " + reason);
    }

    public boolean isTemporarilyBanned(String playerName) {
        BanList banList = Bukkit.getBanList(BanList.Type.NAME);
        BanEntry entry = banList.getBanEntry(playerName);
        if (entry != null) {
            Date expirationDate = entry.getExpiration();
            if (expirationDate != null && expirationDate.after(new Date())) {
                return true;
            }
        }
        return false;
    }
}
