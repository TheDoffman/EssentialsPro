package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.stream.Collectors;

public class ListPlayersCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (isNotPlayer(sender)) {
            sender.sendMessage(errorMessage("❌ This command can only be used by players."));
            return true;
        }

        Player player = (Player) sender;
        String onlinePlayers = getPlayerList(player);

        if (onlinePlayers.isEmpty()) {
            player.sendMessage(errorMessage("❌ No players are currently online."));
            return true;
        }

        player.sendMessage(headerMessage(player, "Online Players"));
        player.sendMessage(successMessage(onlinePlayers));

        return true;
    }

    private boolean isNotPlayer(CommandSender sender) {
        return !(sender instanceof Player);
    }

    private Component errorMessage(String message) {
        return Component.text(message, NamedTextColor.RED);
    }

    private Component successMessage(String message) {
        return Component.text(message, NamedTextColor.GREEN);
    }

    private Component headerMessage(Player player, String title) {
        return Component.text("---- ", NamedTextColor.YELLOW)
                .append(Component.text(title, NamedTextColor.GOLD))
                .append(Component.text(" (" + player.getServer().getOnlinePlayers().size() + "/"
                        + player.getServer().getMaxPlayers() + ")", NamedTextColor.YELLOW))
                .append(Component.text(" ----", NamedTextColor.YELLOW));
    }

    private String getPlayerList(Player player) {
        return player.getServer().getOnlinePlayers().stream()
                .map(Player::getDisplayName)
                .collect(Collectors.joining(", "));
    }
}