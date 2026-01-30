package org.hoffmantv.essentialspro.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Common message helpers for consistency.
 */
public final class MessageUtils {

    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    private static Component prefix = Component.empty();
    private static Component noPermission = Component.text("✖ You don't have permission to use this command.", NamedTextColor.RED);
    private static Component onlyPlayers = Component.text("✖ This command can only be used by players.", NamedTextColor.RED);
    private static Component playerNotFound = Component.text("✖ Player not found or not online.", NamedTextColor.RED);

    private MessageUtils() {
    }

    public static void configure(FileConfiguration config, MessageManager messageManager) {
        String rawPrefix = messageManager.getString("prefix", config.getString("messages.prefix", "")).trim();
        if (rawPrefix.isEmpty()) {
            prefix = Component.empty();
        } else {
            prefix = MINI_MESSAGE.deserialize(rawPrefix);
        }

        noPermission = MINI_MESSAGE.deserialize(messageManager.getString(
                "no-permission",
                "✖ You don't have permission to use this command."
        ));
        onlyPlayers = MINI_MESSAGE.deserialize(messageManager.getString(
                "only-players",
                "✖ This command can only be used by players."
        ));
        playerNotFound = MINI_MESSAGE.deserialize(messageManager.getString(
                "player-not-found",
                "✖ Player not found or not online."
        ));
    }

    public static Component noPermission() {
        return applyPrefix(noPermission);
    }

    public static Component onlyPlayers() {
        return applyPrefix(onlyPlayers);
    }

    public static Component playerNotFound() {
        return applyPrefix(playerNotFound);
    }

    public static Component error(String message) {
        return applyPrefix(Component.text("✖ " + message, NamedTextColor.RED));
    }

    public static Component success(String message) {
        return applyPrefix(Component.text("✔ " + message, NamedTextColor.GREEN));
    }

    public static Component info(String message) {
        return applyPrefix(Component.text("ℹ " + message, NamedTextColor.GRAY));
    }

    public static Component applyPrefix(Component message) {
        if (prefix == null || prefix.equals(Component.empty())) {
            return message;
        }
        return prefix.append(Component.space()).append(message);
    }
}
