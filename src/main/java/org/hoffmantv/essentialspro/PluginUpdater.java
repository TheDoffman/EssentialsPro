package org.hoffmantv.essentialspro;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.plugin.Plugin;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
            // An update is available
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

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Parse the JSON response using Gson
            JsonElement jsonElement = JsonParser.parseString(response.toString());
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            if (!jsonArray.isJsonNull() && jsonArray.size() > 0) {
                JsonObject latestFile = jsonArray.get(jsonArray.size() - 1).getAsJsonObject();
                version = latestFile.get("name").getAsString();
            }
        } catch (IOException e) {
            plugin.getLogger().warning("Failed to check for plugin updates: " + e.getMessage());
        }
        return version;
    }

    private void downloadUpdate() {
        // Add your download URL logic here
        String downloadUrl = "https://dev.bukkit.org/projects/essentialspro/files/latest"; // This is a placeholder, make sure to fetch the correct URL from your API

        Path updateFolderPath = Paths.get("Update");
        if (!Files.exists(updateFolderPath)) {
            try {
                Files.createDirectories(updateFolderPath);
            } catch (IOException e) {
                plugin.getLogger().warning("Failed to create update directory: " + e.getMessage());
            }
        }

        try (BufferedInputStream in = new BufferedInputStream(new URL(downloadUrl).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(updateFolderPath.resolve(plugin.getName() + ".jar").toString())) {
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
