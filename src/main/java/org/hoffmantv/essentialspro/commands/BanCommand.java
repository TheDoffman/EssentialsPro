package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
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

    public BanCommand(BanManager banManager) {
        this.banManager = banManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("essentialspro.ban")) {
            sender.sendMessage(Component.text("✖ You do not have permission to execute this command.").color(NamedTextColor.RED));
            return true;
        }

        if (args.length < 2) {
            sendUsage(sender, label);
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(Component.text("✖ Unable to find player.").color(NamedTextColor.RED));
            return true;
        }

        if (banManager.isBanned(target.getName())) {
            sender.sendMessage(Component.text("✖ Player is already banned.").color(NamedTextColor.RED));
            return true;
        }

        String reason = extractReason(args);
        if (args.length >= 3) {
            handleTemporaryBan(sender, target, reason, args[args.length - 1]);
        } else {
            handlePermanentBan(target, reason);
        }

        return true;
    }

    private void sendUsage(CommandSender sender, String label) {
        sender.sendMessage(Component.text("✖ Usage: /" + label + " <player> <reason> [duration]").color(NamedTextColor.RED));
    }

    private String extractReason(String[] args) {
        boolean hasDuration = args.length >= 3;
        return Arrays.stream(args, 1, hasDuration ? args.length - 1 : args.length)
                .collect(Collectors.joining(" "));
    }

    private void handlePermanentBan(Player target, String reason) {
        banManager.banPlayer(target.getName(), reason);
        target.kickPlayer(Component.text("✖ You have been permanently banned for: " + reason).color(NamedTextColor.RED).toString());
        Bukkit.broadcast(Component.text("⚠ Player '" + target.getName() + "' has been permanently banned for: " + reason).color(NamedTextColor.RED));
    }

    private void handleTemporaryBan(CommandSender sender, Player target, String reason, String durationString) {
        long durationInSeconds = parseDuration(durationString);
        if (durationInSeconds > 0) {
            banManager.banPlayerTemporarily(target, reason, durationInSeconds);
            String formattedDuration = formatDuration(durationInSeconds);
            target.kickPlayer(Component.text("✖ You have been temporarily banned for: " + reason + ". Lifts in: " + formattedDuration).color(NamedTextColor.RED).toString());
            Bukkit.broadcast(Component.text("⚠ Player '" + target.getName() + "' has been temporarily banned for: " + reason + ". Lifts in: " + formattedDuration).color(NamedTextColor.RED));
        } else {
            sender.sendMessage(Component.text("✖ Invalid duration. Use 's' for seconds, 'm' for minutes, 'h' for hours, 'd' for days. E.g., 1d or 30m.").color(NamedTextColor.RED));
        }
    }

    private long parseDuration(String durationString) {
        Pattern pattern = Pattern.compile("(\\d+)([smhd])");
        Matcher matcher = pattern.matcher(durationString);
        if (matcher.matches()) {
            long duration = Long.parseLong(matcher.group(1));
            switch (matcher.group(2)) {
                case "s": return duration;
                case "m": return TimeUnit.MINUTES.toSeconds(duration);
                case "h": return TimeUnit.HOURS.toSeconds(duration);
                case "d": return TimeUnit.DAYS.toSeconds(duration);
            }
        }
        return -1;
    }

    private String formatDuration(long durationInSeconds) {
        long days = TimeUnit.SECONDS.toDays(durationInSeconds);
        long hours = TimeUnit.SECONDS.toHours(durationInSeconds - TimeUnit.DAYS.toSeconds(days));
        long minutes = TimeUnit.SECONDS.toMinutes(durationInSeconds - TimeUnit.DAYS.toSeconds(days) - TimeUnit.HOURS.toSeconds(hours));
        long seconds = durationInSeconds - TimeUnit.DAYS.toSeconds(days) - TimeUnit.HOURS.toSeconds(hours) - TimeUnit.MINUTES.toSeconds(minutes);

        return String.format("%d days, %d hours, %d minutes, %d seconds", days, hours, minutes, seconds);
    }
}