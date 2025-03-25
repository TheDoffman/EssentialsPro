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

/**
 * The MotdCommand class handles the /motd command.
 * If no arguments are provided, it displays the current MOTD.
 * Otherwise, it sets the custom MOTD and updates the server.properties file.
 * The MOTD is displayed to players using legacy formatting codes.
 */
public class MotdCommand implements CommandExecutor, Listener {

    private final EssentialsPro plugin;
    private final FileConfiguration config;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();
    private final LegacyComponentSerializer legacySerializer = LegacyComponentSerializer.legacySection();

    public MotdCommand(EssentialsPro plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // If no arguments, display the current MOTD with an info icon.
        if (args.length == 0) {
            String motd = getMotd();
            sender.sendMessage(
                    Component.text("ℹ Current MOTD: ", NamedTextColor.GREEN)
                            .append(Component.text(motd, NamedTextColor.WHITE))
            );
        } else {
            // Otherwise, set a custom MOTD.
            String motdInput = String.join(" ", args);
            config.set("motd.custom", motdInput);
            plugin.saveConfig();
            updateServerProperties(motdInput);
            sender.sendMessage(
                    Component.text("✔ MOTD updated to: ", NamedTextColor.GREEN)
                            .append(Component.text(motdInput, NamedTextColor.WHITE))
            );
        }
        return true;
    }

    /**
     * Updates the MOTD in the server.properties file with the provided MOTD.
     *
     * @param motd The new MOTD string.
     */
    private void updateServerProperties(String motd) {
        Path serverPropertiesPath = Paths.get("server.properties");
        try {
            String serverPropertiesContent = Files.readString(serverPropertiesPath);
            // Convert the MOTD to a legacy formatted string
            String legacyMotd = toLegacyString(motd);
            serverPropertiesContent = serverPropertiesContent.replaceAll("motd=.*", "motd=" + legacyMotd);
            Files.write(serverPropertiesPath, serverPropertiesContent.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the current MOTD from the config.
     * Uses the custom MOTD if available, otherwise the default MOTD.
     *
     * @return The MOTD string.
     */
    private String getMotd() {
        String customMotd = config.getString("motd.custom", "").trim();
        return !customMotd.isEmpty() ? customMotd : config.getString("motd.default", "");
    }

    @EventHandler
    public void onServerListPing(ServerListPingEvent event) {
        String motd = getMotd();
        event.setMotd(toLegacyString(motd));
    }

    /**
     * Converts a string containing ampersand color codes to a legacy formatted string using '§' codes.
     * Optionally, you can use MiniMessage by uncommenting the code below.
     *
     * @param text The input text.
     * @return The legacy formatted string.
     */
    private String toLegacyString(String text) {
        // If using MiniMessage formatting:
        // Component comp = miniMessage.deserialize(text);
        // return legacySerializer.serialize(comp);

        // For simple ampersand-to-section conversion:
        return text.replace('&', '§');
    }
}