package org.hoffmantv.essentialspro.listeners;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.hoffmantv.essentialspro.EssentialsPro;

/**
 * JoinLeaveListener handles custom join and leave messages.
 * The messages are defined in the configuration file (or use defaults) and are formatted using MiniMessage.
 * Unicode symbols and MiniMessage formatting provide a modern and visually appealing look.
 */
public class JoinLeaveListener implements Listener {

    private final EssentialsPro plugin;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    /**
     * Constructs a new JoinLeaveListener.
     *
     * @param plugin the main plugin instance
     */
    public JoinLeaveListener(EssentialsPro plugin) {
        this.plugin = plugin;
    }

    /**
     * Called when a player joins the server.
     * Reads the join message from the configuration, replaces {player} with the player's name,
     * and sets the join message with MiniMessage formatting.
     *
     * @param event the PlayerJoinEvent
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Retrieve the join message from config or use the default message with a star symbol.
        String joinMessage = plugin.getConfig().getString("joinMessage", "<yellow>✦ [+] <green>{player} <yellow>has joined the server!");
        // Replace the {player} placeholder with the actual player's name.
        joinMessage = joinMessage.replace("{player}", event.getPlayer().getName());
        // Set the join message using MiniMessage to deserialize the formatted string.
        event.joinMessage(miniMessage.deserialize(joinMessage));
    }

    /**
     * Called when a player quits the server.
     * Reads the leave message from the configuration, replaces {player} with the player's name,
     * and sets the quit message with MiniMessage formatting.
     *
     * @param event the PlayerQuitEvent
     */
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // Retrieve the leave message from config or use the default message with a star symbol.
        String leaveMessage = plugin.getConfig().getString("leaveMessage", "<red>✦ [-] <gray>{player} <red>has left the server!");
        // Replace the {player} placeholder with the actual player's name.
        leaveMessage = leaveMessage.replace("{player}", event.getPlayer().getName());
        // Set the quit message using MiniMessage to deserialize the formatted string.
        event.quitMessage(miniMessage.deserialize(leaveMessage));
    }
}