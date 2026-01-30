package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hoffmantv.essentialspro.managers.TeleportRequestManager;
import org.hoffmantv.essentialspro.util.MessageUtils;
import org.hoffmantv.essentialspro.util.Permissions;

public class TpaAllCommand implements CommandExecutor {

    private final TeleportRequestManager teleportRequestManager;

    public TpaAllCommand(TeleportRequestManager teleportRequestManager) {
        this.teleportRequestManager = teleportRequestManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(MessageUtils.onlyPlayers());
            return true;
        }
        if (!player.hasPermission(Permissions.TPAALL)) {
            player.sendMessage(MessageUtils.noPermission());
            return true;
        }

        for (Player target : player.getServer().getOnlinePlayers()) {
            if (!target.equals(player)) {
                teleportRequestManager.addRequest(player, target);
                Component targetMessage = Component.text(player.getName(), NamedTextColor.GREEN)
                        .append(Component.text(" has requested to teleport to you. Use ", NamedTextColor.WHITE))
                        .append(Component.text("/tpaccept", NamedTextColor.AQUA))
                        .append(Component.text(" to accept or ", NamedTextColor.WHITE))
                        .append(Component.text("/tpdeny", NamedTextColor.AQUA))
                        .append(Component.text(" to deny.", NamedTextColor.WHITE));
                target.sendMessage(targetMessage);
            }
        }
        player.sendMessage(MessageUtils.success("Teleport requests sent to all players."));
        return true;
    }
}
