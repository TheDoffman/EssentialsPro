package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hoffmantv.essentialspro.managers.TeleportRequestManager;

public class TpDenyCommand implements CommandExecutor {

    private final TeleportRequestManager teleportRequestManager;

    public TpDenyCommand(TeleportRequestManager teleportRequestManager) {
        this.teleportRequestManager = teleportRequestManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Component.text("Only players can use this command.", NamedTextColor.RED));
            return true;
        }

        Player player = (Player) sender;
        Player requester = teleportRequestManager.getRequest(player);

        if (requester == null) {
            player.sendMessage(Component.text("You have no pending teleport requests.", NamedTextColor.RED));
            return true;
        }

        player.sendMessage(Component.text("Teleport request denied.", NamedTextColor.RED));
        requester.sendMessage(Component.text("Your teleport request to " + player.getName() + " was denied.", NamedTextColor.RED));
        teleportRequestManager.removeRequest(player);

        return true;
    }
}