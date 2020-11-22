package me.hoffman.essentialspro.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.hoffman.essentialspro.main.Main;

public class Warp implements CommandExecutor {
    private static Main plugin = Main.getPlugin(Main.class);

    public Warp(Main plugin) {
        Bukkit.getPluginCommand("warp").setExecutor(this);
        Bukkit.getPluginCommand("setwarp").setExecutor(this);
        Bukkit.getPluginCommand("delwarp").setExecutor(this);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        plugin.prefix = plugin.prefix.replaceAll("&", "\u00A7");
        if (cmd.getName().equalsIgnoreCase("warp")) {
            if (!sender.hasPermission("ep.warp")) {
                sender.sendMessage(ChatColor.RED + "You do not have permission to use that command!");
                return true;
            }
            if (!(sender instanceof Player)) {
                sender.sendMessage(plugin.prefix + ChatColor.RED + "The console cannot use /warp");
                return true;
            }
            Player p = (Player) sender;
            if (args.length == 0) {
                p.sendMessage(plugin.prefix + ChatColor.RED + "Use: /warp <Name>");
                return true;
            }
            if (plugin.getwarps().getConfigurationSection("warps." + args[0]) == null) {
                p.sendMessage(ChatColor.RED + "Warp " + args[0] + " does not exist!");
                return true;
            }
            World w = Bukkit.getServer().getWorld(plugin.getwarps().getString("warps." + args[0] + ".world"));
            double x = plugin.getwarps().getDouble("warps." + args[0] + ".x");
            double y = plugin.getwarps().getDouble("warps." + args[0] + ".y");
            double z = plugin.getwarps().getDouble("warps." + args[0] + ".z");
            float yaw = plugin.getplayers().getInt(p.getName() + "warps." + args[0] + ".yaw");
            float pitch = plugin.getplayers().getInt(p.getName() + "warps." + args[0] + ".pitch");
            p.teleport(new Location(w, x, y, z, yaw, pitch));
            p.sendMessage(ChatColor.GREEN + "Warped to " + args[0] + "!");
        }
        if (cmd.getName().equalsIgnoreCase("setwarp")) {
            if (!sender.hasPermission("ep.setwarp")) {
                sender.sendMessage(ChatColor.RED + "You do not have permission to use that command!");
                return true;
            }
            Player p = (Player) sender;
            if (args.length == 0) {
                p.sendMessage(ChatColor.RED + "Please specify a name!");
                return true;
            }
            plugin.getwarps().set("warps." + args[0] + ".world", p.getLocation().getWorld().getName());
            plugin.getwarps().set("warps." + args[0] + ".x", p.getLocation().getX());
            plugin.getwarps().set("warps." + args[0] + ".y", p.getLocation().getY());
            plugin.getwarps().set("warps." + args[0] + ".z", p.getLocation().getZ());
            plugin.getplayers().set((p.getName()) + "warps." + args[0] + ".yaw", p.getLocation().getYaw());
            plugin.getplayers().set((p.getName()) + "warps." + args[0] + ".pitch", p.getLocation().getPitch());
            plugin.saveYamls();
            p.sendMessage(plugin.prefix + ChatColor.GREEN + "Warp " + args[0] + " set!");
        }
        if (cmd.getName().equalsIgnoreCase("delwarp")) {
            if (!sender.hasPermission("ep.deletewarp")) {
                sender.sendMessage(ChatColor.RED + "You do not have permission to use that command!");
                return true;
            }
            if (args.length == 0) {
                sender.sendMessage(ChatColor.RED + "Please specify a warp name!");
                return true;
            }
            if (plugin.getwarps().getConfigurationSection("warps." + args[0]) == null) {
                sender.sendMessage(ChatColor.RED + "Warp " + args[0] + " does not exist!");
                return true;
            }
            plugin.getwarps().set("warps." + args[0], null);
            plugin.saveYamls();
            sender.sendMessage(ChatColor.GREEN + "Removed warp " + args[0] + "!");
            return true;
        }
        return false;
    }
}

