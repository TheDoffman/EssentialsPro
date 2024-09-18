package org.hoffmantv.essentialspro;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.*;

public class PluginUpdater {

    private final Plugin plugin;
    private final int resourceId;

    public PluginUpdater(Plugin plugin, int resourceId) {
        this.plugin = plugin;
        this.resourceId = resourceId;
    }

    public boolean isPluginUpToDate() {
        String currentVersion = plugin.getDescription().getVersion();
        String latestVersion = getLatestVersionFromWebsite();

        if (latestVersion != null && !latestVersion.equals(currentVersion)) {
            plugin.getLogger().warning("A new version of the plugin is available: " + latestVersion);
            plugin.getLogger().warning("You can download it from: https://dev.bukkit.org/projects/essentialspro/files/latest");
            downloadUpdate();
            return false;
        }
        return true;
    }

    private String getLatestVersionFromWebsite() {
        String version = null;
        try {
            URL url = new URL("https://api.curseforge.com/servermods/files?projectIds=" + resourceId);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");

            // using try-with-resources to automatically close the BufferedReader
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                // Parsing the JSON response using Gson
                JsonElement jsonElement = JsonParser.parseString(response.toString());
                JsonArray jsonArray = jsonElement.getAsJsonArray();
                if (!jsonArray.isJsonNull() && jsonArray.size() > 0) {
                    JsonObject latestFile = jsonArray.get(jsonArray.size() - 1).getAsJsonObject();
                    version = latestFile.get("name").getAsString();
                }
            }
        } catch (IOException e) {
            plugin.getLogger().warning("Failed to check for plugin updates: " + e.getMessage());
        }
        return version;
    }

    private void downloadUpdate() {
        String latestVersion = getLatestVersionFromWebsite();
        if (latestVersion == null) {
            return;
        }

        Path updateFolderPath = Paths.get("Update");
        Path updateFilePath = updateFolderPath.resolve(plugin.getName() + "-" + latestVersion + ".jar");

        // Check if the update file with the latest version already exists
        if (Files.exists(updateFilePath)) {
            plugin.getLogger().info("Latest version of the plugin is already downloaded.");
            return;
        }

        String downloadUrl = "https://dev.bukkit.org/projects/essentialspro/files/latest";

        if (!Files.exists(updateFolderPath)) {
            try {
                Files.createDirectories(updateFolderPath);
            } catch (IOException e) {
                plugin.getLogger().warning("Failed to create update directory: " + e.getMessage());
                return;
            }
        }

        try (BufferedInputStream in = new BufferedInputStream(new URL(downloadUrl).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(updateFilePath.toString())) {
            byte dataBuffer[] = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
        } catch (IOException e) {
            plugin.getLogger().warning("Failed to download update: " + e.getMessage());
        }
    }

}
