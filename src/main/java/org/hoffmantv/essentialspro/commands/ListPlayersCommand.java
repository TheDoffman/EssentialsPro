package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;

import java.util.stream.Collectors;

public class ListPlayersCommand implements CommandExecutor {

    // Optional: Add a permission node for listing players if desired
    private static final String PERMISSION = "essentialspro.listplayers";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Check if sender is a player
        if (!(sender instanceof Player)) {
            sender.sendMessage(Component.text("✖ This command can only be used by players.", NamedTextColor.RED));
            return true;
        }

        Player player = (Player) sender;

        // Optional: Check permission
        if (!player.hasPermission(PERMISSION)) {
            player.sendMessage(Component.text("✖ You don't have permission to use this command.", NamedTextColor.RED));
            return true;
        }

        // Gather online players
        String onlinePlayers = getOnlinePlayerNames();
        if (onlinePlayers.isEmpty()) {
            player.sendMessage(Component.text("✖ No players are currently online.", NamedTextColor.RED));
            return true;
        }

        // Send header
        int onlineCount = Bukkit.getOnlinePlayers().size();
        int maxPlayers = Bukkit.getMaxPlayers();
        Component header = Component.text("---- ", NamedTextColor.YELLOW)
                .append(Component.text("Online Players ", NamedTextColor.GOLD))
                .append(Component.text("(" + onlineCount + "/" + maxPlayers + ")", NamedTextColor.YELLOW))
                .append(Component.text(" ----", NamedTextColor.YELLOW));
        player.sendMessage(header);

        // Display the list of online players
        // Instead of a single line, let’s give each player on a new line for clarity
        for (String playerName : onlinePlayers.split(", ")) {
            player.sendMessage(Component.text("• ", NamedTextColor.GREEN)
                    .append(Component.text(playerName, NamedTextColor.GREEN)));
        }

        return true;
    }

    /**
     * Retrieves a comma-separated list of online player display names.
     */
    private String getOnlinePlayerNames() {
        return Bukkit.getOnlinePlayers().stream()
                .map(Player::getDisplayName)
                .collect(Collectors.joining(", "));
    }
}