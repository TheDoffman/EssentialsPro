package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.hoffmantv.essentialspro.managers.BanManager;

public class UnbanCommand implements CommandExecutor {

    private final BanManager banManager;

    // Messages
    private static final Component PERMISSION_ERROR = Component.text("✖ You don't have permission to use this command.", NamedTextColor.RED);
    private static final Component USAGE_ERROR = Component.text("✖ Usage: /unban <player>", NamedTextColor.RED);
    private static final String SUCCESS_PREFIX = "✔ Player ";
    private static final Component PLAYER_NOT_FOUND_BASE = Component.text("✖ ", NamedTextColor.RED);

    public UnbanCommand(BanManager banManager) {
        this.banManager = banManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Permission check
        if (!sender.hasPermission("essentialspro.unban")) {
            sender.sendMessage(PERMISSION_ERROR);
            return true;
        }

        // Argument check
        if (args.length != 1) {
            sender.sendMessage(USAGE_ERROR);
            return true;
        }

        String playerName = args[0];

        try {
            banManager.unbanPlayer(playerName);
            sender.sendMessage(Component.text(SUCCESS_PREFIX + playerName + " has been unbanned.", NamedTextColor.GREEN));
        } catch (IllegalArgumentException e) {
            // If BanManager throws an error (e.g., player not banned), show it.
            // Ensuring the exception message is user-friendly is important.
            sender.sendMessage(PLAYER_NOT_FOUND_BASE.append(Component.text(e.getMessage(), NamedTextColor.RED)));
        }

        return true;
    }
}