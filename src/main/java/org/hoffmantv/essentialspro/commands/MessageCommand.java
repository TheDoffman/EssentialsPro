package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class MessageCommand implements CommandExecutor {

    private static final Component ONLY_PLAYERS_ERROR = Component.text("✖ This command can only be used by players.", NamedTextColor.RED);
    private static final Component NO_PERMISSION = Component.text("✖ You don't have permission to use this command.", NamedTextColor.RED);
    private static final Component USAGE_ERROR = Component.text("✖ Usage: /message <player> <message>", NamedTextColor.RED);
    private static final Component PLAYER_NOT_FOUND = Component.text("✖ Player not found or not online.", NamedTextColor.RED);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Ensure only players can use this command
        if (!(sender instanceof Player)) {
            sender.sendMessage(ONLY_PLAYERS_ERROR);
            return true;
        }

        Player player = (Player) sender;

        // Check permission
        if (!player.hasPermission("essentialspro.message")) {
            player.sendMessage(NO_PERMISSION);
            return true;
        }

        // Validate arguments
        if (args.length < 2) {
            player.sendMessage(USAGE_ERROR);
            return true;
        }

        // Get the target player
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null || !target.isOnline()) {
            player.sendMessage(PLAYER_NOT_FOUND);
            return true;
        }

        // Join the rest of the arguments into a message string
        String message = joinArguments(args, 1);

        // Send the message to the sender
        player.sendMessage(Component.text("You -> " + target.getName() + ": ")
                .append(Component.text(message, NamedTextColor.GRAY)));

        // Send the message to the target
        target.sendMessage(Component.text(player.getName() + " -> You: ")
                .append(Component.text(message, NamedTextColor.GRAY)));

        return true;
    }

    /**
     * Joins the arguments starting from a specific index into a single space-separated string.
     */
    private String joinArguments(String[] args, int startIndex) {
        StringBuilder sb = new StringBuilder();
        for (int i = startIndex; i < args.length; i++) {
            sb.append(args[i]).append(" ");
        }
        return sb.toString().trim();
    }
}