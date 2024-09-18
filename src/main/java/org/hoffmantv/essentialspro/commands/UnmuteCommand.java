package org.hoffmantv.essentialspro.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hoffmantv.essentialspro.managers.MuteManager;

public class UnmuteCommand implements CommandExecutor {

    private final MuteManager muteManager;

    public UnmuteCommand(MuteManager muteManager) {
        this.muteManager = muteManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player) && !sender.hasPermission("essentialspro.unmute")) {
            sender.sendMessage(ChatColor.RED + "✖ You don't have permission to unmute players.");
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "✖ Usage: /unmute <player>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "✖ Player not found or not online.");
            return true;
        }

        if (!muteManager.isMuted(target)) {
            sender.sendMessage(ChatColor.RED + "✖ " + target.getName() + " is not muted.");
            return true;
        }

        muteManager.unmutePlayer(target);
        sender.sendMessage(ChatColor.GREEN + "✔ " + target.getName() + " has been unmuted.");
        target.sendMessage(ChatColor.GREEN + "✔ You have been unmuted.");

        return true;
    }
}