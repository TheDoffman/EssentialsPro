package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hoffmantv.essentialspro.managers.MuteManager;
import org.hoffmantv.essentialspro.util.MessageUtils;
import org.hoffmantv.essentialspro.util.PlayerLookup;
import org.hoffmantv.essentialspro.util.Permissions;

/**
 * UnmuteCommand allows authorized users to unmute a specified player.
 * It uses Adventure API for all messages and displays clear feedback.
 */
public class UnmuteCommand implements CommandExecutor {

    private final MuteManager muteManager;

    // Predefined messages using Adventure API with Unicode symbols for consistent styling
    private static final Component NO_PERMISSION = MessageUtils.noPermission();
    private static final Component USAGE = Component.text("✖ Usage: /unmute <player>", NamedTextColor.RED);
    private static final Component PLAYER_NOT_FOUND = MessageUtils.playerNotFound();

    /**
     * Constructs an UnmuteCommand with the specified MuteManager.
     *
     * @param muteManager The MuteManager instance handling mute state.
     */
    public UnmuteCommand(MuteManager muteManager) {
        this.muteManager = muteManager;
    }

    /**
     * Processes the /unmute command.
     *
     * @param sender  The command sender.
     * @param command The command.
     * @param label   The alias used.
     * @param args    Command arguments.
     * @return true once the command is processed.
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Check for proper permission
        if (!sender.hasPermission(Permissions.UNMUTE)) {
            sender.sendMessage(NO_PERMISSION);
            return true;
        }

        // Ensure exactly one argument is provided
        if (args.length != 1) {
            sender.sendMessage(USAGE);
            return true;
        }

        // Attempt to get the target player by name
        Player target = PlayerLookup.findOnlinePlayer(args[0]);
        if (target == null) {
            sender.sendMessage(PLAYER_NOT_FOUND);
            return true;
        }

        // If the target is not muted, notify the sender
        if (!muteManager.isMuted(target)) {
            sender.sendMessage(Component.text("✖ " + target.getName() + " is not muted.", NamedTextColor.RED));
            return true;
        }

        // Unmute the target player
        muteManager.unmutePlayer(target);

        // Notify both the sender and the target about the successful unmute
        sender.sendMessage(Component.text("✔ " + target.getName() + " has been unmuted.", NamedTextColor.GREEN));
        target.sendMessage(Component.text("✔ You have been unmuted.", NamedTextColor.GREEN));

        return true;
    }
}