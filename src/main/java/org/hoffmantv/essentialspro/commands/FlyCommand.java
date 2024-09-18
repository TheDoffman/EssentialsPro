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
        // Ensure command can only be used by players
        if (!(sender instanceof Player)) {
            sender.sendMessage(Component.text("This command can only be used by players.", NamedTextColor.RED));
            return true;
        }

        Player player = (Player) sender;

        // Check if the player has the required permissions
        if (!player.hasPermission("essentialspro.fly")) {
            player.sendMessage(Component.text("You don't have permission to use this command.", NamedTextColor.RED));
            return true;
        }

        Player target = player;  // By default, target is the sender

        // If an argument is provided, consider it as target player
        if (args.length >= 1) {
            if (!player.hasPermission("essentialspro.fly.others")) {
                player.sendMessage(Component.text("You don't have permission to change others' flight mode.", NamedTextColor.RED));
                return true;
            }

            target = Bukkit.getPlayerExact(args[0]);
            if (target == null) {
                player.sendMessage(Component.text("Could not find player: " + args[0], NamedTextColor.RED));
                return true;
            }
        }

        // Toggle the target player's flight mode
        target.setAllowFlight(!target.getAllowFlight());

        // Send appropriate message to target and possibly to the sender
        if (target.getAllowFlight()) {
            sendMessage(target, player, "Flight mode enabled.");
        } else {
            target.setFlying(false); // Ensure target isn't left flying
            sendMessage(target, player, "Flight mode disabled.");
        }

        return true;
    }

    // Utility method for sending messages
    private void sendMessage(Player target, Player player, String message) {
        target.sendMessage(Component.text(message, NamedTextColor.GREEN));
        if (!target.equals(player)) {
            player.sendMessage(Component.text(target.getName() + "'s " + message, NamedTextColor.GREEN));
        }
    }
}