package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClearChatCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("essentialspro.clearchat")) {
            sender.sendMessage(Component.text("✖ You don't have permission to use this command.").color(NamedTextColor.RED));
            return true;
        }

        // Clear chat for all players
        Component blankLine = Component.text("");
        for (Player player : Bukkit.getOnlinePlayers()) {
            for (int i = 0; i < 100; i++) {
                player.sendMessage(blankLine);
            }
        }

        // Notify the sender that chat has been cleared
        sender.sendMessage(Component.text("💬 Chat has been cleared for all players.").color(NamedTextColor.GREEN));

        return true;
    }
}