package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MessageCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Component.text("❌ This command can only be used by players.", NamedTextColor.RED));
            return true;
        }

        Player senderPlayer = (Player) sender;

        if (args.length < 2) {
            senderPlayer.sendMessage(Component.text("❌ Usage: /message <player> <message>", NamedTextColor.RED));
            return true;
        }

        String targetName = args[0];
        Player target = Bukkit.getPlayer(targetName);

        if (target == null || !target.isOnline()) {
            senderPlayer.sendMessage(Component.text("❌ Player not found or not online.", NamedTextColor.RED));
            return true;
        }

        // Manually join the rest of the arguments after the first one (the target player's name)
        StringBuilder messageBuilder = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            messageBuilder.append(args[i]).append(" ");
        }
        String message = messageBuilder.toString().trim(); // Trim to remove the trailing space

        // Send the message to the sender
        senderPlayer.sendMessage(Component.text("You -> " + target.getName() + ": ")
                .append(Component.text(message, NamedTextColor.GRAY)));

        // Send the message to the target
        target.sendMessage(Component.text(senderPlayer.getName() + " -> You: ")
                .append(Component.text(message, NamedTextColor.GRAY)));

        return true;
    }
}