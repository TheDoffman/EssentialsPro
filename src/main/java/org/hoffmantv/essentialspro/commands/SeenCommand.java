package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.hoffmantv.essentialspro.util.MessageUtils;
import org.hoffmantv.essentialspro.util.Permissions;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class SeenCommand implements CommandExecutor {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(ZoneId.systemDefault());

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission(Permissions.SEEN)) {
            sender.sendMessage(MessageUtils.noPermission());
            return true;
        }
        if (args.length != 1) {
            sender.sendMessage(MessageUtils.error("Usage: /seen <player>"));
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        if (!target.hasPlayedBefore() && !target.isOnline()) {
            sender.sendMessage(MessageUtils.error("Player has never joined."));
            return true;
        }

        String name = target.getName() == null ? args[0] : target.getName();
        String status = target.isOnline() ? "online" : "offline";
        String firstPlayed = formatTime(target.getFirstPlayed());
        String lastPlayed = formatTime(target.getLastPlayed());

        sender.sendMessage(MessageUtils.applyPrefix(Component.text("Seen info for ", NamedTextColor.YELLOW)
                .append(Component.text(name, NamedTextColor.GREEN))));
        sender.sendMessage(MessageUtils.applyPrefix(Component.text("Status: ", NamedTextColor.YELLOW)
                .append(Component.text(status, NamedTextColor.GREEN))));
        sender.sendMessage(MessageUtils.applyPrefix(Component.text("First played: ", NamedTextColor.YELLOW)
                .append(Component.text(firstPlayed, NamedTextColor.GREEN))));
        sender.sendMessage(MessageUtils.applyPrefix(Component.text("Last played: ", NamedTextColor.YELLOW)
                .append(Component.text(lastPlayed, NamedTextColor.GREEN))));
        return true;
    }

    private String formatTime(long epochMillis) {
        if (epochMillis <= 0) {
            return "unknown";
        }
        return FORMATTER.format(Instant.ofEpochMilli(epochMillis));
    }
}
