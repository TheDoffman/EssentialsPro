package org.hoffmantv.essentialspro.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hoffmantv.essentialspro.managers.FreezeManager;

public class FreezeCommand implements CommandExecutor {

    private final FreezeManager freezeManager;

    public FreezeCommand(FreezeManager freezeManager) {
        this.freezeManager = freezeManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("essentialspro.freeze")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /freeze <player>");
            return true;
        }

        Player target;
        if (sender instanceof Player && args[0].equalsIgnoreCase("self")) {
            target = (Player) sender;
        } else {
            if (!sender.hasPermission("essentialspro.freeze.others")) {
                sender.sendMessage(ChatColor.RED + "You don't have permission to freeze other players.");
                return true;
            }
            target = Bukkit.getServer().getPlayer(args[0]);
        }

        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player not found.");
            return true;
        }

        boolean isFrozen = freezeManager.isPlayerFrozen(target);
        freezeManager.setPlayerFrozen(target, !isFrozen);

        if (isFrozen) {
            target.sendMessage(ChatColor.GREEN + "You have been unfrozen.");
            sender.sendMessage(target.equals(sender)
                    ? ChatColor.GREEN + "You have unfrozen yourself."
                    : ChatColor.GREEN + "Player " + target.getName() + " has been unfrozen.");
        } else {
            target.sendMessage(ChatColor.RED + "You have been frozen.");
            sender.sendMessage(target.equals(sender)
                    ? ChatColor.GREEN + "You have frozen yourself."
                    : ChatColor.GREEN + "Player " + target.getName() + " has been frozen.");
        }

        return true;
    }
}
