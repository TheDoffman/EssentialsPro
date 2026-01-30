package org.hoffmantv.essentialspro.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hoffmantv.essentialspro.managers.HomeManager;
import org.hoffmantv.essentialspro.util.MessageUtils;
import org.hoffmantv.essentialspro.util.Permissions;

public class DelHomeCommand implements CommandExecutor {

    private final HomeManager homeManager;

    public DelHomeCommand(HomeManager homeManager) {
        this.homeManager = homeManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(MessageUtils.onlyPlayers());
            return true;
        }
        if (!player.hasPermission(Permissions.DELHOME)) {
            player.sendMessage(MessageUtils.noPermission());
            return true;
        }
        if (args.length != 1) {
            player.sendMessage(MessageUtils.error("Usage: /delhome <name>"));
            return true;
        }

        boolean deleted = homeManager.deleteHome(player.getUniqueId(), args[0]);
        if (!deleted) {
            player.sendMessage(MessageUtils.error("Home not found."));
            return true;
        }
        player.sendMessage(MessageUtils.success("Home deleted."));
        return true;
    }
}
