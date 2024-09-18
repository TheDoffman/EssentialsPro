package org.hoffmantv.essentialspro.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

public class SmiteCommand implements CommandExecutor {

    private static final String PERMISSION_SMITE = "essentialspro.smite";
    private static final String MSG_ONLY_PLAYERS = ChatColor.RED + "\u274C This command can only be used by players.";
    private static final String MSG_NO_PERMISSION = ChatColor.RED + "\u274C You don't have permission to use this command.";
    private static final String MSG_USAGE = ChatColor.RED + "\u274C Usage: /smite <player>";
    private static final String MSG_PLAYER_NOT_FOUND = ChatColor.RED + "\u274C Player not found or not online.";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MSG_ONLY_PLAYERS);
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission(PERMISSION_SMITE)) {
            player.sendMessage(MSG_NO_PERMISSION);
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(MSG_USAGE);
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(MSG_PLAYER_NOT_FOUND);
            return true;
        }

        // Strike the target with lightning
        target.getWorld().strikeLightning(target.getLocation());

        String smiteMsg = ChatColor.GREEN + "You have smited " + target.getName() + " with lightning!";
        player.sendMessage(smiteMsg);

        // Notify the smitten player
        String smittenMsg = ChatColor.RED + "You were smited by " + player.getName() + "!";
        target.sendMessage(smittenMsg);

        return true;
    }
}
