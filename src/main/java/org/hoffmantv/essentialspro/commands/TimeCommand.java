package org.hoffmantv.essentialspro.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hoffmantv.essentialspro.EssentialsPro;

public class TimeCommand implements CommandExecutor {

    private static final String ONLY_PLAYERS_ERROR = ChatColor.RED + "\u274C This command can only be used by players.";
    private static final String PERMISSION_ERROR = ChatColor.RED + "\u274C You don't have permission to use this command.";
    private static final String USAGE_ERROR = ChatColor.RED + "\u274C Usage: /time <day|night|morning|evening>";
    private static final String INVALID_TIME_ERROR = ChatColor.RED + "\u274C Invalid time argument. Use: day, night, morning, or evening.";
    private static final String TIME_SUCCESS = ChatColor.GREEN + "Time set to ";

    private final EssentialsPro plugin;

    public TimeCommand(EssentialsPro plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ONLY_PLAYERS_ERROR);
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("essentialspro.time")) {
            player.sendMessage(PERMISSION_ERROR);
            return true;
        }

        if (args.length != 1) {
            player.sendMessage(USAGE_ERROR);
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
                player.sendMessage(INVALID_TIME_ERROR);
                return true;
        }

        player.getWorld().setTime(time);
        player.sendMessage(TIME_SUCCESS + timeArg + ".");

        return true;
    }
}
