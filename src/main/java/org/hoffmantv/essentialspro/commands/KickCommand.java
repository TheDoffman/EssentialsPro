package org.hoffmantv.essentialspro.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KickCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Check if the command sender has permission to use the kick command
        if (!sender.hasPermission("essentialspro.kick")) {
            sender.sendMessage(ChatColor.RED + "\u274C You don't have permission to use this command.");
            return true;
        }

        // Check if the command is used correctly
        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "\u274C Usage: /kick <player> [reason]");
            return true;
        }

        // Get the target player
        Player target = Bukkit.getPlayer(args[0]);

        // Check if the target player is online
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "\u274C Player not found or not online.");
            return true;
        }

        // Build the kick reason
        String reason = "Kicked by an admin.";
        if (args.length > 1) {
            // If the reason is provided, concatenate all the arguments to form the reason
            reason = String.join(" ", args).substring(args[0].length()).trim();
        }

        // Kick the player
        target.kickPlayer(ChatColor.RED + "You have been kicked from the server.\nReason: " + reason);

        // Broadcast the kick message to the server
        Bukkit.broadcastMessage(ChatColor.RED + target.getName() + " has been kicked from the server. Reason: " + reason);

        return true;
    }
}
