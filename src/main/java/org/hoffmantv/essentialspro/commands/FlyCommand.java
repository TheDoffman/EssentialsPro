package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hoffmantv.essentialspro.EssentialsPro;

public class FlyCommand implements CommandExecutor {

    private final EssentialsPro plugin;

    public FlyCommand(EssentialsPro plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Ensure the command is used by a player.
        if (!(sender instanceof Player)) {
            sender.sendMessage(Component.text("✖ This command can only be used by players.", NamedTextColor.RED));
            return true;
        }
        Player player = (Player) sender;

        // Check if the player has permission to toggle flight for themselves.
        if (!player.hasPermission("essentialspro.fly")) {
            player.sendMessage(Component.text("✖ You don't have permission to use this command.", NamedTextColor.RED));
            return true;
        }

        // Default target is the sender.
        Player target = player;

        // If an argument is provided, try to get a target player for toggling flight.
        if (args.length > 0) {
            // Verify permission for toggling others' flight mode.
            if (!player.hasPermission("essentialspro.fly.others")) {
                player.sendMessage(Component.text("✖ You don't have permission to change others' flight mode.", NamedTextColor.RED));
                return true;
            }
            target = Bukkit.getPlayerExact(args[0]);
            if (target == null) {
                player.sendMessage(Component.text("✖ Could not find player: " + args[0], NamedTextColor.RED));
                return true;
            }
        }

        // Toggle flight mode for the target.
        boolean newFlightState = !target.getAllowFlight();
        target.setAllowFlight(newFlightState);
        if (!newFlightState) {
            target.setFlying(false); // Ensure the player stops flying if flight mode is turned off.
        }

        // Build a status message.
        String status = newFlightState ? "enabled" : "disabled";
        sendMessage(target, player, "Flight mode " + status + ".");

        return true;
    }

    /**
     * Sends a status message to the target and, if different, notifies the sender.
     *
     * @param target  The player whose flight mode was toggled.
     * @param sender  The player who executed the command.
     * @param message The message to send.
     */
    private void sendMessage(Player target, Player sender, String message) {
        Component msg = Component.text(message, NamedTextColor.GREEN);
        target.sendMessage(msg);
        if (!target.equals(sender)) {
            sender.sendMessage(Component.text(target.getName() + "'s " + message, NamedTextColor.GREEN));
        }
    }
}