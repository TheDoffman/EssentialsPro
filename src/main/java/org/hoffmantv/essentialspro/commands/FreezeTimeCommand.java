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

    public FreezeTimeCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("essentialspro.freezetime")) {
            sender.sendMessage(Component.text("You don't have permission to use this command.", NamedTextColor.RED));
            return true;
        }

        if (args.length == 0 || args[0].equalsIgnoreCase("global")) {
            freezeGlobalTime(sender, args);
        } else if (args.length >= 2 && args[0].equalsIgnoreCase("player")) {
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(Component.text("Player not found.", NamedTextColor.RED));
                return true;
            }
            freezePlayerTime(sender, target);
        } else {
            sender.sendMessage(Component.text("Usage: /freezetime [global|player <player>] [duration]", NamedTextColor.RED));
        }

        return true;
    }

    private void freezeGlobalTime(CommandSender sender, String[] args) {
        World world = ((Player) sender).getWorld();
        frozenTimes.put(world, world.getTime());

        BukkitRunnable freezeTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (frozenTimes.containsKey(world)) {
                    world.setTime(frozenTimes.get(world));
                } else {
                    cancel(); // Stop task when time is unfrozen
                }
            }
        };
        freezeTask.runTaskTimer(plugin, 0L, 1L); // Freeze time every tick

        sender.sendMessage(Component.text("Time has been frozen for the world.", NamedTextColor.GREEN));

        // Optional: If a duration is given, automatically unfreeze after that duration
        if (args.length > 1) {
            long duration = parseDuration(args[1]);
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                unfreezeGlobalTime(sender);
            }, duration * 20);
        }
    }

    private void unfreezeGlobalTime(CommandSender sender) {
        World world = ((Player) sender).getWorld();
        frozenTimes.remove(world);
        sender.sendMessage(Component.text("Time has been unfrozen for the world.", NamedTextColor.GREEN));
    }

    private void freezePlayerTime(CommandSender sender, Player target) {
        playerFrozenTimes.put(target, target.getWorld().getTime());

        BukkitRunnable freezeTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (playerFrozenTimes.containsKey(target)) {
                    target.setPlayerTime(playerFrozenTimes.get(target), false);
                } else {
                    cancel(); // Stop task when time is unfrozen
                }
            }
        };
        freezeTask.runTaskTimer(plugin, 0L, 1L); // Freeze time every tick

        sender.sendMessage(Component.text("Time has been frozen for player: " + target.getName(), NamedTextColor.GREEN));
    }

    private void unfreezePlayerTime(CommandSender sender, Player target) {
        playerFrozenTimes.remove(target);
        target.resetPlayerTime();
        sender.sendMessage(Component.text("Time has been unfrozen for player: " + target.getName(), NamedTextColor.GREEN));
    }

    private long parseDuration(String durationStr) {
        if (durationStr.endsWith("s")) {
            return Long.parseLong(durationStr.replace("s", ""));
        } else if (durationStr.endsWith("m")) {
            return Long.parseLong(durationStr.replace("m", "")) * 60;
        } else if (durationStr.endsWith("h")) {
            return Long.parseLong(durationStr.replace("h", "")) * 3600;
        }
        return 0;
    }
}