package org.hoffmantv.essentialspro.util;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * Loads localized message files from the plugin data folder.
 */
public class MessageManager {

    private final FileConfiguration messagesConfig;

    public MessageManager(JavaPlugin plugin, String locale) {
        String safeLocale = locale == null || locale.isBlank() ? "en" : locale;
        File folder = new File(plugin.getDataFolder(), "messages");
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File messagesFile = new File(folder, safeLocale + ".yml");
        if (!messagesFile.exists()) {
            plugin.saveResource("messages/" + safeLocale + ".yml", false);
        }
        this.messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);
    }

    public String getString(String key, String fallback) {
        return messagesConfig.getString(key, fallback);
    }
}
