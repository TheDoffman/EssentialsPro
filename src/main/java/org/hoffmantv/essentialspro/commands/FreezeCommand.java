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
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /freeze <player>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player not found.");
            return true;
        }

        boolean isFrozen = freezeManager.isPlayerFrozen(target);
        freezeManager.setPlayerFrozen(target, !isFrozen);

        if (isFrozen) {
            target.sendMessage(ChatColor.GREEN + "You have been unfrozen.");
            sender.sendMessage(ChatColor.GREEN + "Player " + target.getName() + " has been unfrozen.");
        } else {
            target.sendMessage(ChatColor.RED + "You have been frozen.");
            sender.sendMessage(ChatColor.GREEN + "Player " + target.getName() + " has been frozen.");
        }

        return true;
    }
}
