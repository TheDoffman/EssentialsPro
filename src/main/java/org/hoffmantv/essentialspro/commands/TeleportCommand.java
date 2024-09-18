package org.hoffmantv.essentialspro.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hoffmantv.essentialspro.EssentialsPro;

public class TeleportCommand implements CommandExecutor {

    private static final String ONLY_PLAYERS_ERROR = ChatColor.RED + "\u274C This command can only be used by players.";
    private static final String PERMISSION_ERROR = ChatColor.RED + "\u274C You don't have permission to use this command.";
    private static final String USAGE_ERROR = ChatColor.RED + "\u274C Usage: /tp <player>";
    private static final String PLAYER_NOT_FOUND_ERROR = ChatColor.RED + "\u274C Player not found or not online.";
    private static final String TELEPORT_SUCCESS = ChatColor.GREEN + "You have been teleported to ";

    private final EssentialsPro plugin;

    public TeleportCommand(EssentialsPro plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ONLY_PLAYERS_ERROR);
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("essentialspro.teleport")) {
            player.sendMessage(PERMISSION_ERROR);
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(USAGE_ERROR);
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null || !target.isOnline()) {
            player.sendMessage(PLAYER_NOT_FOUND_ERROR);
            return true;
        }

        player.teleport(target.getLocation());
        player.sendMessage(TELEPORT_SUCCESS + target.getName() + ".");

        return true;
    }
}
