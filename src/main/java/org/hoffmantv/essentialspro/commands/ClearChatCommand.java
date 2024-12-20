package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.Collections;

public class ClearChatCommand implements CommandExecutor {

    private static final String CLEARCHAT_PERMISSION = "essentialspro.clearchat";
    private static final Component NO_PERMISSION_MSG = Component.text("✖ You don't have permission to use this command.", NamedTextColor.RED);
    private static final Component CHAT_CLEARED_MSG = Component.text("💬 Chat has been cleared for all players.", NamedTextColor.GREEN);

    // We'll create a pre-built list of blank lines to send for clearing the chat
    private static final int LINES_TO_SEND = 100;
    private static final Component BLANK_LINE = Component.empty();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission(CLEARCHAT_PERMISSION)) {
            sender.sendMessage(NO_PERMISSION_MSG);
            return true;
        }

        clearChatForAllPlayers();
        sender.sendMessage(CHAT_CLEARED_MSG);

        return true;
    }

    /**
     * Clears the chat for all currently online players by sending multiple blank lines.
     */
    private void clearChatForAllPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            // Sending multiple blank lines to push old chat out of view
            for (int i = 0; i < LINES_TO_SEND; i++) {
                player.sendMessage(BLANK_LINE);
            }
        }
    }
}