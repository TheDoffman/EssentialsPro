package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hoffmantv.essentialspro.EssentialsPro;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * AfkCommand allows players to toggle their AFK status.
 * When a player goes AFK, their original display name is stored and their name is prefixed with "[AFK] ".
 * When they toggle off AFK, the original name is restored.
 */
public class AFKCommand implements CommandExecutor {

    private final EssentialsPro plugin;
    private final Map<UUID, String> originalNames = new HashMap<>();
    private final LegacyComponentSerializer legacySerializer = LegacyComponentSerializer.legacyAmpersand();

    public AFKCommand(EssentialsPro plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Ensure the sender is a player
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("✖ This command can only be used by players.", NamedTextColor.RED));
            return true;
        }

        UUID uuid = player.getUniqueId();
        if (originalNames.containsKey(uuid)) {
            // Player is currently AFK: restore their original display name
            String originalName = originalNames.get(uuid);
            player.displayName(Component.text(originalName, NamedTextColor.WHITE));
            originalNames.remove(uuid);
            player.sendMessage(Component.text("✔ You are no longer AFK.", NamedTextColor.GREEN));
        } else {
            // Store the player's current display name (using a legacy serializer if needed)
            String currentDisplay = (player.displayName() != null && !player.displayName().equals(Component.empty()))
                    ? legacySerializer.serialize(player.displayName())
                    : player.getName();
            originalNames.put(uuid, currentDisplay);
            // Set the player's display name to indicate AFK status
            player.displayName(Component.text("[AFK] " + currentDisplay, NamedTextColor.GRAY));
            player.sendMessage(Component.text("✔ You are now AFK.", NamedTextColor.GRAY));
        }
        return true;
    }
}