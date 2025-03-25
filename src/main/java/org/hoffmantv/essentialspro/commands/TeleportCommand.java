package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hoffmantv.essentialspro.EssentialsPro;

/**
 * TeleportCommand allows players to teleport to another online player's location.
 * Usage: /tp <player>
 */
public class TeleportCommand implements CommandExecutor {

    // Predefined messages using Adventure API with Unicode symbols.
    private static final Component ONLY_PLAYERS_ERROR = Component.text("✖ This command can only be used by players.", NamedTextColor.RED);
    private static final Component PERMISSION_ERROR = Component.text("✖ You don't have permission to use this command.", NamedTextColor.RED);
    private static final Component USAGE_ERROR = Component.text("✖ Usage: /tp <player>", NamedTextColor.RED);
    private static final Component PLAYER_NOT_FOUND_ERROR = Component.text("✖ Player not found or not online.", NamedTextColor.RED);
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
        if (!player.hasPermission("essentialspro.teleport")) {
            player.sendMessage(PERMISSION_ERROR);
            return true;
        }

        // Validate that a target player name is provided
        if (args.length == 0) {
            player.sendMessage(USAGE_ERROR);
            return true;
        }

        // Attempt to retrieve the target player using an exact match
        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null || !target.isOnline()) {
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