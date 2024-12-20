package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hoffmantv.essentialspro.managers.FreezeManager;

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
        // Check if the command sender is a player
        if (!(sender instanceof Player)) {
            sender.sendMessage(MSG_ONLY_PLAYERS);
            return true;
        }

        // Check arguments length
        if (args.length != 1) {
            sender.sendMessage(MSG_USAGE);
            return true;
        }

        // Fetch the target player
        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            sender.sendMessage(MSG_PLAYER_NOT_FOUND);
            return true;
        }

        // Toggle the frozen state
        boolean wasFrozen = freezeManager.isPlayerFrozen(target);
        freezeManager.setPlayerFrozen(target, !wasFrozen);

        if (wasFrozen) {
            // The player was frozen, now unfrozen
            target.sendMessage(Component.text("✔ You have been unfrozen.", NamedTextColor.GREEN));
            sender.sendMessage(Component.text("✔ Player " + target.getName() + " has been unfrozen.", NamedTextColor.GREEN));
        } else {
            // The player was not frozen, now frozen
            target.sendMessage(Component.text("✖ You have been frozen.", NamedTextColor.RED));
            sender.sendMessage(Component.text("✔ Player " + target.getName() + " has been frozen.", NamedTextColor.GREEN));
        }

        return true;
    }
}