package org.hoffmantv.essentialspro.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hoffmantv.essentialspro.EssentialsPro;

public class FeedCommand implements CommandExecutor {

    private final EssentialsPro plugin;

    public FeedCommand(EssentialsPro plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "\u274C This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("essentialspro.feed")) {
            player.sendMessage(ChatColor.RED + "\u274C You don't have permission to use this command.");
            return true;
        }

        // Check if the player wants to feed another player
        if (args.length > 0 && player.hasPermission("essentialspro.feed.others")) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null || !target.isOnline()) {
                player.sendMessage(ChatColor.RED + "\u274C Player not found or not online.");
                return true;
            }

            // Feed the target player
            target.setFoodLevel(20);
            target.setSaturation(5.0f);

            player.sendMessage(ChatColor.GREEN + "\uD83C\uDF57 You have fed " + target.getName() + ".");
            target.sendMessage(ChatColor.GREEN + "\uD83C\uDF57 You have been fed by " + player.getName() + ".");
        } else {
            // Feed the command sender (player)
            player.setFoodLevel(20);
            player.setSaturation(5.0f);

            player.sendMessage(ChatColor.GREEN + "\uD83C\uDF57 You have been fed.");
        }

        return true;
    }
}
