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

public class MuteCommand implements CommandExecutor {

    private final MuteManager muteManager;

    // Permissions
    private static final String PERMISSION_MUTE = "essentialspro.mute";

    // Constants for messages
    private static final Component NO_PERMISSION = Component.text("You don't have permission to use this command.", NamedTextColor.RED);
    private static final Component USAGE = Component.text("Usage: /mute <player> [duration]", NamedTextColor.RED);
    private static final Component PLAYER_NOT_FOUND = Component.text("Player not found.", NamedTextColor.RED);

    public MuteCommand(MuteManager muteManager) {
        this.muteManager = muteManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Check permissions
        if (!sender.hasPermission(PERMISSION_MUTE)) {
            sender.sendMessage(NO_PERMISSION);
            return true;
        }

        // Validate arguments
        if (args.length < 1) {
            sender.sendMessage(USAGE);
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(PLAYER_NOT_FOUND);
            return true;
        }

        // If the player is already muted, unmute them
        if (muteManager.isMuted(target)) {
            unmutePlayer(sender, target);
            return true;
        }

        // If not muted, mute the player for the specified or default duration
        long duration = parseDuration(args.length > 1 ? args[1] : "10m");
        mutePlayer(sender, target, duration);

        return true;
    }

    /**
     * Unmutes a currently muted player.
     */
    private void unmutePlayer(CommandSender sender, Player target) {
        muteManager.unmutePlayer(target);
        sender.sendMessage(Component.text("Player " + target.getName() + " has been unmuted.", NamedTextColor.GREEN));
        target.sendMessage(Component.text("You have been unmuted.", NamedTextColor.GREEN));
    }

    /**
     * Mutes a player for the specified duration.
     */
    private void mutePlayer(CommandSender sender, Player target, long duration) {
        muteManager.mutePlayer(target, duration);
        String formattedDuration = formatDuration(duration);
        sender.sendMessage(Component.text("Player " + target.getName() + " has been muted for " + formattedDuration + ".", NamedTextColor.GREEN));
        target.sendMessage(Component.text("You have been muted for " + formattedDuration + ".", NamedTextColor.RED));
    }

    /**
     * Parses a duration string (e.g., "5m", "2h", "1d") into seconds.
     * Defaults to 600s (10 minutes) if parsing fails.
     */
    private long parseDuration(String durationString) {
        Pattern pattern = Pattern.compile("(\\d+)([smhd])");
        Matcher matcher = pattern.matcher(durationString);
        if (matcher.matches()) {
            long duration = Long.parseLong(matcher.group(1));
            switch (matcher.group(2)) {
                case "s":
                    return duration;
                case "m":
                    return TimeUnit.MINUTES.toSeconds(duration);
                case "h":
                    return TimeUnit.HOURS.toSeconds(duration);
                case "d":
                    return TimeUnit.DAYS.toSeconds(duration);
                default:
                    return 600; // Default to 10 minutes (600 seconds)
            }
        }
        return 600; // Default if parsing fails
    }

    /**
     * Formats a duration given in seconds into a human-readable string.
     */
    private String formatDuration(long durationInSeconds) {
        long days = TimeUnit.SECONDS.toDays(durationInSeconds);
        long hours = TimeUnit.SECONDS.toHours(durationInSeconds) - TimeUnit.DAYS.toHours(days);
        long minutes = TimeUnit.SECONDS.toMinutes(durationInSeconds) - TimeUnit.HOURS.toMinutes(hours) - TimeUnit.DAYS.toMinutes(days);
        long seconds = durationInSeconds - TimeUnit.MINUTES.toSeconds(minutes) - TimeUnit.HOURS.toSeconds(hours) - TimeUnit.DAYS.toSeconds(days);

        return String.format("%d days, %d hours, %d minutes, %d seconds", days, hours, minutes, seconds);
    }
}