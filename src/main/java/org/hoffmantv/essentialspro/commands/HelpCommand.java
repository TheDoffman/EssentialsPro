package org.hoffmantv.essentialspro.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hoffmantv.essentialspro.EssentialsPro;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HelpCommand implements CommandExecutor {

    private final EssentialsPro plugin;

    public HelpCommand(EssentialsPro plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "\u274C This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;

        List<String> allowedCommands = new ArrayList<>();
        for (String cmd : plugin.getDescription().getCommands().keySet()) {
            if (player.hasPermission("essentialspro." + cmd)) {
                allowedCommands.add(cmd);
            }
        }

        Collections.sort(allowedCommands, String.CASE_INSENSITIVE_ORDER);

        int pageSize = 8;
        int totalPages = (allowedCommands.size() + pageSize - 1) / pageSize;

        if (args.length > 0 && args[0].matches("\\d+")) {
            int page = Integer.parseInt(args[0]);
            if (page <= 0 || page > totalPages) {
                player.sendMessage(ChatColor.RED + "\u274C Invalid page number. Please enter a number between 1 and " + totalPages + ".");
                return true;
            }
            displayCommands(player, allowedCommands, page, pageSize);
        } else {
            displayCommands(player, allowedCommands, 1, pageSize);
        }

        return true;
    }

    private void displayCommands(Player player, List<String> commands, int page, int pageSize) {
        player.sendMessage(ChatColor.YELLOW + "---- " + ChatColor.GOLD + "Available Commands (Page " + page + "/" + ((commands.size() + pageSize - 1) / pageSize) + ")" + ChatColor.YELLOW + " ----");
        int startIndex = (page - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, commands.size());
        for (int i = startIndex; i < endIndex; i++) {
            String cmd = commands.get(i);
            String usage = getCommandUsage(cmd);
            player.sendMessage(ChatColor.GREEN + "/" + cmd + " " + ChatColor.YELLOW + usage);
        }
    }

    private String getCommandUsage(String commandName) {
        Command command = plugin.getCommand(commandName);
        if (command != null) {
            String usage = command.getUsage();
            return usage != null ? usage : "";
        }
        return "";
    }
}
