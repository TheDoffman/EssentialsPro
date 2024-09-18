package org.hoffmantv.essentialspro.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.stream.Collectors;

public class ListPlayersCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (isNotPlayer(sender)) {
            sender.sendMessage(errorMessage("\u274C This command can only be used by players."));
            return true;
        }

        Player player = (Player) sender;
        String onlinePlayers = getPlayerList(player);

        if (onlinePlayers.isEmpty()) {
            player.sendMessage(errorMessage("\u274C No players are currently online."));
            return true;
        }

        player.sendMessage(headerMessage(player, "Online Players"));
        player.sendMessage(successMessage(onlinePlayers));

        return true;
    }

    private boolean isNotPlayer(CommandSender sender) {
        return !(sender instanceof Player);
    }

    private String errorMessage(String message) {
        return ChatColor.RED + message;
    }

    private String successMessage(String message) {
        return ChatColor.GREEN + message;
    }

    private String headerMessage(Player player, String title) {
        return ChatColor.YELLOW + "---- " + ChatColor.GOLD + title + " ("
                + player.getServer().getOnlinePlayers().size() + "/"
                + player.getServer().getMaxPlayers() + ")"
                + ChatColor.YELLOW + " ----";
    }

    private String getPlayerList(Player player) {
        return player.getServer().getOnlinePlayers().stream()
                .map(Player::getDisplayName)
                .collect(Collectors.joining(", "));
    }
}
