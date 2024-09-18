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

    public MuteCommand(MuteManager muteManager) {
        this.muteManager = muteManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("essentialspro.mute")) {
            sender.sendMessage(Component.text("You don't have permission to use this command.", NamedTextColor.RED));
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage(Component.text("Usage: /mute <player> [duration]", NamedTextColor.RED));
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(Component.text("Player not found.", NamedTextColor.RED));
            return true;
        }

        if (muteManager.isMuted(target)) {
            muteManager.unmutePlayer(target);
            sender.sendMessage(Component.text("Player " + target.getName() + " has been unmuted.", NamedTextColor.GREEN));
            target.sendMessage(Component.text("You have been unmuted.", NamedTextColor.GREEN));
            return true;
        }

        long duration = parseDuration(args.length > 1 ? args[1] : "10m"); // Default to 10 minutes if no duration is provided
        muteManager.mutePlayer(target, duration);
        sender.sendMessage(Component.text("Player " + target.getName() + " has been muted for " + formatDuration(duration) + ".", NamedTextColor.GREEN));
        target.sendMessage(Component.text("You have been muted for " + formatDuration(duration) + ".", NamedTextColor.RED));

        return true;
    }

    // Parse the duration string (e.g., "5m", "2h", "1d") into seconds
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
        return 600; // Default to 10 minutes (600 seconds)
    }

    // Format the duration into a human-readable format
    private String formatDuration(long durationInSeconds) {
        long days = TimeUnit.SECONDS.toDays(durationInSeconds);
        long hours = TimeUnit.SECONDS.toHours(durationInSeconds) - TimeUnit.DAYS.toHours(days);
        long minutes = TimeUnit.SECONDS.toMinutes(durationInSeconds) - TimeUnit.HOURS.toMinutes(hours) - TimeUnit.DAYS.toMinutes(days);
        long seconds = durationInSeconds - TimeUnit.MINUTES.toSeconds(minutes) - TimeUnit.HOURS.toSeconds(hours) - TimeUnit.DAYS.toSeconds(days);

        return String.format("%d days, %d hours, %d minutes, %d seconds", days, hours, minutes, seconds);
    }
}