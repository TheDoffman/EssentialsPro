package org.hoffmantv.essentialspro.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hoffmantv.essentialspro.managers.BanManager;

public class UnbanCommand implements CommandExecutor {
    private final BanManager banManager;

    private static final String PERMISSION_ERROR = ChatColor.RED + "You don't have permission to use this command.";

    public UnbanCommand(BanManager banManager) {
        this.banManager = banManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player) || !((Player) sender).hasPermission("essentialspro.unban")) {
            sender.sendMessage(PERMISSION_ERROR);
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /unban <player>");
            return true;
        }

        String playerName = args[0];

        try {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                banManager.unbanPlayer(player, playerName);
                sender.sendMessage(ChatColor.GREEN + "Player " + playerName + " has been unbanned.");
            } else {
                banManager.unbanPlayerFromConsole(playerName);
                sender.sendMessage(ChatColor.GREEN + "Player " + playerName + " has been unbanned from the console.");
            }
        } catch (IllegalArgumentException e) {
            sender.sendMessage(ChatColor.RED + e.getMessage());
        }

        return true;
    }
}
