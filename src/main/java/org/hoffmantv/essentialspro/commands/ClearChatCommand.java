package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClearChatCommand implements CommandExecutor {

    private static final String CLEARCHAT_PERMISSION = "essentialspro.clearchat";
    private static final Component NO_PERMISSION_MSG = Component.text("✖ You don't have permission to use this command.", NamedTextColor.RED);
    private static final Component CHAT_CLEARED_MSG = Component.text("💬 Chat has been cleared for all players.", NamedTextColor.GREEN);

    // Number of blank lines to send (can be made configurable)
    private static final int LINES_TO_SEND = 100;

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
        // Option 1: Sending individual blank messages
        for (Player player : Bukkit.getOnlinePlayers()) {
            for (int i = 0; i < LINES_TO_SEND; i++) {
                player.sendMessage(Component.empty());
            }
        }

        // Option 2 (alternative): Send one consolidated message with newlines.
        // Uncomment the code below to use this alternative approach.
        /*
        StringBuilder blankBuilder = new StringBuilder();
        for (int i = 0; i < LINES_TO_SEND; i++) {
            blankBuilder.append("\n");
        }
        Component blankMessage = Component.text(blankBuilder.toString());
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(blankMessage);
        }
        */
    }
}