package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.Arrays;

public class KickCommand implements CommandExecutor {

    private static final Component NO_PERMISSION = Component.text("✖ You don't have permission to use this command.", NamedTextColor.RED);
    private static final Component USAGE = Component.text("✖ Usage: /kick <player> [reason]", NamedTextColor.RED);
    private static final Component PLAYER_NOT_FOUND = Component.text("✖ Player not found or not online.", NamedTextColor.RED);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Permission check
        if (!sender.hasPermission("essentialspro.kick")) {
            sender.sendMessage(NO_PERMISSION);
            return true;
        }

        // Check for correct usage
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

        // Build the reason from the remaining args or use the default reason
        String reason = "Kicked by an admin.";
        if (args.length > 1) {
            String[] reasonArgs = Arrays.copyOfRange(args, 1, args.length);
            reason = String.join(" ", reasonArgs);
        }

        // Kick the player
        target.kick(Component.text("You have been kicked from the server.\nReason: " + reason, NamedTextColor.RED));

        // Broadcast the kick event to the server
        Bukkit.broadcast(Component.text(target.getName() + " has been kicked from the server. Reason: " + reason, NamedTextColor.RED));

        return true;
    }
}