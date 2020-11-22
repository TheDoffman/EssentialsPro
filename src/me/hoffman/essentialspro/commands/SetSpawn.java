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

public class SetSpawn
implements CommandExecutor {
    private Main plugin = Main.getPlugin(Main.class);

    public SetSpawn(Main plugin) {
        Bukkit.getPluginCommand("setspawn").setExecutor(this);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        plugin.prefix = plugin.prefix.replaceAll("&", "\u00A7");
        if (cmd.getName().equalsIgnoreCase("setspawn")) {
            if (!sender.hasPermission("ep.setspawn")) {
                sender.sendMessage(ChatColor.RED + "You do not have permission to use that command!");
                return true;
            }
            if (!(sender instanceof Player)) {
                sender.sendMessage(plugin.prefix + ChatColor.RED + "The console cannot use /setspawn");
                return true;
            }
        }
        if (sender instanceof Player) {
        	Player p = (Player) sender;
            String Spawn = plugin.getlang().getString("Messages.SpawnSet");
            Spawn = Spawn.replaceAll("&", "\u00A7");
            World world = p.getWorld();
            Location loc = p.getLocation();
            world.setSpawnLocation(loc);
            p.sendMessage(plugin.prefix + Spawn);
        }
        return false;
    }
}
