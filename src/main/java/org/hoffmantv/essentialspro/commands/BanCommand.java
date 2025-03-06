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
            sender.sendMessage(Component.text("✖ I'm sorry, but you do not have permission to execute this command.", NamedTextColor.RED));
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(Component.text("✖ Incorrect usage. Correct format: /" + label + " <player> <reason> [duration]", NamedTextColor.RED));
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(Component.text("✖ Unable to find player. Please ensure the player name is correct.", NamedTextColor.RED));
            return true;
        }

        boolean hasDuration = args.length >= 3;
        String reason = Arrays.stream(args, 1, hasDuration ? args.length - 1 : args.length)
                .collect(Collectors.joining(" "));

        if (hasDuration) {
            long durationInSeconds = parseDuration(args[args.length - 1]);
            if (durationInSeconds > 0) {
                banManager.banPlayerTemporarily(target, reason, durationInSeconds);
                String durationString = formatDuration(durationInSeconds);
                target.kick(Component.text("✖ You have been temporarily banned for the following reason: '"
                        + reason + "'. This ban will lift in: " + durationString, NamedTextColor.RED));
                Bukkit.broadcast(Component.text("✖ Player '" + target.getName() + "' has been temporarily banned for: '"
                        + reason + "'. This ban will lift in: " + durationString, NamedTextColor.RED));
            } else {
                sender.sendMessage(Component.text("✖ Invalid duration format. Please use numbers followed by 's' for seconds, 'm' for minutes, 'h' for hours, or 'd' for days (e.g., 1d for one day, 30m for 30 minutes).", NamedTextColor.RED));
            }
        } else {
            banManager.banPlayer(target.getName(), reason);
            target.kick(Component.text("✖ You have been permanently banned for the following reason: '"
                    + reason + "'.", NamedTextColor.RED));
            Bukkit.broadcast(Component.text("✖ Player '" + target.getName() + "' has been permanently banned for: '"
                    + reason + "'.", NamedTextColor.RED));
        }

        return true;
    }

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
            }
        }
        return -1;
    }

    private String formatDuration(long durationInSeconds) {
        long days = TimeUnit.SECONDS.toDays(durationInSeconds);
        long hours = TimeUnit.SECONDS.toHours(durationInSeconds) - TimeUnit.DAYS.toHours(days);
        long minutes = TimeUnit.SECONDS.toMinutes(durationInSeconds) - TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours(durationInSeconds));
        long seconds = durationInSeconds - TimeUnit.SECONDS.toMinutes(durationInSeconds) * 60;
        return String.format("%d days, %d hours, %d minutes, %d seconds", days, hours, minutes, seconds);
    }
}