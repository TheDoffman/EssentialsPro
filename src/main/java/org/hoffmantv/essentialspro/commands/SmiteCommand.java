package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SmiteCommand implements CommandExecutor {

    private static final String PERMISSION_SMITE = "essentialspro.smite";

    private static final Component MSG_ONLY_PLAYERS = Component.text("This command can only be used by players.", NamedTextColor.RED);
    private static final Component MSG_NO_PERMISSION = Component.text("You don't have permission to use this command.", NamedTextColor.RED);
    private static final Component MSG_USAGE = Component.text("Usage: /smite <player>", NamedTextColor.RED);
    private static final Component MSG_PLAYER_NOT_FOUND = Component.text("Player not found or not online.", NamedTextColor.RED);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MSG_ONLY_PLAYERS);
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission(PERMISSION_SMITE)) {
            player.sendMessage(MSG_NO_PERMISSION);
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(MSG_USAGE);
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(MSG_PLAYER_NOT_FOUND);
            return true;
        }

        // Strike the target with lightning
        target.getWorld().strikeLightning(target.getLocation());

        Component smiteMsg = Component.text("You have smited " + target.getName() + " with lightning!", NamedTextColor.GREEN);
        player.sendMessage(smiteMsg);

        // Notify the smitten player
        Component smittenMsg = Component.text("You were smited by " + player.getName() + "!", NamedTextColor.RED);
        target.sendMessage(smittenMsg);

        return true;
    }
}