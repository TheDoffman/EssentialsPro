package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class ClearInventoryCommand implements CommandExecutor {

    private static final Component NO_PERMISSION = Component.text("✖ You don't have permission to use this command.", NamedTextColor.RED);
    private static final Component NO_PLAYER_SPECIFIED = Component.text("✖ Console must specify a player!", NamedTextColor.RED);
    private static final Component PLAYER_NOT_FOUND = Component.text("✖ That player is not online!", NamedTextColor.RED);
    private static final Component USAGE_MESSAGE = Component.text("Usage: /clearinventory [player]", NamedTextColor.YELLOW);
    private static final Component INVENTORY_CLEARED = Component.text("✔ Your inventory has been cleared.", NamedTextColor.GREEN);

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // Since this command is known to be "clearinventory" from plugin.yml,
        // we can rely on cmd.getName(), but if you have multiple aliases, you can still check this.
        if (!cmd.getName().equalsIgnoreCase("clearinventory")) {
            return true;
        }

        if (!sender.hasPermission("essentialspro.clearinventory")) {
            sender.sendMessage(NO_PERMISSION);
            return true;
        }

        Player target = determineTarget(sender, args);
        if (target == null) {
            // Error message already sent within determineTarget if needed
            return true;
        }

        target.getInventory().clear();
        target.sendMessage(INVENTORY_CLEARED);

        if (!target.equals(sender)) {
            sender.sendMessage(Component.text("✔ " + target.getName() + "'s inventory has been cleared.", NamedTextColor.GREEN));
        }

        return true;
    }

    /**
     * Determines which player to clear the inventory of based on the arguments.
     * If no arguments are provided and sender is a player, the target is the sender.
     * If one argument is provided, attempts to find the specified player.
     * Sends appropriate error messages if needed and returns null if no valid target is found.
     */
    private Player determineTarget(CommandSender sender, String[] args) {
        // No arguments: target = sender if sender is a player
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(NO_PLAYER_SPECIFIED);
                return null;
            }
            return (Player) sender;
        }

        // One argument: attempt to find specified player
        if (args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(PLAYER_NOT_FOUND);
                return null;
            }
            return target;
        }

        // Too many arguments
        sender.sendMessage(USAGE_MESSAGE);
        return null;
    }
}