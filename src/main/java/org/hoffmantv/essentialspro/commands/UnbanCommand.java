package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.hoffmantv.essentialspro.managers.BanManager;

public class UnbanCommand implements CommandExecutor {
    private final BanManager banManager;

    private static final Component PERMISSION_ERROR = Component.text("✖ You don't have permission to use this command.", NamedTextColor.RED);

    public UnbanCommand(BanManager banManager) {
        this.banManager = banManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender.hasPermission("essentialspro.unban"))) {
            sender.sendMessage(PERMISSION_ERROR);
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage(Component.text("Usage: /unban <player>", NamedTextColor.RED));
            return true;
        }

        String playerName = args[0];

        try {
            banManager.unbanPlayer(playerName);
            sender.sendMessage(Component.text("Player " + playerName + " has been unbanned.", NamedTextColor.GREEN));
        } catch (IllegalArgumentException e) {
            sender.sendMessage(Component.text(e.getMessage(), NamedTextColor.RED));
        }

        return true;
    }
}