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
        // Ensure only players use this command
        if (!(sender instanceof Player)) {
            sender.sendMessage(ONLY_PLAYERS_ERROR);
            return true;
        }

        Player senderPlayer = (Player) sender;

        // Check permission
        if (!senderPlayer.hasPermission("essentialspro.message")) {
            senderPlayer.sendMessage(NO_PERMISSION);
            return true;
        }

        // Validate arguments: at least target and message must be provided
        if (args.length < 2) {
            senderPlayer.sendMessage(USAGE_ERROR);
            return true;
        }

        // Retrieve the target player by name
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null || !target.isOnline()) {
            senderPlayer.sendMessage(PLAYER_NOT_FOUND);
            return true;
        }

        // Combine the remaining arguments into the message
        String message = joinArguments(args, 1);

        // Build sender and target messages using a speech bubble icon (💬) for clarity
        Component senderMsg = Component.text("💬 You → " + target.getName() + ": ", NamedTextColor.AQUA)
                .append(Component.text(message, NamedTextColor.GRAY));
        Component targetMsg = Component.text("💬 " + senderPlayer.getName() + " → You: ", NamedTextColor.AQUA)
                .append(Component.text(message, NamedTextColor.GRAY));

        // Send the messages
        senderPlayer.sendMessage(senderMsg);
        target.sendMessage(targetMsg);

        return true;
    }

    /**
     * Joins the command arguments from the specified start index into a single space-separated string.
     *
     * @param args       The array of command arguments.
     * @param startIndex The starting index to join from.
     * @return The joined string.
     */
    private String joinArguments(String[] args, int startIndex) {
        StringBuilder sb = new StringBuilder();
        for (int i = startIndex; i < args.length; i++) {
            sb.append(args[i]).append(" ");
        }
        return sb.toString().trim();
    }
}