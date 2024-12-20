package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
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
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public MotdCommand(EssentialsPro plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // If no arguments are provided, display the current MOTD
        if (args.length == 0) {
            String motd = getMotd();
            sender.sendMessage(
                    Component.text("Current MOTD: ", NamedTextColor.GREEN)
                            .append(Component.text(motd, NamedTextColor.WHITE))
            );
        } else {
            // Set custom MOTD
            String motdInput = String.join(" ", args);
            // Process with MiniMessage for advanced formatting if desired
            // Store this raw string as is, or consider storing MiniMessage tags and convert later
            config.set("motd.custom", motdInput);
            plugin.saveConfig();
            updateServerProperties(motdInput);

            sender.sendMessage(Component.text("MOTD updated to: ", NamedTextColor.GREEN)
                    .append(Component.text(motdInput, NamedTextColor.WHITE)));
        }
        return true;
    }

    /**
     * Updates the 'motd' line in server.properties with the given motd.
     */
    private void updateServerProperties(String motd) {
        Path serverPropertiesPath = Paths.get("server.properties");
        try {
            String serverPropertiesContent = Files.readString(serverPropertiesPath);
            // Convert MiniMessage/legacy to a plain string for server.properties
            // The server only reads motd as legacy formatting codes (i.e., §)
            String legacyMotd = toLegacyString(motd);
            serverPropertiesContent = serverPropertiesContent.replaceAll("motd=.*", "motd=" + legacyMotd);
            Files.write(serverPropertiesPath, serverPropertiesContent.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the current MOTD. If custom MOTD is present, use that; otherwise, use default.
     */
    private String getMotd() {
        String customMotd = config.getString("motd.custom", "").trim();
        return !customMotd.isEmpty() ? customMotd : config.getString("motd.default", "");
    }

    @EventHandler
    public void onServerListPing(ServerListPingEvent event) {
        String motd = getMotd();
        // Convert to legacy string to display correctly in the server list
        event.setMotd(toLegacyString(motd));
    }

    /**
     * Converts a MiniMessage/legacy-compatible string into a legacy formatted string
     * with '§' codes for compatibility with server list and server.properties.
     */
    private String toLegacyString(String text) {
        // If you want MiniMessage formatting, you can do:
        // Component comp = miniMessage.deserialize(text);
        // return LegacyComponentSerializer.legacySection().serialize(comp);

        // If currently using ampersand codes, you can just replace them:
        return text.replace('&', '§');
    }
}