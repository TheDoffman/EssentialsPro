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
        // Ensure the command is only used by players
        if (!(sender instanceof Player)) {
            sender.sendMessage(Component.text("This command can only be used by players.", NamedTextColor.RED));
            return true;
        }

        Player player = (Player) sender;

        // Check if the player has permission to use /fly on themselves
        if (!player.hasPermission("essentialspro.fly")) {
            player.sendMessage(Component.text("✖ You don't have permission to use this command.", NamedTextColor.RED));
            return true;
        }

        Player target = player; // Default target is the sender

        // If arguments are provided, try to find the target player
        if (args.length >= 1) {
            // Check permission to change others' flight mode
            if (!player.hasPermission("essentialspro.fly.others")) {
                player.sendMessage(Component.text("✖ You don't have permission to change others' flight mode.", NamedTextColor.RED));
                return true;
            }

            target = Bukkit.getPlayerExact(args[0]);
            if (target == null) {
                player.sendMessage(Component.text("✖ Could not find player: " + args[0], NamedTextColor.RED));
                return true;
            }
        } else {
            // If no arguments are provided and the player tried to target someone else without permission, show usage
            if (!player.hasPermission("essentialspro.fly.others")) {
                player.sendMessage(Component.text("Usage: /" + label + " [player]", NamedTextColor.YELLOW));
            }
        }

        // Toggle flight mode
        boolean newFlightState = !target.getAllowFlight();
        target.setAllowFlight(newFlightState);

        // If flight is turned off, ensure the player is not flying
        if (!newFlightState) {
            target.setFlying(false);
        }

        // Send messages to target and the command sender
        String modeStatus = newFlightState ? "enabled" : "disabled";
        sendMessage(target, player, "Flight mode " + modeStatus + ".");

        return true;
    }

    /**
     * Sends a success message to the target and, if different, also notifies the command sender.
     */
    private void sendMessage(Player target, Player sender, String message) {
        target.sendMessage(Component.text(message, NamedTextColor.GREEN));
        if (!target.equals(sender)) {
            sender.sendMessage(Component.text(target.getName() + "'s " + message, NamedTextColor.GREEN));
        }
    }
}