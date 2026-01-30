package org.hoffmantv.essentialspro.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hoffmantv.essentialspro.util.MessageUtils;
import org.hoffmantv.essentialspro.util.PlayerLookup;
import org.hoffmantv.essentialspro.util.Permissions;

public class TpHereCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(MessageUtils.onlyPlayers());
            return true;
        }
        if (!player.hasPermission(Permissions.TPHERE)) {
            player.sendMessage(MessageUtils.noPermission());
            return true;
        }
        if (args.length != 1) {
            player.sendMessage(MessageUtils.error("Usage: /tphere <player>"));
            return true;
        }

        Player target = PlayerLookup.findOnlinePlayer(args[0]);
        if (target == null) {
            player.sendMessage(MessageUtils.playerNotFound());
            return true;
        }

        target.teleport(player.getLocation());
        player.sendMessage(MessageUtils.success("Teleported " + target.getName() + " to you."));
        target.sendMessage(MessageUtils.success("You have been teleported to " + player.getName() + "."));
        return true;
    }
}
