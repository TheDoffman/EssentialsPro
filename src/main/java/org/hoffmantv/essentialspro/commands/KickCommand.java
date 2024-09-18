package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KickCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Check if the command sender has permission to use the kick command
        if (!sender.hasPermission("essentialspro.kick")) {
            sender.sendMessage(Component.text("❌ You don't have permission to use this command.", NamedTextColor.RED));
            return true;
        }

        // Check if the command is used correctly
        if (args.length < 1) {
            sender.sendMessage(Component.text("❌ Usage: /kick <player> [reason]", NamedTextColor.RED));
            return true;
        }

        // Get the target player
        Player target = Bukkit.getPlayer(args[0]);

        // Check if the target player is online
        if (target == null) {
            sender.sendMessage(Component.text("❌ Player not found or not online.", NamedTextColor.RED));
            return true;
        }

        // Build the kick reason
        String reason = "Kicked by an admin.";
        if (args.length > 1) {
            // If the reason is provided, concatenate all the arguments to form the reason
            reason = String.join(" ", args).substring(args[0].length()).trim();
        }

        // Kick the player with a reason
        target.kick(Component.text("You have been kicked from the server.\nReason: " + reason, NamedTextColor.RED));

        // Broadcast the kick message to the server
        Bukkit.broadcast(Component.text(target.getName() + " has been kicked from the server. Reason: " + reason, NamedTextColor.RED));

        return true;
    }
}