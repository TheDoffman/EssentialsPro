package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hoffmantv.essentialspro.managers.TeleportRequestManager;

/**
 * TpaCommand allows a player to send a teleport request to another player.
 * The target player will be notified and can use /tpaccept or /tpdeny to respond.
 *
 * Usage: /tpa <player>
 */
public class TpaCommand implements CommandExecutor {

    private static final Component NO_PERMISSION = Component.text("✖ You do not have permission to use this command.", NamedTextColor.RED);
    private static final Component ONLY_PLAYERS = Component.text("✖ Only players can use this command.", NamedTextColor.RED);
    private static final Component USAGE = Component.text("✖ Usage: /tpa <player>", NamedTextColor.RED);
    private static final Component PLAYER_NOT_FOUND = Component.text("✖ Player not found.", NamedTextColor.RED);

    private final TeleportRequestManager teleportRequestManager;

    /**
     * Constructs a new TpaCommand with the specified TeleportRequestManager.
     *
     * @param teleportRequestManager The TeleportRequestManager that handles teleport requests.
     */
    public TpaCommand(TeleportRequestManager teleportRequestManager) {
        this.teleportRequestManager = teleportRequestManager;
    }

    /**
     * Executes the /tpa command. This command sends a teleport request from the sender to the target player.
     *
     * @param sender  The command sender.
     * @param command The command.
     * @param label   The alias used.
     * @param args    The command arguments.
     * @return true after processing the command.
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Ensure that the sender is a player.
        if (!(sender instanceof Player)) {
            sender.sendMessage(ONLY_PLAYERS);
            return true;
        }
        Player player = (Player) sender;

        // Check if the player has the required permission.
        if (!player.hasPermission("essentialspro.tpa")) {
            player.sendMessage(NO_PERMISSION);
            return true;
        }

        // Validate the argument length
        if (args.length != 1) {
            player.sendMessage(USAGE);
            return true;
        }

        // Attempt to retrieve the target player.
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(PLAYER_NOT_FOUND);
            return true;
        }

        // Add the teleport request
        teleportRequestManager.addRequest(player, target);

        // Notify the target with a clear message using Unicode symbols.
        Component targetMessage = Component.text(player.getName(), NamedTextColor.GREEN)
                .append(Component.text(" has requested to teleport to you. Use ", NamedTextColor.WHITE))
                .append(Component.text("/tpaccept", NamedTextColor.AQUA))
                .append(Component.text(" to accept or ", NamedTextColor.WHITE))
                .append(Component.text("/tpdeny", NamedTextColor.AQUA))
                .append(Component.text(" to deny.", NamedTextColor.WHITE));
        target.sendMessage(targetMessage);

        // Notify the sender that the request has been sent.
        sendSuccess(player, "Teleport request sent to " + target.getName() + ".");
        return true;
    }

    /**
     * Sends an error message to the specified sender.
     *
     * @param sender  The sender of the message.
     * @param message The error message component.
     */
    private void sendError(CommandSender sender, Component message) {
        sender.sendMessage(message);
    }

    /**
     * Sends a success message to the specified sender.
     *
     * @param sender  The sender of the message.
     * @param message The success message (as plain text).
     */
    private void sendSuccess(CommandSender sender, String message) {
        sender.sendMessage(Component.text("✔ " + message, NamedTextColor.GREEN));
    }
}