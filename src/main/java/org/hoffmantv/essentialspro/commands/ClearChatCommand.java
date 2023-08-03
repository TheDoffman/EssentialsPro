package org.hoffmantv.essentialspro.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClearChatCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("essentialspro.clearchat")) {
            sender.sendMessage(ChatColor.RED + "\u274C You don't have permission to use this command.");
            return true;
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            for (int i = 0; i < 100; i++) {
                player.sendMessage("");
            }
        }

        // Notify the command sender that the chat has been cleared
        sender.sendMessage(ChatColor.GREEN + "\uD83D\uDCAC Chat has been cleared for all players.");

        return true;
    }
}