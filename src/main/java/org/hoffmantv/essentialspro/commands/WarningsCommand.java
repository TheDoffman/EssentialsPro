package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hoffmantv.essentialspro.managers.WarningManager;
import org.hoffmantv.essentialspro.util.MessageUtils;
import org.hoffmantv.essentialspro.util.Permissions;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class WarningsCommand implements CommandExecutor {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(ZoneId.systemDefault());

    private final WarningManager warningManager;

    public WarningsCommand(WarningManager warningManager) {
        this.warningManager = warningManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission(Permissions.WARNINGS)) {
            sender.sendMessage(MessageUtils.noPermission());
            return true;
        }

        OfflinePlayer target;
        if (args.length == 0) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage(MessageUtils.error("Usage: /warnings <player>"));
                return true;
            }
            target = player;
        } else {
            if (!sender.hasPermission(Permissions.WARNINGS_OTHERS)) {
                sender.sendMessage(MessageUtils.noPermission());
                return true;
            }
            target = Bukkit.getOfflinePlayer(args[0]);
        }

        if (target == null || (!target.hasPlayedBefore() && !target.isOnline())) {
            sender.sendMessage(MessageUtils.error("Player has never joined."));
            return true;
        }

        List<String> warnings = warningManager.getWarnings(target.getUniqueId());
        if (warnings.isEmpty()) {
            sender.sendMessage(MessageUtils.info("No warnings found."));
            return true;
        }

        sender.sendMessage(MessageUtils.applyPrefix(Component.text("Warnings for ", NamedTextColor.YELLOW)
                .append(Component.text(target.getName() == null ? "Player" : target.getName(), NamedTextColor.GREEN))));
        for (String entry : warnings) {
            String[] parts = entry.split("\\|", 3);
            String time = parts.length > 0 ? formatTime(parts[0]) : "unknown";
            String actor = parts.length > 1 ? parts[1] : "Console";
            String reason = parts.length > 2 ? parts[2] : "No reason";
            sender.sendMessage(MessageUtils.applyPrefix(Component.text("- [" + time + "] ", NamedTextColor.GRAY)
                    .append(Component.text(actor + ": ", NamedTextColor.YELLOW))
                    .append(Component.text(reason, NamedTextColor.WHITE))));
        }
        return true;
    }

    private String formatTime(String millis) {
        try {
            long value = Long.parseLong(millis);
            if (value <= 0) {
                return "unknown";
            }
            return FORMATTER.format(Instant.ofEpochMilli(value));
        } catch (NumberFormatException e) {
            return "unknown";
        }
    }
}
