package org.hoffmantv.essentialspro.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
            sender.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /" + label + " <player> <reason> [duration]");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player not found.");
            return true;
        }

        boolean hasDuration = args.length >= 3;
        String reason = Arrays.stream(args, 1, hasDuration ? args.length - 1 : args.length).collect(Collectors.joining(" "));

        if (hasDuration) {
            long durationInSeconds = parseDuration(args[args.length - 1]);
            if (durationInSeconds > 0) {
                banManager.banPlayerTemporarily(target, reason, durationInSeconds);
                String durationString = formatDuration(durationInSeconds);
                target.kickPlayer(ChatColor.RED + "You have been temporarily banned for: " + reason + " Duration: " + durationString);
                Bukkit.broadcastMessage(ChatColor.RED + "Player " + target.getName() + " has been temporarily banned for: " + reason + " Duration: " + durationString);
            } else {
                sender.sendMessage(ChatColor.RED + "Invalid duration format. Use numbers followed by 's', 'm', 'h', or 'd' (e.g., 1d, 30m).");
            }
        } else {
            banManager.banPlayer(target.getName(), reason);
            target.kickPlayer(ChatColor.RED + "You have been banned: " + reason);
            Bukkit.broadcastMessage(ChatColor.RED + "Player " + target.getName() + " has been banned for: " + reason);
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
        long hours = TimeUnit.SECONDS.toHours(durationInSeconds - TimeUnit.DAYS.toSeconds(days));
        long minutes = TimeUnit.SECONDS.toMinutes(durationInSeconds - TimeUnit.DAYS.toSeconds(days) - TimeUnit.HOURS.toSeconds(hours));
        long seconds = durationInSeconds - TimeUnit.DAYS.toSeconds(days) - TimeUnit.HOURS.toSeconds(hours) - TimeUnit.MINUTES.toSeconds(minutes);

        return String.format("%d days, %d hours, %d minutes, %d seconds", days, hours, minutes, seconds);
    }
}
