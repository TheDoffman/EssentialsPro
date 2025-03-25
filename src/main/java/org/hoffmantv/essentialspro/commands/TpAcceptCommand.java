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
 * TpAcceptCommand allows players to accept a pending teleport request.
 * When executed, the requester is teleported to the command sender's location.
 * Both parties are notified using clear, modern messages.
 */
public class TpAcceptCommand implements CommandExecutor {

    private final TeleportRequestManager teleportRequestManager;

    /**
     * Constructs a new TpAcceptCommand with the specified TeleportRequestManager.
     *
     * @param teleportRequestManager The TeleportRequestManager that manages teleport requests.
     */
    public TpAcceptCommand(TeleportRequestManager teleportRequestManager) {
        this.teleportRequestManager = teleportRequestManager;
    }

    /**
     * Executes the /tpaccept command.
     * Teleports the requesting player to the executing player's location and notifies both players.
     *
     * @param sender  The command sender.
     * @param command The command.
     * @param label   The command alias.
     * @param args    The command arguments.
     * @return true after command processing.
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Ensure only players can execute the command using pattern matching.
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("✖ Only players can use this command.", NamedTextColor.RED));
            return true;
        }

        // Check for required permission.
        if (!player.hasPermission("essentialspro.tpaccept")) {
            player.sendMessage(Component.text("✖ You do not have permission to use this command.", NamedTextColor.RED));
            return true;
        }

        // Retrieve any pending teleport request for this player.
        Player requester = teleportRequestManager.getRequest(player);
        if (requester == null) {
            player.sendMessage(Component.text("✖ You have no pending teleport requests.", NamedTextColor.RED));
            return true;
        }

        // Teleport the requester to the player's location.
        requester.teleport(player.getLocation());

        // Notify both parties.
        player.sendMessage(Component.text("✔ Teleport request accepted.", NamedTextColor.GREEN));
        requester.sendMessage(Component.text("✔ Your teleport request has been accepted.", NamedTextColor.GREEN));

        // Remove the fulfilled request.
        teleportRequestManager.removeRequest(player);

        return true;
    }
}