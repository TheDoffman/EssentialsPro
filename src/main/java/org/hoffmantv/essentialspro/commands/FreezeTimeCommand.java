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

public class FreezeTimeCommand implements CommandExecutor {

    private final JavaPlugin plugin;
    private final Map<World, Long> frozenTimes = new HashMap<>();
    private final Map<Player, Long> playerFrozenTimes = new HashMap<>();

    private static final Component NO_PERMISSION = Component.text("✖ You don't have permission to use this command.", NamedTextColor.RED);
    private static final Component PLAYER_NOT_FOUND = Component.text("✖ Player not found.", NamedTextColor.RED);
    private static final Component USAGE_MESSAGE = Component.text("✖ Usage: /freezetime [global|player <player>] [duration]", NamedTextColor.RED);
    private static final Component TIME_FROZEN_WORLD = Component.text("✔ Time has been frozen for the world.", NamedTextColor.GREEN);
    private static final Component TIME_UNFROZEN_WORLD = Component.text("✔ Time has been unfrozen for the world.", NamedTextColor.GREEN);
    private static final Component TIME_FROZEN_PLAYER = Component.text("✔ Time has been frozen for the player: ", NamedTextColor.GREEN);
    private static final Component TIME_UNFROZEN_PLAYER = Component.text("✔ Time has been unfrozen for the player: ", NamedTextColor.GREEN);

    public FreezeTimeCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("essentialspro.freezetime")) {
            sender.sendMessage(NO_PERMISSION);
            return true;
        }

        if (args.length == 0 || args[0].equalsIgnoreCase("global")) {
            handleGlobalFreeze(sender, args);
        } else if (args[0].equalsIgnoreCase("player")) {
            handlePlayerFreeze(sender, args);
        } else {
            sender.sendMessage(USAGE_MESSAGE);
        }

        return true;
    }

    private void handleGlobalFreeze(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Component.text("✖ Only players can freeze time globally (since we use their current world).", NamedTextColor.RED));
            return;
        }

        Player player = (Player) sender;
        World world = player.getWorld();
        frozenTimes.put(world, world.getTime());

        // Task that continuously sets the time to the frozen value
        BukkitRunnable freezeTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (frozenTimes.containsKey(world)) {
                    world.setTime(frozenTimes.get(world));
                } else {
                    cancel(); // If unfrozen, stop the task
                }
            }
        };
        freezeTask.runTaskTimer(plugin, 0L, 1L); // Freeze time every tick

        sender.sendMessage(TIME_FROZEN_WORLD);

        // If duration is provided, schedule unfreeze after that duration
        if (args.length > 1) {
            long duration = parseDuration(args[1]);
            if (duration > 0) {
                Bukkit.getScheduler().runTaskLater(plugin, () -> unfreezeGlobalTime(sender), duration * 20L);
            }
        }
    }

    private void unfreezeGlobalTime(CommandSender sender) {
        if (!(sender instanceof Player)) {
            // If for some reason this is called by non-player, skip gracefully
            return;
        }

        Player player = (Player) sender;
        World world = player.getWorld();
        frozenTimes.remove(world);
        sender.sendMessage(TIME_UNFROZEN_WORLD);
    }

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

    private void freezePlayerTime(CommandSender sender, Player target) {
        playerFrozenTimes.put(target, target.getWorld().getTime());

        // Task that continuously sets the player’s personal time
        BukkitRunnable freezeTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (playerFrozenTimes.containsKey(target)) {
                    target.setPlayerTime(playerFrozenTimes.get(target), false);
                } else {
                    cancel();
                }
            }
        };
        freezeTask.runTaskTimer(plugin, 0L, 1L);

        sender.sendMessage(TIME_FROZEN_PLAYER.append(Component.text(target.getName(), NamedTextColor.GREEN)));
    }

    private void unfreezePlayerTime(CommandSender sender, Player target) {
        playerFrozenTimes.remove(target);
        target.resetPlayerTime();
        sender.sendMessage(TIME_UNFROZEN_PLAYER.append(Component.text(target.getName(), NamedTextColor.GREEN)));
    }

    private long parseDuration(String durationStr) {
        if (durationStr.endsWith("s")) {
            return parseNumber(durationStr.replace("s", ""));
        } else if (durationStr.endsWith("m")) {
            return parseNumber(durationStr.replace("m", "")) * 60;
        } else if (durationStr.endsWith("h")) {
            return parseNumber(durationStr.replace("h", "")) * 3600;
        }
        return 0;
    }

    private long parseNumber(String numStr) {
        try {
            return Long.parseLong(numStr);
        } catch (NumberFormatException e) {
            return 0; // Invalid format, return 0 as fallback
        }
    }
}