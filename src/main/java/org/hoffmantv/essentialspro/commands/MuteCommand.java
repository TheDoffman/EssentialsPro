package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hoffmantv.essentialspro.managers.MuteManager;
import org.hoffmantv.essentialspro.util.DurationUtils;
import org.hoffmantv.essentialspro.util.MessageUtils;
import org.hoffmantv.essentialspro.util.PlayerLookup;
import org.hoffmantv.essentialspro.util.Permissions;

/**
 * MuteCommand toggles a player's mute state.
 * If the player is already muted, they are unmuted.
 * Otherwise, the player is muted for a specified duration (default is 10 minutes).
 */
public class MuteCommand implements CommandExecutor {

    private final MuteManager muteManager;

    // Permission node for muting
    private static final String PERMISSION_MUTE = Permissions.MUTE;

    // Pre-built messages using Adventure API with Unicode symbols
    private static final Component NO_PERMISSION = MessageUtils.noPermission();
    private static final Component USAGE = Component.text("✖ Usage: /mute <player> [duration]", NamedTextColor.RED);
    private static final Component PLAYER_NOT_FOUND = MessageUtils.playerNotFound();

    /**
     * Constructs a new MuteCommand with the provided MuteManager.
     *
     * @param muteManager The manager handling mute state.
     */
    public MuteCommand(MuteManager muteManager) {
        this.muteManager = muteManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Check if the sender has permission to mute
        if (!sender.hasPermission(PERMISSION_MUTE)) {
            sender.sendMessage(NO_PERMISSION);
            return true;
        }

        // Validate arguments: at least a target player must be provided
        if (args.length < 1) {
            sender.sendMessage(USAGE);
            return true;
        }

        // Attempt to retrieve the target player
        Player target = PlayerLookup.findOnlinePlayer(args[0]);
        if (target == null) {
            sender.sendMessage(PLAYER_NOT_FOUND);
            return true;
        }

        // If the target is already muted, unmute them
        if (muteManager.isMuted(target)) {
            unmutePlayer(sender, target);
            return true;
        }

        // Otherwise, mute the player with the specified duration or default to 10 minutes
        long duration = DurationUtils.parseDurationSeconds(args.length > 1 ? args[1] : "10m", 600);
        mutePlayer(sender, target, duration);
        return true;
    }

    /**
     * Unmutes the specified player.
     *
     * @param sender The command sender.
     * @param target The player to unmute.
     */
    private void unmutePlayer(CommandSender sender, Player target) {
        muteManager.unmutePlayer(target);
        sender.sendMessage(Component.text("✔ " + target.getName() + " has been unmuted.", NamedTextColor.GREEN));
        target.sendMessage(Component.text("✔ You have been unmuted.", NamedTextColor.GREEN));
    }

    /**
     * Mutes the specified player for the given duration.
     *
     * @param sender   The command sender.
     * @param target   The player to mute.
     * @param duration Duration in seconds.
     */
    private void mutePlayer(CommandSender sender, Player target, long duration) {
        muteManager.mutePlayer(target, duration);
        String formattedDuration = DurationUtils.formatDuration(duration);
        sender.sendMessage(Component.text("✔ " + target.getName() + " has been muted for " + formattedDuration + ".", NamedTextColor.GREEN));
        target.sendMessage(Component.text("✖ You have been muted for " + formattedDuration + ".", NamedTextColor.RED));
    }
}