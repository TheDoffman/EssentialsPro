package org.hoffmantv.essentialspro.managers;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * MuteManager manages the muted state of players. It stores mute end times (in milliseconds)
 * for each muted player's UUID and persists this data to a YAML file.
 */
public class MuteManager {

    private final Map<UUID, Long> mutedPlayers = new HashMap<>();
    private final File muteFile;
    private final YamlConfiguration muteConfig;

    /**
     * Constructs a new MuteManager and loads any existing mutes from file.
     *
     * @param dataFolder The plugin's data folder.
     */
    public MuteManager(File dataFolder) {
        // Create the mute file in the plugin's data folder.
        this.muteFile = new File(dataFolder, "mutes.yml");
        if (!muteFile.exists()) {
            try {
                if (muteFile.getParentFile() != null) {
                    muteFile.getParentFile().mkdirs();
                }
                muteFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.muteConfig = YamlConfiguration.loadConfiguration(muteFile);
        loadMutesFromFile();
    }

    /**
     * Mutes a player for a specified duration (in seconds).
     *
     * @param player            The player to mute.
     * @param durationInSeconds The duration in seconds.
     */
    public void mutePlayer(Player player, long durationInSeconds) {
        long endTime = System.currentTimeMillis() + (durationInSeconds * 1000);
        mutedPlayers.put(player.getUniqueId(), endTime);
        saveMutesToFile();
    }

    /**
     * Unmutes a player.
     *
     * @param player The player to unmute.
     */
    public void unmutePlayer(Player player) {
        mutedPlayers.remove(player.getUniqueId());
        saveMutesToFile();
    }

    /**
     * Checks if a player is muted.
     * If the mute has expired, the player is automatically unmuted.
     *
     * @param player The player to check.
     * @return true if the player is muted; false otherwise.
     */
    public boolean isMuted(Player player) {
        Long endTime = mutedPlayers.get(player.getUniqueId());
        if (endTime == null) {
            return false;
        }
        // If the current time is past the mute end time, remove the mute and return false.
        if (System.currentTimeMillis() > endTime) {
            mutedPlayers.remove(player.getUniqueId());
            saveMutesToFile();
            return false;
        }
        return true;
    }

    /**
     * Gets the remaining mute duration for a player in seconds.
     *
     * @param player The player.
     * @return The remaining time in seconds, or 0 if the player is not muted.
     */
    public long getRemainingMuteTime(Player player) {
        Long endTime = mutedPlayers.get(player.getUniqueId());
        if (endTime == null) {
            return 0;
        }
        return Math.max((endTime - System.currentTimeMillis()) / 1000, 0);
    }

    /**
     * Saves the current mute data to the YAML file.
     */
    private void saveMutesToFile() {
        // Clear current entries in the configuration
        muteConfig.getKeys(false).forEach(key -> muteConfig.set(key, null));
        // Save current mutedPlayers to the configuration
        for (Map.Entry<UUID, Long> entry : mutedPlayers.entrySet()) {
            muteConfig.set(entry.getKey().toString(), entry.getValue());
        }
        try {
            muteConfig.save(muteFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads mute data from the YAML file into the mutedPlayers map.
     */
    private void loadMutesFromFile() {
        for (String key : muteConfig.getKeys(false)) {
            try {
                UUID playerUuid = UUID.fromString(key);
                long endTime = muteConfig.getLong(key);
                mutedPlayers.put(playerUuid, endTime);
            } catch (IllegalArgumentException e) {
                // If the UUID is invalid, skip the entry.
                e.printStackTrace();
            }
        }
    }
}