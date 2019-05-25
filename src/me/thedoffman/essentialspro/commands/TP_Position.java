package me.thedoffman.essentialspro.commands;

import me.thedoffman.essentialspro.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TP_Position
implements CommandExecutor {
    private Main plugin = Main.getPlugin(Main.class);

    public TP_Position(Main plugin) {
        Bukkit.getPluginCommand("tpc").setExecutor(this);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String CommandLabel, String[] arg) {
        plugin.prefix = plugin.prefix.replaceAll("&", "\u00A7");
        if (cmd.getName().equalsIgnoreCase("tpc")) {
            if (!sender.hasPermission("ep.tpc")) {
                sender.sendMessage(ChatColor.RED + "You do not have permission to use that command!");
                return true;
            }
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "The console cannot use /tpc");
                return true;
            }
            Player player = (Player)sender;
            if (arg.length < 3) {
                player.sendMessage(plugin.prefix + ChatColor.RED + "Too few arguements. /tpc <x> <y> <z>");
            } else if (arg.length > 3) {
                player.sendMessage(plugin.prefix + ChatColor.RED + "Too many arguements. /tpc <x> <y> <z>");
            } else {
                try {
                    double x = Integer.parseInt(arg[0]);
                    double y = Integer.parseInt(arg[1]);
                    double z = Integer.parseInt(arg[2]);
                    Location loc = new Location(player.getWorld(), x, y, z);
                    player.teleport(loc);
                    player.sendMessage(plugin.prefix + "Teleporting to X:" + x + " Y:" + y + " Z:" + z);
                }
                catch (NumberFormatException e) {
                    player.sendMessage(plugin.prefix + ChatColor.RED + "The numbers may not have decimals!");
                }
            }
        } 
        return true;
    }
}

