package org.hoffmantv.essentialspro.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hoffmantv.essentialspro.managers.WarpManager;
import org.hoffmantv.essentialspro.util.MessageUtils;
import org.hoffmantv.essentialspro.util.Permissions;

public class SetWarpCommand implements CommandExecutor {

    private final WarpManager warpManager;

    public SetWarpCommand(WarpManager warpManager) {
        this.warpManager = warpManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(MessageUtils.onlyPlayers());
            return true;
        }
        if (!player.hasPermission(Permissions.SETWARP)) {
            player.sendMessage(MessageUtils.noPermission());
            return true;
        }
        if (args.length != 1) {
            player.sendMessage(MessageUtils.error("Usage: /setwarp <name>"));
            return true;
        }

        warpManager.setWarp(args[0], player.getLocation());
        player.sendMessage(MessageUtils.success("Warp set."));
        return true;
    }
}
