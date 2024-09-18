package org.hoffmantv.essentialspro.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hoffmantv.essentialspro.managers.JailManager;

import java.util.UUID;

public class UnJailCommand implements CommandExecutor {

    private final JailManager jailManager;

    public UnJailCommand(JailManager jailManager) {
        this.jailManager = jailManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("essentialspro.unjail")) {
            player.sendMessage(ChatColor.RED + "✖ You don't have permission to use this command.");
            return true;
        }

        if (args.length != 1) {
            player.sendMessage(ChatColor.RED + "Usage: /unjail <player>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(ChatColor.RED + "✖ Player not found.");
            return true;
        }

        UUID targetUUID = target.getUniqueId();
        if (!jailManager.isPlayerJailed(targetUUID)) {
            player.sendMessage(ChatColor.RED + target.getName() + " is not currently jailed.");
            return true;
        }

        // Unjail the player
        jailManager.unJailPlayer(target);

        player.sendMessage(ChatColor.GREEN + target.getName() + " has been released from jail.");
        return true;
    }
}