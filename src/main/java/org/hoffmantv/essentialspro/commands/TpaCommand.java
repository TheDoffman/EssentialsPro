package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hoffmantv.essentialspro.managers.TeleportRequestManager;

public class TpaCommand implements CommandExecutor {

    private final TeleportRequestManager teleportRequestManager;

    public TpaCommand(TeleportRequestManager teleportRequestManager) {
        this.teleportRequestManager = teleportRequestManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Component.text("Only players can use this command.", NamedTextColor.RED));
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("essentialspro.tpa")) {
            player.sendMessage(Component.text("You do not have permission to use this command.", NamedTextColor.RED));
            return true;
        }

        if (args.length != 1) {
            player.sendMessage(Component.text("✖ Usage: /tpa <player>", NamedTextColor.RED));
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(Component.text("Player not found.", NamedTextColor.RED));
            return true;
        }

        teleportRequestManager.addRequest(player, target);

        target.sendMessage(Component.text(player.getName() + " has requested to teleport to you.", NamedTextColor.GREEN)
                .append(Component.text(" Use /tpaccept to accept or /tpadeny to deny.", NamedTextColor.GRAY)));
        player.sendMessage(Component.text("Teleport request sent to " + target.getName(), NamedTextColor.GREEN));

        return true;
    }
}