package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hoffmantv.essentialspro.EssentialsPro;

import java.util.HashMap;
import java.util.Map;

public class TimeCommand implements CommandExecutor {

    private static final Component ONLY_PLAYERS_ERROR = Component.text("✖ This command can only be used by players.", NamedTextColor.RED);
    private static final Component PERMISSION_ERROR = Component.text("✖ You don't have permission to use this command.", NamedTextColor.RED);
    private static final Component USAGE_ERROR = Component.text("✖ Usage: /time <day|night|morning|evening>", NamedTextColor.RED);
    private static final Component TIME_SUCCESS_PREFIX = Component.text("✔ Time set to ", NamedTextColor.GREEN);

    // Mapping of time arguments to their respective tick values
    private static final Map<String, Long> TIME_OPTIONS = new HashMap<>();
    static {
        TIME_OPTIONS.put("day", 1000L);
        TIME_OPTIONS.put("night", 13000L);
        TIME_OPTIONS.put("morning", 0L);
        TIME_OPTIONS.put("evening", 11000L);
    }

    private final EssentialsPro plugin;

    public TimeCommand(EssentialsPro plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Ensure command is used by a player
        if (!(sender instanceof Player)) {
            sender.sendMessage(ONLY_PLAYERS_ERROR);
            return true;
        }

        Player player = (Player) sender;

        // Check permissions
        if (!player.hasPermission("essentialspro.time")) {
            player.sendMessage(PERMISSION_ERROR);
            return true;
        }

        // Validate arguments
        if (args.length != 1) {
            player.sendMessage(USAGE_ERROR);
            return true;
        }

        String timeArg = args[0].toLowerCase();
        Long timeTicks = TIME_OPTIONS.get(timeArg);

        if (timeTicks == null) {
            // Construct a message listing valid options
            Component validOptions = Component.text("Valid options: ", NamedTextColor.YELLOW)
                    .append(Component.text(String.join(", ", TIME_OPTIONS.keySet()), NamedTextColor.GREEN));

            player.sendMessage(Component.text("✖ Invalid time argument. ", NamedTextColor.RED).append(validOptions));
            return true;
        }

        // Set the world time
        player.getWorld().setTime(timeTicks);
        player.sendMessage(TIME_SUCCESS_PREFIX.append(Component.text(timeArg + ".", NamedTextColor.GREEN)));

        return true;
    }
}