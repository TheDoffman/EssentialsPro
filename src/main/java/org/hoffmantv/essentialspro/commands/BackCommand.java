package org.hoffmantv.essentialspro.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hoffmantv.essentialspro.managers.BackManager;
import org.hoffmantv.essentialspro.util.MessageUtils;
import org.hoffmantv.essentialspro.util.Permissions;

public class BackCommand implements CommandExecutor {

    private final BackManager backManager;

    public BackCommand(BackManager backManager) {
        this.backManager = backManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(MessageUtils.onlyPlayers());
            return true;
        }
        if (!player.hasPermission(Permissions.BACK)) {
            player.sendMessage(MessageUtils.noPermission());
            return true;
        }

        int stepsBack = 1;
        if (args.length > 0) {
            try {
                stepsBack = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                player.sendMessage(MessageUtils.error("Usage: /back [steps]"));
                return true;
            }
        }

        Location lastLocation = backManager.getLocation(player, stepsBack);
        if (lastLocation == null) {
            player.sendMessage(MessageUtils.info("No previous location found."));
            return true;
        }

        player.teleport(lastLocation);
        player.sendMessage(MessageUtils.success("Teleported back to a previous location."));
        return true;
    }
}
