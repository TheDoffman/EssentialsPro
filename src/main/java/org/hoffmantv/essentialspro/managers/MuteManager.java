package org.hoffmantv.essentialspro.managers;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MuteManager {
    private final Map<UUID, Long> mutedPlayers = new HashMap<>();
    private final File muteFile;
    private final YamlConfiguration muteConfig;

    public MuteManager(File dataFolder) {
        // Create the mute file
        muteFile = new File(dataFolder, "mutes.yml");
        if (!muteFile.exists()) {
            try {
                muteFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        muteConfig = YamlConfiguration.loadConfiguration(muteFile);

        // Load mutes from file
        loadMutesFromFile();
    }

    // Mute a player with a specified duration (in seconds)
    public void mutePlayer(Player player, long durationInSeconds) {
        long endTime = System.currentTimeMillis() + (durationInSeconds * 1000);
        mutedPlayers.put(player.getUniqueId(), endTime);
        saveMutesToFile();
    }

    // Unmute a player
    public void unmutePlayer(Player player) {
        mutedPlayers.remove(player.getUniqueId());
        saveMutesToFile();
    }

    // Check if a player is muted
    public boolean isMuted(Player player) {
        Long endTime = mutedPlayers.get(player.getUniqueId());
        if (endTime == null) return false;

        // If the mute time has passed, unmute the player
        if (System.currentTimeMillis() > endTime) {
            mutedPlayers.remove(player.getUniqueId());
            saveMutesToFile();
            return false;
        }
        return true;
    }

    // Get the remaining mute duration (in seconds)
    public long getRemainingMuteTime(Player player) {
        Long endTime = mutedPlayers.get(player.getUniqueId());
        if (endTime == null) return 0;
        return (endTime - System.currentTimeMillis()) / 1000;
    }

    // Save muted players to the YAML file
    private void saveMutesToFile() {
        for (Map.Entry<UUID, Long> entry : mutedPlayers.entrySet()) {
            muteConfig.set(entry.getKey().toString(), entry.getValue());
        }
        try {
            muteConfig.save(muteFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load muted players from the YAML file
    private void loadMutesFromFile() {
        for (String key : muteConfig.getKeys(false)) {
            UUID playerUuid = UUID.fromString(key);
            long endTime = muteConfig.getLong(key);
            mutedPlayers.put(playerUuid, endTime);
        }
    }
}