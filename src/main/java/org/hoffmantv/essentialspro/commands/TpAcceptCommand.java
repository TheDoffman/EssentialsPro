package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hoffmantv.essentialspro.managers.TeleportRequestManager;

public class TpAcceptCommand implements CommandExecutor {

    private final TeleportRequestManager teleportRequestManager;

    public TpAcceptCommand(TeleportRequestManager teleportRequestManager) {
        this.teleportRequestManager = teleportRequestManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Component.text("Only players can use this command.", NamedTextColor.RED));
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("essentialspro.tpaccept")) {
            player.sendMessage(Component.text("You do not have permission to use this command.", NamedTextColor.RED));
            return true;
        }

        Player requester = teleportRequestManager.getRequest(player);

        if (requester == null) {
            player.sendMessage(Component.text("You have no pending teleport requests.", NamedTextColor.RED));
            return true;
        }

        // Teleport the requester to the player's location
        requester.teleport(player.getLocation());

        // Notify both players
        player.sendMessage(Component.text("Teleport request accepted.", NamedTextColor.GREEN));
        requester.sendMessage(Component.text("Your teleport request has been accepted.", NamedTextColor.GREEN));

        // Remove the teleport request from the manager
        teleportRequestManager.removeRequest(player);

        return true;
    }
}