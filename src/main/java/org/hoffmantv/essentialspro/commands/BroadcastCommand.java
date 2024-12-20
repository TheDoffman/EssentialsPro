package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.hoffmantv.essentialspro.EssentialsPro;

import java.util.Arrays;
import java.util.List;

public class BroadcastCommand implements CommandExecutor {

    private static final Component NO_PERMISSION = Component.text("✖ You don't have permission to use this command.", NamedTextColor.RED);
    private static final Component USAGE_BASE = Component.text("⚠ Usage: /broadcast [options] <message>", NamedTextColor.RED);
    private static final Component USAGE_GROUP = Component.text("⚠ Usage: /broadcast -g <group> <message>", NamedTextColor.RED);
    private static final Component USAGE_TIMED = Component.text("⚠ Usage: /broadcast -t <delay> <message>", NamedTextColor.RED);
    private static final Component USAGE_SILENT = Component.text("⚠ Usage: /broadcast -s <message>", NamedTextColor.RED);
    private static final Component USAGE_NOTIFY = Component.text("⚠ Usage: /broadcast -n <message>", NamedTextColor.RED);

    private final EssentialsPro plugin;
    private final Component prefix;
    private final Sound defaultSound;

    public BroadcastCommand(EssentialsPro plugin) {
        this.plugin = plugin;
        // Broadcast prefix
        this.prefix = Component.text("📢 [Broadcast] ", NamedTextColor.GREEN);
        // Example sound, adjust as needed
        this.defaultSound = Sound.ENTITY_PLAYER_LEVELUP;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("essentialspro.broadcast")) {
            sender.sendMessage(NO_PERMISSION);
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage(USAGE_BASE);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "-g":
                handleGroupBroadcast(sender, label, args);
                break;
            case "-t":
                handleTimedBroadcast(sender, label, args);
                break;
            case "-s":
                handleSilentBroadcast(sender, label, args);
                break;
            case "-n":
                handleNotifyBroadcast(sender, label, args);
                break;
            default:
                // Default broadcast (no options)
                String message = joinArguments(args, 0);
                broadcastMessage(prefix.append(Component.text(message, NamedTextColor.WHITE)));
                break;
        }

        return true;
    }

    private void handleGroupBroadcast(CommandSender sender, String label, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(USAGE_GROUP);
            return;
        }
        String group = args[1];
        String message = joinArguments(args, 2);

        // TODO: Implement group-based broadcasting logic once you have a way to determine player groups
        // For now, just sending a note to the sender.
        sender.sendMessage(Component.text("Group broadcast to '" + group + "' is not yet implemented.", NamedTextColor.YELLOW));
    }

    private void handleTimedBroadcast(CommandSender sender, String label, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(USAGE_TIMED);
            return;
        }

        try {
            int delay = Integer.parseInt(args[1]);
            String message = joinArguments(args, 2);

            new BukkitRunnable() {
                @Override
                public void run() {
                    broadcastMessage(prefix.append(Component.text(message, NamedTextColor.WHITE)));
                }
            }.runTaskLater(plugin, delay * 20L); // Convert delay (seconds) to ticks

            sender.sendMessage(Component.text("✔ Timed broadcast scheduled in " + delay + " seconds.", NamedTextColor.GREEN));

        } catch (NumberFormatException e) {
            sender.sendMessage(Component.text("✖ Invalid delay format. Use a number for delay in seconds.", NamedTextColor.RED));
        }
    }

    private void handleSilentBroadcast(CommandSender sender, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(USAGE_SILENT);
            return;
        }

        String message = joinArguments(args, 1);
        broadcastMessage(Component.text(message, NamedTextColor.WHITE));
    }

    private void handleNotifyBroadcast(CommandSender sender, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(USAGE_NOTIFY);
            return;
        }

        String message = joinArguments(args, 1);
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.playSound(player.getLocation(), defaultSound, 1, 1);
            player.sendMessage(prefix.append(Component.text(message, NamedTextColor.WHITE)));
        }
        sender.sendMessage(Component.text("✔ Notify broadcast sent.", NamedTextColor.GREEN));
    }

    private void broadcastMessage(Component message) {
        Bukkit.broadcast(message);
    }

    /**
     * Joins the arguments starting from a specific index into a single string.
     */
    private String joinArguments(String[] args, int startIndex) {
        List<String> argList = Arrays.asList(args).subList(startIndex, args.length);
        return String.join(" ", argList);
    }
}