package org.hoffmantv.essentialspro.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.util.ChatPaginator;

public class ClearInventoryCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("clearinventory")) {
            if(sender.hasPermission("essentialspro.clearinventory")) {
                Player target;

                // No arguments, try to clear the sender's inventory
                if(args.length == 0) {
                    if(!(sender instanceof Player)) {
                        sender.sendMessage("Console must specify a player!");
                        return true;
                    }
                    target = (Player) sender;
                }
                // One argument, try to clear the specified player's inventory
                else if(args.length == 1) {
                    target = Bukkit.getPlayer(args[0]);
                    if(target == null) {
                        sender.sendMessage(ChatColor.RED + "That player is not online!");
                        return true;
                    }
                }
                else {
                    sender.sendMessage("Usage: /clearinventory [player]");
                    return true;
                }

                target.getInventory().clear();
                target.sendMessage(ChatColor.GREEN + "Your inventory has been cleared.");
                if(!target.equals(sender)) {
                    sender.sendMessage(ChatColor.RED + target.getName() + "'s inventory has been cleared.");
                }

            } else {
                sender.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
            }
        }
        return true;
    }
}
