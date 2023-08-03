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
            sender.sendMessage(ChatColor.RED + "\u2716 This command can only be used by players."); // Cross symbol for error
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "\u2716 Usage: /freeze <player>"); // Cross symbol for error
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            sender.sendMessage(ChatColor.RED + "\u2716 Player not found."); // Cross symbol for error
            return true;
        }

        boolean isFrozen = freezeManager.isPlayerFrozen(target);
        freezeManager.setPlayerFrozen(target, !isFrozen);

        if (isFrozen) {
            target.sendMessage(ChatColor.GREEN + "\u2714 You have been unfrozen."); // Checkmark symbol for success
            sender.sendMessage(ChatColor.GREEN + "\u2714 Player " + target.getName() + " has been unfrozen."); // Checkmark symbol for success
        } else {
            target.sendMessage(ChatColor.RED + "\u2716 You have been frozen."); // Cross symbol for error
            sender.sendMessage(ChatColor.GREEN + "\u2714 Player " + target.getName() + " has been frozen."); // Checkmark symbol for success
        }

        return true;
    }
}
