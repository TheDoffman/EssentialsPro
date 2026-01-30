package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hoffmantv.essentialspro.EssentialsPro;
import org.hoffmantv.essentialspro.util.MessageUtils;
import org.hoffmantv.essentialspro.util.PlayerLookup;
import org.hoffmantv.essentialspro.util.Permissions;

/**
 * TeleportCommand allows players to teleport to another online player's location.
 * Usage: /tp <player>
 */
public class TeleportCommand implements CommandExecutor {

    // Predefined messages using Adventure API with Unicode symbols.
    private static final Component ONLY_PLAYERS_ERROR = MessageUtils.onlyPlayers();
    private static final Component PERMISSION_ERROR = MessageUtils.noPermission();
    private static final Component USAGE_ERROR = Component.text("✖ Usage: /tp <player>", NamedTextColor.RED);
    private static final Component PLAYER_NOT_FOUND_ERROR = MessageUtils.playerNotFound();
    // Success message color (and an icon)
    private static final NamedTextColor SUCCESS_COLOR = NamedTextColor.GREEN;

    private final EssentialsPro plugin;

    /**
     * Constructs a new TeleportCommand.
     *
     * @param plugin The main plugin instance.
     */
    public TeleportCommand(EssentialsPro plugin) {
        this.plugin = plugin;
    }

    /**
     * Executes the /tp command. Teleports the sender to the target player's location.
     *
     * @param sender  The command sender.
     * @param command The command.
     * @param label   The alias used.
     * @param args    The command arguments.
     * @return true after execution.
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Ensure sender is a player
        if (!(sender instanceof Player)) {
            sender.sendMessage(ONLY_PLAYERS_ERROR);
            return true;
        }

        Player player = (Player) sender;

        // Check for proper permission
        if (!player.hasPermission(Permissions.TELEPORT)) {
            player.sendMessage(PERMISSION_ERROR);
            return true;
        }

        // Validate that a target player name is provided
        if (args.length == 0) {
            player.sendMessage(USAGE_ERROR);
            return true;
        }

        // Attempt to retrieve the target player using an exact match
        Player target = PlayerLookup.findOnlinePlayer(args[0]);
        if (target == null) {
            player.sendMessage(PLAYER_NOT_FOUND_ERROR);
            return true;
        }

        // Teleport the sender to the target's location
        player.teleport(target.getLocation());
        // Send a success message including a location marker icon "📍"
        player.sendMessage(Component.text("✔ You have been teleported to ", SUCCESS_COLOR)
                .append(Component.text(target.getName(), SUCCESS_COLOR))
                .append(Component.text(" 📍", SUCCESS_COLOR)));
        return true;
    }
}