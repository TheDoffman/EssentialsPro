package org.hoffmantv.essentialspro.managers;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * WarningManager persists player warnings to a YAML file.
 */
public class WarningManager {

    private final File warningsFile;
    private final YamlConfiguration warningsConfig;
    private final Logger logger;

    public WarningManager(File dataFolder, Logger logger) {
        this.logger = logger;
        this.warningsFile = new File(dataFolder, "warnings.yml");
        if (!warningsFile.exists()) {
            try {
                if (warningsFile.getParentFile() != null) {
                    warningsFile.getParentFile().mkdirs();
                }
                warningsFile.createNewFile();
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Failed to create warnings.yml file.", e);
            }
        }
        this.warningsConfig = YamlConfiguration.loadConfiguration(warningsFile);
    }

    public void addWarning(UUID playerId, String actor, String reason, long timestampMillis) {
        if (playerId == null || reason == null) {
            return;
        }
        String key = playerId.toString();
        List<String> warnings = new ArrayList<>(warningsConfig.getStringList(key));
        warnings.add(formatWarning(timestampMillis, actor, reason));
        warningsConfig.set(key, warnings);
        save();
    }

    public List<String> getWarnings(UUID playerId) {
        if (playerId == null) {
            return Collections.emptyList();
        }
        return new ArrayList<>(warningsConfig.getStringList(playerId.toString()));
    }

    public void clearWarnings(UUID playerId) {
        if (playerId == null) {
            return;
        }
        warningsConfig.set(playerId.toString(), null);
        save();
    }

    private String formatWarning(long timestampMillis, String actor, String reason) {
        String safeActor = actor == null || actor.isBlank() ? "Console" : actor;
        return timestampMillis + "|" + safeActor + "|" + reason;
    }

    private void save() {
        try {
            warningsConfig.save(warningsFile);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to save warnings.yml file.", e);
        }
    }
}
