package org.hoffmantv.essentialspro.listeners;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.hoffmantv.essentialspro.EssentialsPro;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();

        // Get the custom join message from the config.yml or use the default message
        String joinMessage = EssentialsPro.getInstance().getConfig().getString("join_message", "&aWelcome back, [PLAYER]! Enjoy your stay on the server!");

        // Replace the [PLAYER] placeholder with the actual player name
        joinMessage = joinMessage.replace("[PLAYER]", playerName);

        // Send the custom join message to the player
        player.sendMessage(Component.text(joinMessage));
    }
}

