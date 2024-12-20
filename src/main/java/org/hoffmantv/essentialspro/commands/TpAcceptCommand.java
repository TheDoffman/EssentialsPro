package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hoffmantv.essentialspro.managers.TeleportRequestManager;

public class TpAcceptCommand implements CommandExecutor {

    // Constant components for messages
    private static final Component ONLY_PLAYERS_MSG = Component.text("✖ Only players can use this command.", NamedTextColor.RED);
    private static final Component NO_PERMISSION_MSG = Component.text("✖ You do not have permission to use this command.", NamedTextColor.RED);
    private static final Component NO_REQUEST_MSG = Component.text("✖ You have no pending teleport requests.", NamedTextColor.RED);
    private static final Component ACCEPTED_MSG = Component.text("✔ Teleport request accepted.", NamedTextColor.GREEN);
    private static final Component ACCEPTED_NOTIFY_MSG = Component.text("✔ Your teleport request has been accepted.", NamedTextColor.GREEN);

    private final TeleportRequestManager teleportRequestManager;

    public TpAcceptCommand(TeleportRequestManager teleportRequestManager) {
        this.teleportRequestManager = teleportRequestManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Check if sender is a player
        if (!(sender instanceof Player)) {
            sender.sendMessage(ONLY_PLAYERS_MSG);
            return true;
        }

        Player player = (Player) sender;

        // Check permission
        if (!player.hasPermission("essentialspro.tpaccept")) {
            player.sendMessage(NO_PERMISSION_MSG);
            return true;
        }

        // Retrieve any pending teleport request for this player
        Player requester = teleportRequestManager.getRequest(player);
        if (requester == null) {
            player.sendMessage(NO_REQUEST_MSG);
            return true;
        }

        // Teleport the requester to this player's location
        requester.teleport(player.getLocation());

        // Notify both parties
        player.sendMessage(ACCEPTED_MSG);
        requester.sendMessage(ACCEPTED_NOTIFY_MSG);

        // Remove the fulfilled request
        teleportRequestManager.removeRequest(player);

        return true;
    }
}