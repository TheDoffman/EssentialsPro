package org.hoffmantv.essentialspro.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hoffmantv.essentialspro.managers.TeleportRequestManager;

public class TpDenyCommand implements CommandExecutor {
    private TeleportRequestManager teleportRequestManager;

    public TpDenyCommand(TeleportRequestManager teleportRequestManager) {
        this.teleportRequestManager = teleportRequestManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;
        Player requester = teleportRequestManager.getRequest(player);
        if (requester == null) {
            player.sendMessage(ChatColor.RED + "You have no pending teleport requests.");
            return true;
        }

        player.sendMessage(ChatColor.RED + "Teleport request denied.");
        requester.sendMessage(ChatColor.RED + "Your teleport request to " + player.getName() + " was denied.");
        teleportRequestManager.removeRequest(player);

        return true;
    }
}
