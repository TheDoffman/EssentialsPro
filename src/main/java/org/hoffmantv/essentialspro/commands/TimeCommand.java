package org.hoffmantv.essentialspro.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hoffmantv.essentialspro.EssentialsPro;

public class TimeCommand implements CommandExecutor {

    private final EssentialsPro plugin;

    public TimeCommand(EssentialsPro plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("essentialspro.time")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
            return true;
        }

        if (args.length != 1) {
            player.sendMessage(ChatColor.RED + "Usage: /time <day|night|morning|evening>");
            return true;
        }

        String timeArg = args[0].toLowerCase();

        long time;
        switch (timeArg) {
            case "day":
                time = 1000; // Set to morning (1000 ticks)
                break;
            case "night":
                time = 13000; // Set to night (13000 ticks)
                break;
            case "morning":
                time = 0; // Set to morning (0 ticks)
                break;
            case "evening":
                time = 11000; // Set to evening (11000 ticks)
                break;
            default:
                player.sendMessage(ChatColor.RED + "Invalid time argument. Use: day, night, morning, or evening.");
                return true;
        }

        player.getWorld().setTime(time);
        player.sendMessage(ChatColor.GREEN + "Time set to " + timeArg + ".");

        return true;
    }
}
