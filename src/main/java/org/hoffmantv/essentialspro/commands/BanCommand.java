package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hoffmantv.essentialspro.managers.BanManager;
import org.hoffmantv.essentialspro.util.DurationUtils;
import org.hoffmantv.essentialspro.util.PlayerLookup;
import org.hoffmantv.essentialspro.util.Permissions;

import java.util.Arrays;
import java.util.stream.Collectors;

public class BanCommand implements CommandExecutor {

    private final BanManager banManager;

    public BanCommand(BanManager banManager) {
        this.banManager = banManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Permission check
        if (!sender.hasPermission(Permissions.BAN)) {
            sender.sendMessage(Component.text("✖ I'm sorry, but you do not have permission to execute this command.", NamedTextColor.RED));
            return true;
        }

        // Validate arguments
        if (args.length < 2) {
            sender.sendMessage(Component.text("✖ Incorrect usage. Correct format: /" + label + " <player> <reason> [duration]", NamedTextColor.RED));
            return true;
        }

        // Get target player
        Player target = PlayerLookup.findOnlinePlayer(args[0]);
        if (target == null) {
            sender.sendMessage(Component.text("✖ Unable to find player. Please ensure the player name is correct.", NamedTextColor.RED));
            return true;
        }

        // Determine if a duration was provided and extract the reason accordingly
        boolean hasDuration = args.length >= 3;
        String reason = Arrays.stream(args, 1, hasDuration ? args.length - 1 : args.length)
                .collect(Collectors.joining(" "));

        if (hasDuration) {
            long durationInSeconds = DurationUtils.parseDurationSeconds(args[args.length - 1], -1);
            if (durationInSeconds > 0) {
                banManager.banPlayerTemporarily(target, reason, durationInSeconds);
                String durationString = DurationUtils.formatDuration(durationInSeconds);
                Component kickMessage = Component.text("✖ You have been temporarily banned for: '" + reason + "'. This ban will lift in: " + durationString, NamedTextColor.RED);
                target.kick(kickMessage);
                Bukkit.broadcast(Component.text("✖ Player '" + target.getName() + "' has been temporarily banned for: '" + reason + "'. This ban will lift in: " + durationString, NamedTextColor.RED));
            } else {
                sender.sendMessage(Component.text("✖ Invalid duration format. Please use numbers followed by 's' (seconds), 'm' (minutes), 'h' (hours), or 'd' (days).", NamedTextColor.RED));
            }
        } else {
            banManager.banPlayer(target.getName(), reason);
            target.kick(Component.text("✖ You have been permanently banned for: '" + reason + "'.", NamedTextColor.RED));
            Bukkit.broadcast(Component.text("✖ Player '" + target.getName() + "' has been permanently banned for: '" + reason + "'.", NamedTextColor.RED));
        }

        return true;
    }
}