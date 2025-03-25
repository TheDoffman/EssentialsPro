package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.stream.Collectors;

public class KickCommand implements CommandExecutor {

    // Predefined messages using the Adventure API with Unicode symbols
    private static final Component NO_PERMISSION = Component.text("✖ You don't have permission to use this command.", NamedTextColor.RED);
    private static final Component USAGE = Component.text("✖ Usage: /kick <player> [reason]", NamedTextColor.RED);
    private static final Component PLAYER_NOT_FOUND = Component.text("✖ Player not found or not online.", NamedTextColor.RED);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Check if sender has the required permission
        if (!sender.hasPermission("essentialspro.kick")) {
            sender.sendMessage(NO_PERMISSION);
            return true;
        }

        // Check if the command is used correctly
        if (args.length < 1) {
            sender.sendMessage(USAGE);
            return true;
        }

        // Get the target player
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null || !target.isOnline()) {
            sender.sendMessage(PLAYER_NOT_FOUND);
            return true;
        }

        // Build the kick reason: use the provided reason or a default message
        String reason = args.length > 1
                ? Arrays.stream(args, 1, args.length).collect(Collectors.joining(" "))
                : "Kicked by an admin.";

        // Kick the target player using the Adventure API
        target.kick(Component.text("You have been kicked from the server.\nReason: " + reason, NamedTextColor.RED));

        // Broadcast the kick event to all players
        Bukkit.broadcast(Component.text(target.getName() + " has been kicked from the server. Reason: " + reason, NamedTextColor.RED));

        return true;
    }
}