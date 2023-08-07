package org.hoffmantv.essentialspro.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MessageCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "\u274C This command can only be used by players.");
            return true;
        }

        Player senderPlayer = (Player) sender;

        if (args.length < 2) {
            senderPlayer.sendMessage(ChatColor.RED + "\u274C Usage: /message <player> <message>");
            return true;
        }

        String targetName = args[0];
        Player target = Bukkit.getPlayer(targetName);

        if (target == null || !target.isOnline()) {
            senderPlayer.sendMessage(ChatColor.RED + "\u274C Player not found or not online.");
            return true;
        }

        // Combine the rest of the arguments into the message
        StringBuilder messageBuilder = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            messageBuilder.append(args[i]);
            if (i < args.length - 1) {
                messageBuilder.append(" ");
            }
        }
        String message = messageBuilder.toString();

        // Send the message to the sender
        senderPlayer.sendMessage(ChatColor.GRAY + "You" + ChatColor.RESET + " -> " + ChatColor.GRAY + target.getName() + ": " + ChatColor.RESET + message);

        // Send the message to the target
        target.sendMessage(ChatColor.GRAY + senderPlayer.getName() + ChatColor.RESET + " -> " + ChatColor.GRAY + "You: " + ChatColor.RESET + message);

        return true;
    }
}
