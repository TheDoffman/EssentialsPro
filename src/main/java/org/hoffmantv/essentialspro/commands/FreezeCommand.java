package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hoffmantv.essentialspro.managers.FreezeManager;

/**
 * Command that toggles the freeze state of a specified player.
 * When frozen, the player is prevented from moving or interacting with the world.
 */
public class FreezeCommand implements CommandExecutor {

    private static final Component MSG_ONLY_PLAYERS = Component.text("✖ This command can only be used by players.", NamedTextColor.RED);
    private static final Component MSG_USAGE = Component.text("✖ Usage: /freeze <player>", NamedTextColor.RED);
    private static final Component MSG_PLAYER_NOT_FOUND = Component.text("✖ Player not found.", NamedTextColor.RED);

    private final FreezeManager freezeManager;

    public FreezeCommand(FreezeManager freezeManager) {
        this.freezeManager = freezeManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Ensure the command sender is a player
        if (!(sender instanceof Player)) {
            sender.sendMessage(MSG_ONLY_PLAYERS);
            return true;
        }
        Player senderPlayer = (Player) sender;

        // Verify proper argument count
        if (args.length != 1) {
            senderPlayer.sendMessage(MSG_USAGE);
            return true;
        }

        // Retrieve the target player
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            senderPlayer.sendMessage(MSG_PLAYER_NOT_FOUND);
            return true;
        }

        // Toggle the freeze state
        boolean wasFrozen = freezeManager.isPlayerFrozen(target);
        freezeManager.setPlayerFrozen(target, !wasFrozen);

        if (wasFrozen) {
            // If target was frozen, unfreeze them
            target.sendMessage(Component.text("✔ You have been unfrozen.", NamedTextColor.GREEN));
            senderPlayer.sendMessage(Component.text("✔ Player " + target.getName() + " has been unfrozen.", NamedTextColor.GREEN));
        } else {
            // If target was not frozen, freeze them
            target.sendMessage(Component.text("✖ You have been frozen.", NamedTextColor.RED));
            senderPlayer.sendMessage(Component.text("✔ Player " + target.getName() + " has been frozen.", NamedTextColor.GREEN));
        }

        return true;
    }
}