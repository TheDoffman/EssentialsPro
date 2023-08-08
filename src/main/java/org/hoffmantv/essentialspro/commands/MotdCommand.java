package org.hoffmantv.essentialspro.commands;

import org.bukkit.ChatColor;
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
            sender.sendMessage(ChatColor.GREEN + "Current MOTD: " + motd);
        } else {
            // Set custom MOTD
            String motd = ChatColor.translateAlternateColorCodes('&', String.join(" ", args));
            config.set("motd.custom", motd);
            plugin.saveConfig();  // This saves the configuration to the config.yml
            updateServerProperties(motd);
            sender.sendMessage(ChatColor.GREEN + "MOTD updated to: " + motd);
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
        if (!customMotd.isEmpty()) {
            return customMotd;
        } else {
            return config.getString("motd.default", "");
        }
    }

    @EventHandler
    public void onServerListPing(ServerListPingEvent event) {
        String motd = getMotd();
        event.setMotd(ChatColor.translateAlternateColorCodes('&', motd));
    }
}
