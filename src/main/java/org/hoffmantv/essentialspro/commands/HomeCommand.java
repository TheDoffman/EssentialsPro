package org.hoffmantv.essentialspro.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hoffmantv.essentialspro.managers.HomeManager;
import org.hoffmantv.essentialspro.util.MessageUtils;
import org.hoffmantv.essentialspro.util.Permissions;

public class HomeCommand implements CommandExecutor {

    private final HomeManager homeManager;

    public HomeCommand(HomeManager homeManager) {
        this.homeManager = homeManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(MessageUtils.onlyPlayers());
            return true;
        }
        if (!player.hasPermission(Permissions.HOME)) {
            player.sendMessage(MessageUtils.noPermission());
            return true;
        }

        String homeName = args.length > 0 ? args[0] : "home";
        Location home = homeManager.getHome(player.getUniqueId(), homeName);
        if (home == null) {
            player.sendMessage(MessageUtils.error("Home '" + homeName + "' not found."));
            return true;
        }
        player.teleport(home);
        player.sendMessage(MessageUtils.success("Teleported to home '" + homeName + "'."));
        return true;
    }
}
