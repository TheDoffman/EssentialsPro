package org.hoffmantv.essentialspro.listeners;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class ChatSpamPrevention implements Listener {

    private JavaPlugin plugin;
    private FileConfiguration config;
    private Map<Player, Long> lastChatTimes;
    private int chatDelay; // Time in seconds between chat messages

    public ChatSpamPrevention(JavaPlugin plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
        this.lastChatTimes = new HashMap<>();
        loadConfig();
    }

    private void loadConfig() {
        if (!config.contains("chatDelay")) {
            config.set("chatDelay", 2); // Default value of 2 seconds
            plugin.saveConfig();
        }
        chatDelay = config.getInt("chatDelay");
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPermission("essentialspro.bypass.spam")) {
            long lastChatTime = lastChatTimes.getOrDefault(player, 0L);
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastChatTime < chatDelay * 1000) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "Please wait a moment before sending another message.");
            } else {
                lastChatTimes.put(player, currentTime);
            }
        }
    }

    public void setChatDelay(int chatDelay) {
        this.chatDelay = chatDelay;
    }
}
