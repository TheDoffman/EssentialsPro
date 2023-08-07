package org.hoffmantv.essentialspro.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hoffmantv.essentialspro.managers.TeleportRequestManager;

public class TpaCommand implements CommandExecutor {
    private TeleportRequestManager teleportRequestManager;

    public TpaCommand(TeleportRequestManager teleportRequestManager) {
        this.teleportRequestManager = teleportRequestManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("\u274C Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("essentialspro.tpa")) {
            player.sendMessage(ChatColor.RED + "\u274C You do not have permission to use this command.");
            return true;
        }

        if (args.length != 1) {
            player.sendMessage(ChatColor.RED + "\u274C Usage: /tpa <player>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(ChatColor.RED + "\u274C Player not found.");
            return true;
        }

        teleportRequestManager.addRequest(player, target);
        target.sendMessage(ChatColor.GREEN + player.getName() + " has requested to teleport to you. Use /tpaccept to accept or /tpadeny to deny.");
        player.sendMessage(ChatColor.GREEN + "Teleport request sent to " + target.getName());

        return true;
    }
}
