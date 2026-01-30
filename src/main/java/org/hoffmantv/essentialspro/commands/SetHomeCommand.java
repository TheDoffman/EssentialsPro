package org.hoffmantv.essentialspro.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.hoffmantv.essentialspro.managers.HomeManager;
import org.hoffmantv.essentialspro.util.MessageUtils;
import org.hoffmantv.essentialspro.util.Permissions;

import java.util.Set;

public class SetHomeCommand implements CommandExecutor {

    private final HomeManager homeManager;
    private final JavaPlugin plugin;

    public SetHomeCommand(JavaPlugin plugin, HomeManager homeManager) {
        this.plugin = plugin;
        this.homeManager = homeManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(MessageUtils.onlyPlayers());
            return true;
        }
        if (!player.hasPermission(Permissions.SETHOME)) {
            player.sendMessage(MessageUtils.noPermission());
            return true;
        }

        String homeName = args.length > 0 ? args[0] : "home";
        Set<String> existingHomes = homeManager.listHomes(player.getUniqueId());
        boolean alreadyExists = existingHomes.stream()
                .anyMatch(name -> name.equalsIgnoreCase(homeName));
        int limit = resolveHomeLimit(player);
        if (!alreadyExists && existingHomes.size() >= limit) {
            player.sendMessage(MessageUtils.error("You have reached your home limit (" + limit + ")."));
            return true;
        }

        homeManager.setHome(player.getUniqueId(), homeName, player.getLocation());
        player.sendMessage(MessageUtils.success("Home '" + homeName + "' set."));
        return true;
    }

    private int resolveHomeLimit(Player player) {
        int baseLimit = plugin.getConfig().getInt("homes.defaultLimit", 1);
        int maxLimit = plugin.getConfig().getInt("homes.maxLimit", 10);
        int permissionLimit = 0;
        for (int i = 1; i <= maxLimit; i++) {
            if (player.hasPermission(Permissions.HOMES + "." + i)) {
                permissionLimit = i;
            }
        }
        return Math.max(baseLimit, permissionLimit);
    }
}
