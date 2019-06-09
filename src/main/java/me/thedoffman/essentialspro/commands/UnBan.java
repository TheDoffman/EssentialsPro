package me.thedoffman.essentialspro.commands;

import me.thedoffman.essentialspro.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class UnBan
implements CommandExecutor {
    private Main plugin = Main.getPlugin(Main.class);

    public UnBan(Main plugin) {
        Bukkit.getPluginCommand("unban").setExecutor(this);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        plugin.prefix = plugin.prefix.replaceAll("&", "\u00A7");
        if (cmd.getName().equalsIgnoreCase("unban")) {
            if (!sender.hasPermission("ep.unban")) {
                sender.sendMessage(ChatColor.RED + "You do not have permission to use that command!");
                return true;
            }
            if (args.length == 0) {
                sender.sendMessage(plugin.prefix + ChatColor.RED + "Please specify a player name!");
                return true;
            }
            if (!this.plugin.getplayers().getBoolean(args[0] + ".banned")) {
                sender.sendMessage(plugin.prefix + ChatColor.RED + "Player " + args[0] + " is not banned!");
                return true;
            }
            this.plugin.getplayers().set(args[0] + ".banned", false);
            this.plugin.saveYamls();
            sender.sendMessage(plugin.prefix + ChatColor.GREEN + "Unbaned " + args[0] + "!");
        }
        return true;
    }
}

