package org.hoffmantv.essentialspro.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hoffmantv.essentialspro.managers.HomeManager;
import org.hoffmantv.essentialspro.util.MessageUtils;
import org.hoffmantv.essentialspro.util.Permissions;

public class RenameHomeCommand implements CommandExecutor {

    private final HomeManager homeManager;

    public RenameHomeCommand(HomeManager homeManager) {
        this.homeManager = homeManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(MessageUtils.onlyPlayers());
            return true;
        }
        if (!player.hasPermission(Permissions.RENAMEHOME)) {
            player.sendMessage(MessageUtils.noPermission());
            return true;
        }
        if (args.length != 2) {
            player.sendMessage(MessageUtils.error("Usage: /renamehome <old> <new>"));
            return true;
        }

        boolean renamed = homeManager.renameHome(player.getUniqueId(), args[0], args[1]);
        if (!renamed) {
            player.sendMessage(MessageUtils.error("Unable to rename home."));
            return true;
        }
        player.sendMessage(MessageUtils.success("Home renamed."));
        return true;
    }
}
