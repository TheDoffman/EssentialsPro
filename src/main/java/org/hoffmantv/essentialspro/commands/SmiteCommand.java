package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SmiteCommand implements CommandExecutor {

    private static final String PERMISSION_SMITE = "essentialspro.smite";

    // Predefined messages using Adventure API with Unicode symbols
    private static final Component ONLY_PLAYERS_MSG = Component.text("✖ This command can only be used by players.", NamedTextColor.RED);
    private static final Component NO_PERMISSION_MSG = Component.text("✖ You don't have permission to use this command.", NamedTextColor.RED);
    private static final Component USAGE_MSG = Component.text("✖ Usage: /smite <player>", NamedTextColor.RED);
    private static final Component PLAYER_NOT_FOUND_MSG = Component.text("✖ Player not found or not online.", NamedTextColor.RED);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Ensure the sender is a player
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ONLY_PLAYERS_MSG);
            return true;
        }

        // Check for permission
        if (!player.hasPermission(PERMISSION_SMITE)) {
            player.sendMessage(NO_PERMISSION_MSG);
            return true;
        }

        // Validate arguments
        if (args.length == 0) {
            player.sendMessage(USAGE_MSG);
            return true;
        }

        // Attempt to find the target player
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(PLAYER_NOT_FOUND_MSG);
            return true;
        }

        // Strike the target with lightning
        target.getWorld().strikeLightning(target.getLocation());

        // Notify the smiting player with an extra lightning icon "⚡"
        Component smiteSuccess = Component.text("✔ You have smited " + target.getName() + " with lightning! ⚡", NamedTextColor.GREEN);
        player.sendMessage(smiteSuccess);

        // Notify the target player
        Component smittenMsg = Component.text("✖ You were smited by " + player.getName() + "! ⚡", NamedTextColor.RED);
        target.sendMessage(smittenMsg);

        // (Optional) Play a sound effect for extra feedback
        player.playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1.0f, 1.0f);

        return true;
    }
}