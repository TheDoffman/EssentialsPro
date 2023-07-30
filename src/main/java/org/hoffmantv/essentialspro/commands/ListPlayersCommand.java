package org.hoffmantv.essentialspro.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ListPlayersCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;

        List<String> onlinePlayersList = new ArrayList<>();
        for (Player onlinePlayer : player.getServer().getOnlinePlayers()) {
            onlinePlayersList.add(onlinePlayer.getDisplayName());
        }

        if (onlinePlayersList.isEmpty()) {
            player.sendMessage(ChatColor.RED + "No players are currently online.");
            return true;
        }

        int onlinePlayersCount = onlinePlayersList.size();
        String onlinePlayers = String.join(", ", onlinePlayersList);

        player.sendMessage(ChatColor.YELLOW + "---- " + ChatColor.GOLD + "Online Players (" + onlinePlayersCount + "/" + player.getServer().getMaxPlayers() + ")" + ChatColor.YELLOW + " ----");
        player.sendMessage(ChatColor.GREEN + onlinePlayers);

        return true;
    }
}
