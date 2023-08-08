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
        if (!isBanned(playerName)) {
            banList.addBan(playerName, reason, (Date) null, (String) null);
        }
    }

    public boolean isBanned(String playerName) {
        BanList banList = Bukkit.getBanList(BanList.Type.NAME);
        return banList.isBanned(playerName);
    }

    public void unbanPlayer(String playerName) {
        if (isBanned(playerName)) {
            BanList banList = Bukkit.getBanList(BanList.Type.NAME);
            banList.pardon(playerName);
        }
    }

    public void banPlayerTemporarily(Player player, String reason, long durationInSeconds) {
        if (!isBanned(player.getName())) {
            BanList banList = Bukkit.getBanList(BanList.Type.NAME);
            Date expirationDate = new Date(System.currentTimeMillis() + (durationInSeconds * 1000));
            banList.addBan(player.getName(), reason, expirationDate, (String) null);
        }
    }

    public boolean isTemporarilyBanned(String playerName) {
        BanList banList = Bukkit.getBanList(BanList.Type.NAME);
        BanEntry entry = banList.getBanEntry(playerName);
        return entry != null && entry.getExpiration() != null && entry.getExpiration().after(new Date());
    }
}
