package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClearInventoryCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("clearinventory")) {
            if (sender.hasPermission("essentialspro.clearinventory")) {
                Player target;

                // No arguments, clear the sender's inventory if they are a player
                if (args.length == 0) {
                    if (!(sender instanceof Player)) {
                        sender.sendMessage(Component.text("Console must specify a player!").color(NamedTextColor.RED));
                        return true;
                    }
                    target = (Player) sender;
                }
                // One argument, clear the specified player's inventory
                else if (args.length == 1) {
                    target = Bukkit.getPlayer(args[0]);
                    if (target == null) {
                        sender.sendMessage(Component.text("That player is not online!").color(NamedTextColor.RED));
                        return true;
                    }
                } else {
                    sender.sendMessage(Component.text("Usage: /clearinventory [player]").color(NamedTextColor.YELLOW));
                    return true;
                }

                target.getInventory().clear();
                target.sendMessage(Component.text("Your inventory has been cleared.").color(NamedTextColor.GREEN));
                if (!target.equals(sender)) {
                    sender.sendMessage(Component.text(target.getName() + "'s inventory has been cleared.").color(NamedTextColor.GREEN));
                }

            } else {
                sender.sendMessage(Component.text("You don't have permission to use this command.").color(NamedTextColor.RED));
            }
        }
        return true;
    }
}