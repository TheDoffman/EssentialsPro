package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hoffmantv.essentialspro.managers.TeleportRequestManager;

public class TpaCommand implements CommandExecutor {

    private static final Component NO_PERMISSION = Component.text("✖ You do not have permission to use this command.", NamedTextColor.RED);
    private static final Component ONLY_PLAYERS = Component.text("✖ Only players can use this command.", NamedTextColor.RED);
    private static final Component USAGE = Component.text("✖ Usage: /tpa <player>", NamedTextColor.RED);
    private static final Component PLAYER_NOT_FOUND = Component.text("✖ Player not found.", NamedTextColor.RED);

    private final TeleportRequestManager teleportRequestManager;

    public TpaCommand(TeleportRequestManager teleportRequestManager) {
        this.teleportRequestManager = teleportRequestManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sendError(sender, ONLY_PLAYERS);
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("essentialspro.tpa")) {
            sendError(player, NO_PERMISSION);
            return true;
        }

        if (args.length != 1) {
            sendError(player, USAGE);
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sendError(player, PLAYER_NOT_FOUND);
            return true;
        }

        // Add teleport request
        teleportRequestManager.addRequest(player, target);

        // Notify target
        Component targetMessage = Component.text(player.getName() + " has requested to teleport to you.", NamedTextColor.GREEN)
                .append(Component.text(" Use /tpaccept to accept or /tpadeny to deny.", NamedTextColor.GRAY));
        target.sendMessage(targetMessage);

        // Notify sender
        sendSuccess(player, "Teleport request sent to " + target.getName() + ".");

        return true;
    }

    private void sendError(CommandSender sender, Component message) {
        sender.sendMessage(message);
    }

    private void sendSuccess(CommandSender sender, String message) {
        sender.sendMessage(Component.text("✔ " + message, NamedTextColor.GREEN));
    }
}