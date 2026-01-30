package org.hoffmantv.essentialspro.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.hoffmantv.essentialspro.managers.WarningManager;
import org.hoffmantv.essentialspro.util.MessageUtils;
import org.hoffmantv.essentialspro.util.Permissions;

import java.util.Arrays;
import java.util.stream.Collectors;

public class WarnCommand implements CommandExecutor {

    private final WarningManager warningManager;

    public WarnCommand(WarningManager warningManager) {
        this.warningManager = warningManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission(Permissions.WARN)) {
            sender.sendMessage(MessageUtils.noPermission());
            return true;
        }
        if (args.length < 2) {
            sender.sendMessage(MessageUtils.error("Usage: /warn <player> <reason>"));
            return true;
        }

        String targetName = args[0];
        OfflinePlayer target = Bukkit.getOfflinePlayer(targetName);
        if (!target.hasPlayedBefore() && !target.isOnline()) {
            sender.sendMessage(MessageUtils.error("Player has never joined."));
            return true;
        }

        String reason = Arrays.stream(args, 1, args.length).collect(Collectors.joining(" "));
        warningManager.addWarning(target.getUniqueId(), sender.getName(), reason, System.currentTimeMillis());
        sender.sendMessage(MessageUtils.success("Warning issued to " + targetName + "."));
        if (target.isOnline() && target.getPlayer() != null) {
            target.getPlayer().sendMessage(MessageUtils.error("You have been warned: " + reason));
        }
        return true;
    }
}
