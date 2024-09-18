package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import org.hoffmantv.essentialspro.EssentialsPro;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MotdCommand implements CommandExecutor, Listener {

    private final EssentialsPro plugin;
    private final FileConfiguration config;

    public MotdCommand(EssentialsPro plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            // Display current MOTD
            String motd = getMotd();
            sender.sendMessage(Component.text("Current MOTD: ", NamedTextColor.GREEN)
                    .append(Component.text(motd, NamedTextColor.WHITE)));
        } else {
            // Set custom MOTD
            String motd = translateAlternateColorCodes('&', String.join(" ", args));
            config.set("motd.custom", motd);
            plugin.saveConfig();  // Save the updated MOTD to config.yml
            updateServerProperties(motd);
            sender.sendMessage(Component.text("MOTD updated to: ", NamedTextColor.GREEN)
                    .append(Component.text(motd, NamedTextColor.WHITE)));
        }
        return true;
    }

    private void updateServerProperties(String motd) {
        Path serverPropertiesPath = Paths.get("server.properties");
        try {
            String serverPropertiesContent = Files.readString(serverPropertiesPath);
            serverPropertiesContent = serverPropertiesContent.replaceAll("motd=.*", "motd=" + motd);
            Files.write(serverPropertiesPath, serverPropertiesContent.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getMotd() {
        String customMotd = config.getString("motd.custom", "").trim();
        return !customMotd.isEmpty() ? customMotd : config.getString("motd.default", "");
    }

    @EventHandler
    public void onServerListPing(ServerListPingEvent event) {
        String motd = getMotd();
        event.setMotd(translateAlternateColorCodes('&', motd));
    }

    // Utility method to simulate translateAlternateColorCodes using Adventure API
    private String translateAlternateColorCodes(char altColorChar, String textToTranslate) {
        return textToTranslate.replace(altColorChar, '§');
    }
}