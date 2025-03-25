package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.hoffmantv.essentialspro.managers.BanManager;

/**
 * UnbanCommand allows authorized users to unban a player.
 * It requires exactly one argument (the player's name) and
 * sends clear feedback to the command sender.
 */
public class UnbanCommand implements CommandExecutor {

    private final BanManager banManager;

    // Predefined messages using Adventure API with Unicode symbols
    private static final Component NO_PERMISSION = Component.text("✖ You don't have permission to use this command.", NamedTextColor.RED);
    private static final Component USAGE = Component.text("✖ Usage: /unban <player>", NamedTextColor.RED);
    private static final Component PLAYER_NOT_FOUND = Component.text("✖ Player not found or not banned.", NamedTextColor.RED);
    private static final Component UNBAN_SUCCESS_PREFIX = Component.text("✔ Player ", NamedTextColor.GREEN);
    private static final Component UNBAN_SUCCESS_SUFFIX = Component.text(" has been unbanned.", NamedTextColor.GREEN);

    /**
     * Constructs a new UnbanCommand.
     *
     * @param banManager The BanManager instance to handle unban operations.
     */
    public UnbanCommand(BanManager banManager) {
        this.banManager = banManager;
    }

    /**
     * Executes the /unban command.
     * It unbans the specified player and notifies the sender.
     *
     * @param sender  The command sender.
     * @param command The command.
     * @param label   The alias used.
     * @param args    The command arguments.
     * @return true after processing.
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Check for permission
        if (!sender.hasPermission("essentialspro.unban")) {
            sender.sendMessage(NO_PERMISSION);
            return true;
        }

        // Ensure exactly one argument is provided
        if (args.length != 1) {
            sender.sendMessage(USAGE);
            return true;
        }

        String playerName = args[0];

        try {
            // Attempt to unban the player using the BanManager
            banManager.unbanPlayer(playerName);
            // Build and send a success message
            Component successMessage = UNBAN_SUCCESS_PREFIX
                    .append(Component.text(playerName, NamedTextColor.GREEN))
                    .append(UNBAN_SUCCESS_SUFFIX);
            sender.sendMessage(successMessage);
        } catch (IllegalArgumentException e) {
            // Send error message if unban fails (e.g. player is not banned)
            sender.sendMessage(PLAYER_NOT_FOUND.append(Component.text(" " + e.getMessage(), NamedTextColor.RED)));
        }

        return true;
    }
}