package org.hoffmantv.essentialspro.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.hoffmantv.essentialspro.managers.WarningManager;
import org.hoffmantv.essentialspro.util.MessageUtils;
import org.hoffmantv.essentialspro.util.Permissions;

public class ClearWarningsCommand implements CommandExecutor {

    private final WarningManager warningManager;

    public ClearWarningsCommand(WarningManager warningManager) {
        this.warningManager = warningManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission(Permissions.CLEARWARNINGS)) {
            sender.sendMessage(MessageUtils.noPermission());
            return true;
        }
        if (args.length != 1) {
            sender.sendMessage(MessageUtils.error("Usage: /clearwarnings <player>"));
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        if (!target.hasPlayedBefore() && !target.isOnline()) {
            sender.sendMessage(MessageUtils.error("Player has never joined."));
            return true;
        }

        warningManager.clearWarnings(target.getUniqueId());
        sender.sendMessage(MessageUtils.success("Warnings cleared for " + args[0] + "."));
        return true;
    }
}
