package org.hoffmantv.essentialspro.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.hoffmantv.essentialspro.EssentialsPro;

public class BroadcastCommand implements CommandExecutor {

    public BroadcastCommand(EssentialsPro plugin) {
        this.plugin = plugin;
    }

    private EssentialsPro plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("essentialspro.broadcast")) {
            sender.sendMessage(ChatColor.RED + "\u274C You don't have permission to use this command.");
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "\u26A0 Usage: /" + label + " [options] <message>");
            return true;
        }

        String prefix = ChatColor.GREEN + "\uD83D\uDCE3 [Broadcast] " + ChatColor.WHITE;
        String soundName = "ENTITY_PLAYER_LEVELUP"; // Example sound, change to what you want
        Sound sound = Sound.valueOf(soundName);

        if (args[0].equalsIgnoreCase("-g")) {
            if (args.length < 3) {
                sender.sendMessage(ChatColor.RED + "\u26A0 Usage: /" + label + " -g <group> <message>");
                return true;
            }

            String group = args[1];
            String message = ChatColor.translateAlternateColorCodes('&', String.join(" ", args).substring(4 + group.length()));
        } else if (args[0].equalsIgnoreCase("-t")) {
            if (args.length < 3) {
                sender.sendMessage(ChatColor.RED + "\u26A0 Usage: /" + label + " -t <delay> <message>");
                return true;
            }

            int delay = Integer.parseInt(args[1]);
            String message = ChatColor.translateAlternateColorCodes('&', String.join(" ", args).substring(4 + args[1].length()));
            new BukkitRunnable() {
                @Override
                public void run() {
                    Bukkit.broadcastMessage(prefix + message);
                }
            }.runTaskLater(plugin, delay * 20); // Convert delay to ticks
        } else if (args[0].equalsIgnoreCase("-s")) {
            if (args.length < 2) {
                sender.sendMessage(ChatColor.RED + "\u26A0 Usage: /" + label + " -s <message>");
                return true;
            }

            String message = ChatColor.translateAlternateColorCodes('&', String.join(" ", args).substring(3));
            Bukkit.broadcastMessage(message);
        } else if (args[0].equalsIgnoreCase("-n")) {
            if (args.length < 2) {
                sender.sendMessage(ChatColor.RED + "\u26A0 Usage: /" + label + " -n <message>");
                return true;
            }

            String message = ChatColor.translateAlternateColorCodes('&', String.join(" ", args).substring(3));
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.playSound(player.getLocation(), sound, 1, 1);
                player.sendMessage(prefix + message);
            }
        } else {
            String message = ChatColor.translateAlternateColorCodes('&', String.join(" ", args));
            Bukkit.broadcastMessage(prefix + message);
        }

        return true;
    }
}
