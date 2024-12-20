package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hoffmantv.essentialspro.managers.BanManager;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class BanCommand implements CommandExecutor {

    private final BanManager banManager;
    private static final Component NO_PERMISSION = Component.text("✖ You do not have permission to execute this command.", NamedTextColor.RED);
    private static final Component USAGE = Component.text("✖ Usage: /ban <player> <reason> [duration]", NamedTextColor.RED);
    private static final Component PLAYER_NOT_FOUND = Component.text("✖ Unable to find player.", NamedTextColor.RED);
    private static final Component ALREADY_BANNED = Component.text("✖ Player is already banned.", NamedTextColor.RED);
    private static final Component INVALID_DURATION = Component.text("✖ Invalid duration. Use 's' for seconds, 'm' for minutes, 'h' for hours, 'd' for days (e.g., 1d or 30m).", NamedTextColor.RED);

    public BanCommand(BanManager banManager) {
        this.banManager = banManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("essentialspro.ban")) {
            sender.sendMessage(NO_PERMISSION);
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(USAGE);
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(PLAYER_NOT_FOUND);
            return true;
        }

        if (banManager.isBanned(target.getName())) {
            sender.sendMessage(ALREADY_BANNED);
            return true;
        }

        String reason = extractReason(args);
        boolean hasDuration = args.length >= 3;
        if (hasDuration) {
            handleTemporaryBan(sender, target, reason, args[args.length - 1]);
        } else {
            handlePermanentBan(sender, target, reason);
        }

        return true;
    }

    private String extractReason(String[] args) {
        boolean hasDuration = args.length >= 3;
        // Extract reason from everything except the last arg if there's a duration
        return Arrays.stream(args, 1, hasDuration ? args.length - 1 : args.length)
                .collect(Collectors.joining(" "));
    }

    private void handlePermanentBan(CommandSender sender, Player target, String reason) {
        banManager.banPlayer(target.getName(), reason);

        // Kick the player with a permanent ban message
        Component banMsg = Component.text("✖ You have been permanently banned for: " + reason, NamedTextColor.RED);
        target.kickPlayer(LegacyComponentSerializer.legacySection().serialize(banMsg));

        // Broadcast the ban
        Component broadcastMsg = Component.text("⚠ Player '" + target.getName() + "' has been permanently banned for: " + reason, NamedTextColor.RED);
        Bukkit.broadcast(broadcastMsg);

        sender.sendMessage(Component.text("✔ Player " + target.getName() + " has been permanently banned.", NamedTextColor.GREEN));
    }

    private void handleTemporaryBan(CommandSender sender, Player target, String reason, String durationString) {
        long durationInSeconds = parseDuration(durationString);
        if (durationInSeconds <= 0) {
            sender.sendMessage(INVALID_DURATION);
            return;
        }

        banManager.banPlayerTemporarily(target, reason, durationInSeconds);
        String formattedDuration = formatDuration(durationInSeconds);

        Component banMsg = Component.text("✖ You have been temporarily banned for: " + reason + ". Lifts in: " + formattedDuration, NamedTextColor.RED);
        target.kickPlayer(LegacyComponentSerializer.legacySection().serialize(banMsg));

        Component broadcastMsg = Component.text("⚠ Player '" + target.getName() + "' has been temporarily banned for: " + reason + ". Lifts in: " + formattedDuration, NamedTextColor.RED);
        Bukkit.broadcast(broadcastMsg);

        sender.sendMessage(Component.text("✔ Player " + target.getName() + " has been temporarily banned for " + formattedDuration + ".", NamedTextColor.GREEN));
    }

    private long parseDuration(String durationString) {
        Pattern pattern = Pattern.compile("(\\d+)([smhd])");
        Matcher matcher = pattern.matcher(durationString);
        if (matcher.matches()) {
            long duration = Long.parseLong(matcher.group(1));
            char unit = matcher.group(2).charAt(0);
            switch (unit) {
                case 's': return duration;
                case 'm': return TimeUnit.MINUTES.toSeconds(duration);
                case 'h': return TimeUnit.HOURS.toSeconds(duration);
                case 'd': return TimeUnit.DAYS.toSeconds(duration);
                default: return -1;
            }
        }
        return -1; // Invalid format
    }

    private String formatDuration(long durationInSeconds) {
        long days = TimeUnit.SECONDS.toDays(durationInSeconds);
        long hours = TimeUnit.SECONDS.toHours(durationInSeconds) % 24;
        long minutes = TimeUnit.SECONDS.toMinutes(durationInSeconds) % 60;
        long seconds = durationInSeconds % 60;

        return String.format("%d days, %d hours, %d minutes, %d seconds", days, hours, minutes, seconds);
    }
}