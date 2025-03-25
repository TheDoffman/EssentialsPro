package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClearInventoryCommand implements CommandExecutor {

    // Adventure API components for messages
    private static final Component NO_PERMISSION = Component.text("✖ You don't have permission to use this command.", NamedTextColor.RED);
    private static final Component NO_PLAYER_SPECIFIED = Component.text("✖ Console must specify a player!", NamedTextColor.RED);
    private static final Component PLAYER_NOT_FOUND = Component.text("✖ That player is not online!", NamedTextColor.RED);
    private static final Component USAGE_MESSAGE = Component.text("Usage: /clearinventory [player]", NamedTextColor.YELLOW);
    private static final Component INVENTORY_CLEARED = Component.text("✔ Your inventory has been cleared.", NamedTextColor.GREEN);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Check if sender has the necessary permission
        if (!sender.hasPermission("essentialspro.clearinventory")) {
            sender.sendMessage(NO_PERMISSION);
            return true;
        }

        // Determine target player based on command arguments
        Player target = determineTarget(sender, args);
        if (target == null) {
            return true;
        }

        // Clear the player's inventory, armor, and off-hand slot
        target.getInventory().clear();
        target.getInventory().setArmorContents(null);
        target.getInventory().setItemInOffHand(null);
        target.sendMessage(INVENTORY_CLEARED);

        // If the sender isn't the target, inform the sender as well
        if (!target.equals(sender)) {
            sender.sendMessage(Component.text("✔ " + target.getName() + "'s inventory has been cleared.", NamedTextColor.GREEN));
        }

        return true;
    }

    private Player determineTarget(CommandSender sender, String[] args) {
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(NO_PLAYER_SPECIFIED);
                return null;
            }
            return (Player) sender;
        }

        if (args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(PLAYER_NOT_FOUND);
                return null;
            }
            return target;
        }

        // Too many arguments provided – show usage message
        sender.sendMessage(USAGE_MESSAGE);
        return null;
    }
}