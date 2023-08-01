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

        String playerName = args[0];
        Player target = Bukkit.getPlayer(playerName);

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
            } else {
                sender.sendMessage(ChatColor.RED + "Invalid duration format. Use numbers followed by 's', 'm', 'h', or 'd' (e.g., 1d, 30m).");
            }
        } else {
            banManager.banPlayer(target.getName(), reason);
            target.kickPlayer(ChatColor.RED + "You have been banned: " + reason);
        }

        return true;
    }

    private long parseDuration(String durationString) {
        Pattern pattern = Pattern.compile("(\\d+)([smhd])");
        Matcher matcher = pattern.matcher(durationString);
        if (matcher.matches()) {
            long duration = Long.parseLong(matcher.group(1));
            String unit = matcher.group(2);
            switch (unit) {
                case "s":
                    return TimeUnit.SECONDS.toSeconds(duration);
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
}
