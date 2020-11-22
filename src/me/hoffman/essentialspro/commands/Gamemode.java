package me.hoffman.essentialspro.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.hoffman.essentialspro.main.Main;

public class Gamemode implements CommandExecutor {
    private Main plugin = Main.getPlugin(Main.class);

    public Gamemode(Main plugin) {
        Bukkit.getPluginCommand("gamemode").setExecutor(this);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        plugin.prefix = plugin.prefix.replaceAll("&", "\u00A7");
        if (cmd.getName().equalsIgnoreCase("gamemode")) {
        		if (!sender.hasPermission("ep.gamemode")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use that command!");
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.prefix + ChatColor.RED + "The console cannot use /gamemode");
            return true;
        }
        Player player = (Player)sender;
        if (args.length == 0) {
            player.sendMessage(plugin.prefix + "Gamemode " + ChatColor.GREEN + "0" + ChatColor.WHITE + ", " + ChatColor.GREEN + "1 " + ChatColor.WHITE + ", " + ChatColor.GREEN + "2 " + ChatColor.WHITE + "or " + ChatColor.GREEN + "3" + ChatColor.WHITE + ".");
            return true;
        }
        if (args.length != 1) return true;
        if (args[0].equalsIgnoreCase("0")) {
            player.setGameMode(GameMode.SURVIVAL);
            player.sendMessage(plugin.prefix + "Gamemode set to" + ChatColor.GREEN + " Survival");
            return true;
        }
        if (args[0].equalsIgnoreCase("1")) {
            player.setGameMode(GameMode.CREATIVE);
            player.sendMessage(plugin.prefix + "Gamemode set to" + ChatColor.GREEN + " Creative");
            return true;
        }
        if (args[0].equalsIgnoreCase("2")) {
            player.setGameMode(GameMode.ADVENTURE);
            player.sendMessage(plugin.prefix + "Gamemode set to" + ChatColor.GREEN + " Adventure");
            return true;
        }
        if (args[0].equalsIgnoreCase("3")) {
            player.setGameMode(GameMode.SPECTATOR);
            player.sendMessage(plugin.prefix + "GameMode set to" + ChatColor.GREEN + " Spectator");
            return true;
        } 
        player.sendMessage(plugin.prefix + ChatColor.RED + "Usage: /gamemode <name> [0:1:2:3]");
        return true;
    }
		return false; 
}
}

