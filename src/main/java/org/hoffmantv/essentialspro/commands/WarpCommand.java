package org.hoffmantv.essentialspro.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hoffmantv.essentialspro.managers.WarpManager;
import org.hoffmantv.essentialspro.util.MessageUtils;
import org.hoffmantv.essentialspro.util.Permissions;

public class WarpCommand implements CommandExecutor {

    private final WarpManager warpManager;

    public WarpCommand(WarpManager warpManager) {
        this.warpManager = warpManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(MessageUtils.onlyPlayers());
            return true;
        }
        if (!player.hasPermission(Permissions.WARP)) {
            player.sendMessage(MessageUtils.noPermission());
            return true;
        }
        if (args.length != 1) {
            player.sendMessage(MessageUtils.error("Usage: /warp <name>"));
            return true;
        }

        Location warp = warpManager.getWarp(args[0]);
        if (warp == null) {
            player.sendMessage(MessageUtils.error("Warp not found."));
            return true;
        }
        player.teleport(warp);
        player.sendMessage(MessageUtils.success("Teleported to warp."));
        return true;
    }
}
