package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hoffmantv.essentialspro.managers.TeleportRequestManager;

public class TpDenyCommand implements CommandExecutor {

    private static final Component ONLY_PLAYERS_ERROR = Component.text("✖ Only players can use this command.", NamedTextColor.RED);
    private static final Component NO_PERMISSION = Component.text("✖ You don't have permission to use this command.", NamedTextColor.RED);
    private static final Component NO_REQUESTS = Component.text("✖ You have no pending teleport requests.", NamedTextColor.RED);

    private final TeleportRequestManager teleportRequestManager;

    public TpDenyCommand(TeleportRequestManager teleportRequestManager) {
        this.teleportRequestManager = teleportRequestManager;
    }

    /**
     * Denies the pending teleport request from another player.
     * Usage: /tpdeny
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Ensure that only a player can use this command
        if (!(sender instanceof Player)) {
            sender.sendMessage(ONLY_PLAYERS_ERROR);
            return true;
        }

        Player player = (Player) sender;

        // Check for permission
        if (!player.hasPermission("essentialspro.tpdeny")) {
            player.sendMessage(NO_PERMISSION);
            return true;
        }

        // Attempt to retrieve the requester who sent a teleport request to this player
        Player requester = teleportRequestManager.getRequest(player);

        // If no request is found, inform the player
        if (requester == null) {
            player.sendMessage(NO_REQUESTS);
            return true;
        }

        // Deny the request and notify both players
        player.sendMessage(Component.text("✖ Teleport request denied.", NamedTextColor.RED));
        requester.sendMessage(Component.text("✖ Your teleport request to " + player.getName() + " was denied.", NamedTextColor.RED));

        // Remove the request from the teleport manager
        teleportRequestManager.removeRequest(player);

        return true;
    }
}