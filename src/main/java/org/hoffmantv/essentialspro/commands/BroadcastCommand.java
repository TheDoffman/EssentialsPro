package org.hoffmantv.essentialspro.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.ChatColor;

public class BroadcastCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("essentialspro.broadcast")) {
            if (args.length > 0) {
                String message = ChatColor.translateAlternateColorCodes('&', String.join(" ", args));
                Bukkit.broadcastMessage(ChatColor.GREEN + "[Broadcast] " + ChatColor.WHITE + message);
            } else {
                sender.sendMessage(ChatColor.RED + "Usage: /broadcast <message>");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
        }
        return true;
    }
}
