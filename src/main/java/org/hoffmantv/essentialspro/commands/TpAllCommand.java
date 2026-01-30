package org.hoffmantv.essentialspro.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hoffmantv.essentialspro.util.MessageUtils;
import org.hoffmantv.essentialspro.util.Permissions;

public class TpAllCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(MessageUtils.onlyPlayers());
            return true;
        }
        if (!player.hasPermission(Permissions.TPALL)) {
            player.sendMessage(MessageUtils.noPermission());
            return true;
        }

        for (Player target : player.getServer().getOnlinePlayers()) {
            if (!target.equals(player)) {
                target.teleport(player.getLocation());
                target.sendMessage(MessageUtils.success("Teleported to " + player.getName() + "."));
            }
        }
        player.sendMessage(MessageUtils.success("Teleported all players to you."));
        return true;
    }
}
