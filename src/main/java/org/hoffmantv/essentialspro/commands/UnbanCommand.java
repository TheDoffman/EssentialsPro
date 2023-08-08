package org.hoffmantv.essentialspro.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hoffmantv.essentialspro.managers.BanManager;

public class UnbanCommand implements CommandExecutor {
    private final BanManager banManager;

    private static final String PERMISSION_ERROR = ChatColor.RED + "\u274C You don't have permission to use this command.";

    public UnbanCommand(BanManager banManager) {
        this.banManager = banManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender.hasPermission("essentialspro.unban"))) {
            sender.sendMessage(PERMISSION_ERROR);
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "\u274C Usage: /unban <player>");
            return true;
        }

        String playerName = args[0];

        try {
            banManager.unbanPlayer(playerName);
            sender.sendMessage(ChatColor.GREEN + "Player " + playerName + " has been unbanned.");
        } catch (IllegalArgumentException e) {
            sender.sendMessage(ChatColor.RED + e.getMessage());
        }

        return true;
    }
}
