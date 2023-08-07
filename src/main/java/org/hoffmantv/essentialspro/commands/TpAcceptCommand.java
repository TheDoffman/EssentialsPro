package org.hoffmantv.essentialspro.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hoffmantv.essentialspro.managers.TeleportRequestManager;

public class TpAcceptCommand implements CommandExecutor {
    private TeleportRequestManager teleportRequestManager;

    public TpAcceptCommand(TeleportRequestManager teleportRequestManager) {
        this.teleportRequestManager = teleportRequestManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("\u274C Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("essentialspro.tpaccept")) {
            player.sendMessage(ChatColor.RED + "\u274C You do not have permission to use this command.");
            return true;
        }

        Player requester = teleportRequestManager.getRequest(player);
        if (requester == null) {
            player.sendMessage(ChatColor.RED + "\u274C You have no pending teleport requests.");
            return true;
        }

        requester.teleport(player.getLocation());
        player.sendMessage(ChatColor.GREEN + "Teleport request accepted.");
        teleportRequestManager.removeRequest(player);

        return true;
    }
}
