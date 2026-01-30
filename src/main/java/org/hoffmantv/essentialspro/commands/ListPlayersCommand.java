package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hoffmantv.essentialspro.util.MessageUtils;
import org.hoffmantv.essentialspro.util.Permissions;

import java.util.stream.Collectors;

/**
 * Displays a list of online players along with a header showing the current count.
 * Only players with the "essentialspro.listplayers" permission may use this command.
 */
public class ListPlayersCommand implements CommandExecutor {

    private static final String PERMISSION = Permissions.LISTPLAYERS;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Ensure the command sender is a player
        if (!(sender instanceof Player)) {
            sender.sendMessage(MessageUtils.onlyPlayers());
            return true;
        }
        Player player = (Player) sender;

        // Check if the player has permission to use the command
        if (!player.hasPermission(PERMISSION)) {
            player.sendMessage(MessageUtils.noPermission());
            return true;
        }

        // Build a comma-separated list of online players
        String onlinePlayers = Bukkit.getOnlinePlayers().stream()
                .map(Player::getDisplayName)
                .collect(Collectors.joining(", "));

        if (onlinePlayers.isEmpty()) {
            player.sendMessage(Component.text("✖ No players are currently online.", NamedTextColor.RED));
            return true;
        }

        // Send the header message
        player.sendMessage(createHeader());

        // Display each player on a new line with a bullet symbol
        for (String name : onlinePlayers.split(", ")) {
            player.sendMessage(Component.text("• ", NamedTextColor.GREEN)
                    .append(Component.text(name, NamedTextColor.GREEN)));
        }

        return true;
    }

    /**
     * Creates a formatted header displaying the number of online players.
     *
     * @return The header as an Adventure Component.
     */
    private Component createHeader() {
        int onlineCount = Bukkit.getOnlinePlayers().size();
        int maxPlayers = Bukkit.getMaxPlayers();
        return Component.text("📜 ")
                .append(Component.text("Online Players ", NamedTextColor.GOLD))
                .append(Component.text("(" + onlineCount + "/" + maxPlayers + ")", NamedTextColor.YELLOW))
                .append(Component.text(" 📜", NamedTextColor.GOLD));
    }
}