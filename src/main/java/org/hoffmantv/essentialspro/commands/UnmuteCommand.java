package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hoffmantv.essentialspro.managers.MuteManager;

public class UnmuteCommand implements CommandExecutor {

    private final MuteManager muteManager;

    // Message constants for consistency and easier maintenance
    private static final Component NO_PERMISSION = Component.text("✖ You don't have permission to unmute players.", NamedTextColor.RED);
    private static final Component USAGE = Component.text("✖ Usage: /unmute <player>", NamedTextColor.RED);
    private static final Component PLAYER_NOT_FOUND = Component.text("✖ Player not found or not online.", NamedTextColor.RED);

    public UnmuteCommand(MuteManager muteManager) {
        this.muteManager = muteManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("essentialspro.unmute")) {
            sender.sendMessage(NO_PERMISSION);
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage(USAGE);
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(PLAYER_NOT_FOUND);
            return true;
        }

        if (!muteManager.isMuted(target)) {
            sender.sendMessage(Component.text("✖ " + target.getName() + " is not muted.", NamedTextColor.RED));
            return true;
        }

        muteManager.unmutePlayer(target);

        // Notify the sender and the target of the unmute event
        sender.sendMessage(Component.text("✔ " + target.getName() + " has been unmuted.", NamedTextColor.GREEN));
        target.sendMessage(Component.text("✔ You have been unmuted.", NamedTextColor.GREEN));

        return true;
    }
}