package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * FreezeTimeCommand allows players with the proper permission to freeze time globally or for individual players.
 * When time is frozen, the world’s time or a player's personal time is continuously set to a fixed value.
 * Optionally, a duration can be provided to automatically unfreeze time after that period.
 */
public class FreezeTimeCommand implements CommandExecutor {

    private final JavaPlugin plugin;
    // Store frozen time values: for global (world) and for individual players.
    private final Map<World, Long> frozenTimes = new HashMap<>();
    private final Map<Player, Long> playerFrozenTimes = new HashMap<>();

    // Pre-built messages using the Adventure API and Unicode symbols.
    private static final Component NO_PERMISSION = Component.text("✖ You don't have permission to use this command.", NamedTextColor.RED);
    private static final Component USAGE_MESSAGE = Component.text("✖ Usage: /freezetime [global|player <player>] [duration]", NamedTextColor.RED);
    private static final Component TIME_FROZEN_WORLD = Component.text("✔ Time has been frozen for the world.", NamedTextColor.GREEN);
    private static final Component TIME_UNFROZEN_WORLD = Component.text("✔ Time has been unfrozen for the world.", NamedTextColor.GREEN);
    private static final Component TIME_FROZEN_PLAYER = Component.text("✔ Time has been frozen for the player: ", NamedTextColor.GREEN);
    private static final Component TIME_UNFROZEN_PLAYER = Component.text("✔ Time has been unfrozen for the player: ", NamedTextColor.GREEN);
    private static final Component PLAYER_NOT_FOUND = Component.text("✖ Player not found.", NamedTextColor.RED);

    /**
     * Constructs a new FreezeTimeCommand.
     *
     * @param plugin The JavaPlugin instance.
     */
    public FreezeTimeCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Check permission (assuming you require "essentialspro.freezetime" permission externally)
        if (!sender.hasPermission("essentialspro.freezetime")) {
            sender.sendMessage(NO_PERMISSION);
            return true;
        }

        // Determine freeze type based on arguments
        if (args.length == 0 || args[0].equalsIgnoreCase("global")) {
            handleGlobalFreeze(sender, args);
        } else if (args[0].equalsIgnoreCase("player")) {
            handlePlayerFreeze(sender, args);
        } else {
            sender.sendMessage(USAGE_MESSAGE);
        }
        return true;
    }

    /**
     * Handles freezing time globally for the sender's current world.
     *
     * @param sender The command sender.
     * @param args   The command arguments.
     */
    private void handleGlobalFreeze(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Component.text("✖ Only players can freeze time globally (based on their current world).", NamedTextColor.RED));
            return;
        }
        Player player = (Player) sender;
        World world = player.getWorld();
        // Save the current world time to freeze at
        frozenTimes.put(world, world.getTime());

        // Continuously set the world time to the frozen value every tick.
        new BukkitRunnable() {
            @Override
            public void run() {
                if (frozenTimes.containsKey(world)) {
                    world.setTime(frozenTimes.get(world));
                } else {
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);

        sender.sendMessage(TIME_FROZEN_WORLD);

        // If a duration is provided, schedule unfreeze after that duration.
        if (args.length > 1) {
            long duration = parseDuration(args[1]);
            if (duration > 0) {
                Bukkit.getScheduler().runTaskLater(plugin, () -> unfreezeGlobalTime(sender), duration * 20L);
            }
        }
    }

    /**
     * Unfreezes the global time for the sender's current world.
     *
     * @param sender The command sender.
     */
    private void unfreezeGlobalTime(CommandSender sender) {
        if (!(sender instanceof Player)) return;
        Player player = (Player) sender;
        World world = player.getWorld();
        frozenTimes.remove(world);
        sender.sendMessage(TIME_UNFROZEN_WORLD);
    }

    /**
     * Handles freezing time for a specific player.
     *
     * @param sender The command sender.
     * @param args   The command arguments.
     */
    private void handlePlayerFreeze(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(USAGE_MESSAGE);
            return;
        }
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(PLAYER_NOT_FOUND);
            return;
        }
        freezePlayerTime(sender, target);
    }

    /**
     * Freezes the personal time of a specific player.
     *
     * @param sender The command sender.
     * @param target The player to freeze.
     */
    private void freezePlayerTime(CommandSender sender, Player target) {
        // Save the player's current world time to freeze at.
        playerFrozenTimes.put(target, target.getWorld().getTime());

        // Continuously set the player's personal time to the frozen value every tick.
        new BukkitRunnable() {
            @Override
            public void run() {
                if (playerFrozenTimes.containsKey(target)) {
                    target.setPlayerTime(playerFrozenTimes.get(target), false);
                } else {
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);

        sender.sendMessage(TIME_FROZEN_PLAYER.append(Component.text(target.getName(), NamedTextColor.GREEN)));
    }

    /**
     * Parses a duration string (e.g., "10s", "5m", "2h", "1d") into seconds.
     *
     * @param durationStr The duration string.
     * @return The duration in seconds, or 0 if parsing fails.
     */
    private long parseDuration(String durationStr) {
        Pattern pattern = Pattern.compile("(\\d+)([smhd])");
        Matcher matcher = pattern.matcher(durationStr);
        if (matcher.matches()) {
            long number = Long.parseLong(matcher.group(1));
            String unit = matcher.group(2);
            switch (unit) {
                case "s":
                    return number;
                case "m":
                    return TimeUnit.MINUTES.toSeconds(number);
                case "h":
                    return TimeUnit.HOURS.toSeconds(number);
                case "d":
                    return TimeUnit.DAYS.toSeconds(number);
                default:
                    return 0;
            }
        }
        return 0;
    }
}