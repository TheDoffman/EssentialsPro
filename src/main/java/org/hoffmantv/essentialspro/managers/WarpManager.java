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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * WarpManager persists shared warps to a YAML file.
 */
public class WarpManager {

    private final File warpsFile;
    private final YamlConfiguration warpsConfig;
    private final Logger logger;

    public WarpManager(File dataFolder, Logger logger) {
        this.logger = logger;
        this.warpsFile = new File(dataFolder, "warps.yml");
        if (!warpsFile.exists()) {
            try {
                if (warpsFile.getParentFile() != null) {
                    warpsFile.getParentFile().mkdirs();
                }
                warpsFile.createNewFile();
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Failed to create warps.yml file.", e);
            }
        }
        this.warpsConfig = YamlConfiguration.loadConfiguration(warpsFile);
    }

    public void setWarp(String name, Location location) {
        if (name == null || location == null || location.getWorld() == null) {
            return;
        }
        String key = name.toLowerCase();
        warpsConfig.set(key + ".world", location.getWorld().getName());
        warpsConfig.set(key + ".x", location.getX());
        warpsConfig.set(key + ".y", location.getY());
        warpsConfig.set(key + ".z", location.getZ());
        warpsConfig.set(key + ".yaw", location.getYaw());
        warpsConfig.set(key + ".pitch", location.getPitch());
        save();
    }

    public Location getWarp(String name) {
        if (name == null) {
            return null;
        }
        String key = name.toLowerCase();
        String worldName = warpsConfig.getString(key + ".world");
        if (worldName == null) {
            return null;
        }
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            return null;
        }
        double x = warpsConfig.getDouble(key + ".x");
        double y = warpsConfig.getDouble(key + ".y");
        double z = warpsConfig.getDouble(key + ".z");
        float yaw = (float) warpsConfig.getDouble(key + ".yaw");
        float pitch = (float) warpsConfig.getDouble(key + ".pitch");
        return new Location(world, x, y, z, yaw, pitch);
    }

    public Set<String> listWarps() {
        Set<String> keys = warpsConfig.getKeys(false);
        if (keys == null || keys.isEmpty()) {
            return Collections.emptySet();
        }
        return new HashSet<>(keys);
    }

    private void save() {
        try {
            warpsConfig.save(warpsFile);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to save warps.yml file.", e);
        }
    }
}
