package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hoffmantv.essentialspro.managers.WarpManager;
import org.hoffmantv.essentialspro.util.MessageUtils;
import org.hoffmantv.essentialspro.util.Permissions;

import java.util.Set;
import java.util.stream.Collectors;

public class WarpsCommand implements CommandExecutor {

    private final WarpManager warpManager;

    public WarpsCommand(WarpManager warpManager) {
        this.warpManager = warpManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(MessageUtils.onlyPlayers());
            return true;
        }
        if (!player.hasPermission(Permissions.WARPS)) {
            player.sendMessage(MessageUtils.noPermission());
            return true;
        }

        Set<String> warps = warpManager.listWarps();
        if (warps.isEmpty()) {
            player.sendMessage(MessageUtils.info("No warps set."));
            return true;
        }

        String list = warps.stream()
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .collect(Collectors.joining(", "));
        player.sendMessage(MessageUtils.applyPrefix(
                Component.text("Warps: ", NamedTextColor.YELLOW)
                        .append(Component.text(list, NamedTextColor.GREEN))
        ));
        return true;
    }
}
