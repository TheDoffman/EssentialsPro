package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hoffmantv.essentialspro.managers.TeleportRequestManager;

/**
 * TpDenyCommand allows a player to deny a pending teleport request.
 * The command checks for the permission node "essentialspro.tpdeny".
 * If a request is found, it denies it and notifies both the requester and the receiver.
 */
public class TpDenyCommand implements CommandExecutor {

    private final TeleportRequestManager teleportRequestManager;

    // Pre-defined messages using Adventure API with Unicode symbols
    private static final Component ONLY_PLAYERS_ERROR = Component.text("✖ Only players can use this command.", NamedTextColor.RED);
    private static final Component NO_PERMISSION = Component.text("✖ You don't have permission to use this command.", NamedTextColor.RED);
    private static final Component NO_REQUESTS = Component.text("✖ You have no pending teleport requests.", NamedTextColor.RED);

    /**
     * Constructs a new TpDenyCommand with the specified TeleportRequestManager.
     *
     * @param teleportRequestManager The TeleportRequestManager that manages teleport requests.
     */
    public TpDenyCommand(TeleportRequestManager teleportRequestManager) {
        this.teleportRequestManager = teleportRequestManager;
    }

    /**
     * Executes the /tpdeny command.
     *
     * @param sender  The command sender.
     * @param command The command.
     * @param label   The alias used.
     * @param args    The command arguments.
     * @return true after command processing.
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Check that the sender is a player
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ONLY_PLAYERS_ERROR);
            return true;
        }

        // Check permission for tpdeny
        if (!player.hasPermission("essentialspro.tpdeny")) {
            player.sendMessage(NO_PERMISSION);
            return true;
        }

        // Retrieve the pending teleport request for the player
        Player requester = teleportRequestManager.getRequest(player);
        if (requester == null) {
            player.sendMessage(NO_REQUESTS);
            return true;
        }

        // Notify the player that the request was denied
        player.sendMessage(Component.text("✖ Teleport request denied.", NamedTextColor.RED));
        // Notify the requester that their request was denied
        requester.sendMessage(Component.text("✖ Your teleport request to " + player.getName() + " was denied.", NamedTextColor.RED));

        // Remove the request from the teleport manager
        teleportRequestManager.removeRequest(player);
        return true;
    }
}