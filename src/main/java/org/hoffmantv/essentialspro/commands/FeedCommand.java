package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hoffmantv.essentialspro.EssentialsPro;

public class FeedCommand implements CommandExecutor {

    private final EssentialsPro plugin;

    public FeedCommand(EssentialsPro plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Component.text("This command can only be used by players.").color(NamedTextColor.RED));
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("essentialspro.feed")) {
            player.sendMessage(Component.text("You don't have permission to use this command.").color(NamedTextColor.RED));
            return true;
        }

        // Check if the player wants to feed another player
        if (args.length > 0 && player.hasPermission("essentialspro.feed.others")) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null || !target.isOnline()) {
                player.sendMessage(Component.text("Player not found or not online.").color(NamedTextColor.RED));
                return true;
            }

            // Feed the target player
            target.setFoodLevel(20);
            target.setSaturation(5.0f);

            player.sendMessage(Component.text("You have fed " + target.getName() + ".").color(NamedTextColor.GREEN));
            target.sendMessage(Component.text("You have been fed by " + player.getName() + ".").color(NamedTextColor.GREEN));
        } else {
            // Feed the command sender (player)
            player.setFoodLevel(20);
            player.setSaturation(5.0f);

            player.sendMessage(Component.text("You have been fed.").color(NamedTextColor.GREEN));
        }

        return true;
    }
}