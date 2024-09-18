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

public class BroadcastCommand implements CommandExecutor {

    private final EssentialsPro plugin;

    public BroadcastCommand(EssentialsPro plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("essentialspro.broadcast")) {
            sender.sendMessage(Component.text("✖ You don't have permission to use this command.").color(NamedTextColor.RED));
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage(Component.text("⚠ Usage: /" + label + " [options] <message>").color(NamedTextColor.RED));
            return true;
        }

        String prefix = Component.text("📢 [Broadcast] ").color(NamedTextColor.GREEN).toString();
        String soundName = "ENTITY_PLAYER_LEVELUP"; // Example sound, change as needed
        Sound sound = Sound.valueOf(soundName);

        // Group-based broadcast
        if (args[0].equalsIgnoreCase("-g")) {
            if (args.length < 3) {
                sender.sendMessage(Component.text("⚠ Usage: /" + label + " -g <group> <message>").color(NamedTextColor.RED));
                return true;
            }
            String group = args[1];
            String message = String.join(" ", args).substring(4 + group.length());
            // Logic to broadcast to specific group
            // Assuming you have a way to fetch groups and send the message
        }
        // Timed broadcast
        else if (args[0].equalsIgnoreCase("-t")) {
            if (args.length < 3) {
                sender.sendMessage(Component.text("⚠ Usage: /" + label + " -t <delay> <message>").color(NamedTextColor.RED));
                return true;
            }

            try {
                int delay = Integer.parseInt(args[1]);
                String message = String.join(" ", args).substring(4 + args[1].length());

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Bukkit.broadcast(Component.text(prefix + message).color(NamedTextColor.WHITE));
                    }
                }.runTaskLater(plugin, delay * 20); // Convert delay to ticks (20 ticks = 1 second)

            } catch (NumberFormatException e) {
                sender.sendMessage(Component.text("✖ Invalid delay format. Use a number for delay in seconds.").color(NamedTextColor.RED));
                return true;
            }
        }
        // Silent broadcast (no sound, just message)
        else if (args[0].equalsIgnoreCase("-s")) {
            if (args.length < 2) {
                sender.sendMessage(Component.text("⚠ Usage: /" + label + " -s <message>").color(NamedTextColor.RED));
                return true;
            }

            String message = String.join(" ", args).substring(3);
            Bukkit.broadcast(Component.text(message).color(NamedTextColor.WHITE));
        }
        // Notify broadcast (plays sound and shows message to everyone)
        else if (args[0].equalsIgnoreCase("-n")) {
            if (args.length < 2) {
                sender.sendMessage(Component.text("⚠ Usage: /" + label + " -n <message>").color(NamedTextColor.RED));
                return true;
            }

            String message = String.join(" ", args).substring(3);
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.playSound(player.getLocation(), sound, 1, 1);
                player.sendMessage(Component.text(prefix + message).color(NamedTextColor.WHITE));
            }
        }
        // Default broadcast
        else {
            String message = String.join(" ", args);
            Bukkit.broadcast(Component.text(prefix + message).color(NamedTextColor.WHITE));
        }

        return true;
    }
}