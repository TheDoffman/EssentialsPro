package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hoffmantv.essentialspro.managers.MuteManager;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * MuteCommand toggles a player's mute state.
 * If the player is already muted, they are unmuted.
 * Otherwise, the player is muted for a specified duration (default is 10 minutes).
 */
public class MuteCommand implements CommandExecutor {

    private final MuteManager muteManager;

    // Permission node for muting
    private static final String PERMISSION_MUTE = "essentialspro.mute";

    // Pre-built messages using Adventure API with Unicode symbols
    private static final Component NO_PERMISSION = Component.text("✖ You don't have permission to use this command.", NamedTextColor.RED);
    private static final Component USAGE = Component.text("✖ Usage: /mute <player> [duration]", NamedTextColor.RED);
    private static final Component PLAYER_NOT_FOUND = Component.text("✖ Player not found.", NamedTextColor.RED);

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
        Player target = Bukkit.getPlayer(args[0]);
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
        long duration = parseDuration(args.length > 1 ? args[1] : "10m");
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
        String formattedDuration = formatDuration(duration);
        sender.sendMessage(Component.text("✔ " + target.getName() + " has been muted for " + formattedDuration + ".", NamedTextColor.GREEN));
        target.sendMessage(Component.text("✖ You have been muted for " + formattedDuration + ".", NamedTextColor.RED));
    }

    /**
     * Parses a duration string (e.g., "5m", "2h", "1d") into seconds.
     * Defaults to 600 seconds (10 minutes) if parsing fails.
     *
     * @param durationString The duration string.
     * @return The duration in seconds.
     */
    private long parseDuration(String durationString) {
        Pattern pattern = Pattern.compile("(\\d+)([smhd])");
        Matcher matcher = pattern.matcher(durationString);
        if (matcher.matches()) {
            long duration = Long.parseLong(matcher.group(1));
            String unit = matcher.group(2);
            switch (unit) {
                case "s":
                    return duration;
                case "m":
                    return TimeUnit.MINUTES.toSeconds(duration);
                case "h":
                    return TimeUnit.HOURS.toSeconds(duration);
                case "d":
                    return TimeUnit.DAYS.toSeconds(duration);
                default:
                    return 600; // Default to 10 minutes
            }
        }
        return 600; // Default if parsing fails
    }

    /**
     * Formats a duration (in seconds) into a human-readable string.
     *
     * @param durationInSeconds The duration in seconds.
     * @return A formatted string (e.g., "0 days, 0 hours, 10 minutes, 0 seconds").
     */
    private String formatDuration(long durationInSeconds) {
        long days = durationInSeconds / 86400;
        long hours = (durationInSeconds % 86400) / 3600;
        long minutes = (durationInSeconds % 3600) / 60;
        long seconds = durationInSeconds % 60;
        return String.format("%d days, %d hours, %d minutes, %d seconds", days, hours, minutes, seconds);
    }
}