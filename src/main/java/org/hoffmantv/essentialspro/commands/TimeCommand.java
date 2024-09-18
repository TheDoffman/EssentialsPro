package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hoffmantv.essentialspro.EssentialsPro;

public class TimeCommand implements CommandExecutor {

    private static final Component ONLY_PLAYERS_ERROR = Component.text("This command can only be used by players.", NamedTextColor.RED);
    private static final Component PERMISSION_ERROR = Component.text("You don't have permission to use this command.", NamedTextColor.RED);
    private static final Component USAGE_ERROR = Component.text("Usage: /time <day|night|morning|evening>", NamedTextColor.RED);
    private static final Component INVALID_TIME_ERROR = Component.text("Invalid time argument. Use: day, night, morning, or evening.", NamedTextColor.RED);
    private static final Component TIME_SUCCESS = Component.text("Time set to ", NamedTextColor.GREEN);

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
        player.sendMessage(TIME_SUCCESS.append(Component.text(timeArg + ".", NamedTextColor.GREEN)));

        return true;
    }
}