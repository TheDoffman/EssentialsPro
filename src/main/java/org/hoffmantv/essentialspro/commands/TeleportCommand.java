package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hoffmantv.essentialspro.EssentialsPro;

public class TeleportCommand implements CommandExecutor {

    private static final Component ONLY_PLAYERS_ERROR = Component.text("This command can only be used by players.", NamedTextColor.RED);
    private static final Component PERMISSION_ERROR = Component.text("You don't have permission to use this command.", NamedTextColor.RED);
    private static final Component USAGE_ERROR = Component.text("Usage: /tp <player>", NamedTextColor.RED);
    private static final Component PLAYER_NOT_FOUND_ERROR = Component.text("Player not found or not online.", NamedTextColor.RED);

    private final EssentialsPro plugin;

    public TeleportCommand(EssentialsPro plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ONLY_PLAYERS_ERROR);
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("essentialspro.teleport")) {
            player.sendMessage(PERMISSION_ERROR);
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(USAGE_ERROR);
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null || !target.isOnline()) {
            player.sendMessage(PLAYER_NOT_FOUND_ERROR);
            return true;
        }

        player.teleport(target.getLocation());
        player.sendMessage(Component.text("You have been teleported to " + target.getName() + ".", NamedTextColor.GREEN));

        return true;
    }
}