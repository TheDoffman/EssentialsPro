package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hoffmantv.essentialspro.managers.HomeManager;
import org.hoffmantv.essentialspro.util.MessageUtils;
import org.hoffmantv.essentialspro.util.Permissions;

import java.util.Set;
import java.util.stream.Collectors;

public class HomesCommand implements CommandExecutor {

    private final HomeManager homeManager;

    public HomesCommand(HomeManager homeManager) {
        this.homeManager = homeManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(MessageUtils.onlyPlayers());
            return true;
        }
        if (!player.hasPermission(Permissions.HOMES)) {
            player.sendMessage(MessageUtils.noPermission());
            return true;
        }

        Set<String> homes = homeManager.listHomes(player.getUniqueId());
        if (homes.isEmpty()) {
            player.sendMessage(MessageUtils.info("You have no homes set."));
            return true;
        }

        String list = homes.stream()
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .collect(Collectors.joining(", "));
        player.sendMessage(MessageUtils.applyPrefix(
                Component.text("Homes: ", NamedTextColor.YELLOW)
                        .append(Component.text(list, NamedTextColor.GREEN))
        ));
        return true;
    }
}
