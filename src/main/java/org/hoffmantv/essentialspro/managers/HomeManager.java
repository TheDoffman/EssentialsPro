package org.hoffmantv.essentialspro.managers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * HomeManager persists player homes to a YAML file.
 */
public class HomeManager {

    private final File homesFile;
    private final YamlConfiguration homesConfig;
    private final Logger logger;

    public HomeManager(File dataFolder, Logger logger) {
        this.logger = logger;
        this.homesFile = new File(dataFolder, "homes.yml");
        if (!homesFile.exists()) {
            try {
                if (homesFile.getParentFile() != null) {
                    homesFile.getParentFile().mkdirs();
                }
                homesFile.createNewFile();
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Failed to create homes.yml file.", e);
            }
        }
        this.homesConfig = YamlConfiguration.loadConfiguration(homesFile);
    }

    public void setHome(UUID playerId, String homeName, Location location) {
        if (playerId == null || homeName == null || location == null || location.getWorld() == null) {
            return;
        }
        String key = buildKey(playerId, homeName);
        homesConfig.set(key + ".world", location.getWorld().getName());
        homesConfig.set(key + ".x", location.getX());
        homesConfig.set(key + ".y", location.getY());
        homesConfig.set(key + ".z", location.getZ());
        homesConfig.set(key + ".yaw", location.getYaw());
        homesConfig.set(key + ".pitch", location.getPitch());
        save();
    }

    public Location getHome(UUID playerId, String homeName) {
        if (playerId == null || homeName == null) {
            return null;
        }
        String key = buildKey(playerId, homeName);
        String worldName = homesConfig.getString(key + ".world");
        if (worldName == null) {
            return null;
        }
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            return null;
        }
        double x = homesConfig.getDouble(key + ".x");
        double y = homesConfig.getDouble(key + ".y");
        double z = homesConfig.getDouble(key + ".z");
        float yaw = (float) homesConfig.getDouble(key + ".yaw");
        float pitch = (float) homesConfig.getDouble(key + ".pitch");
        return new Location(world, x, y, z, yaw, pitch);
    }

    public Set<String> listHomes(UUID playerId) {
        if (playerId == null) {
            return Collections.emptySet();
        }
        String baseKey = playerId.toString();
        if (!homesConfig.contains(baseKey)) {
            return Collections.emptySet();
        }
        return new HashSet<>(homesConfig.getConfigurationSection(baseKey).getKeys(false));
    }

    public boolean deleteHome(UUID playerId, String homeName) {
        if (playerId == null || homeName == null) {
            return false;
        }
        String key = buildKey(playerId, homeName);
        if (!homesConfig.contains(key)) {
            return false;
        }
        homesConfig.set(key, null);
        save();
        return true;
    }

    public boolean renameHome(UUID playerId, String oldName, String newName) {
        if (playerId == null || oldName == null || newName == null) {
            return false;
        }
        String oldKey = buildKey(playerId, oldName);
        String newKey = buildKey(playerId, newName);
        if (!homesConfig.contains(oldKey) || homesConfig.contains(newKey)) {
            return false;
        }
        homesConfig.set(newKey, homesConfig.get(oldKey));
        homesConfig.set(oldKey, null);
        save();
        return true;
    }

    private String buildKey(UUID playerId, String homeName) {
        return playerId + "." + homeName.toLowerCase();
    }

    private void save() {
        try {
            homesConfig.save(homesFile);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to save homes.yml file.", e);
        }
    }
}
