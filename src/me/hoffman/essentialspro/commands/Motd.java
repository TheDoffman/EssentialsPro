package me.hoffman.essentialspro.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import me.hoffman.essentialspro.main.Main;

public class Motd
implements CommandExecutor,
Listener {
    private Main plugin = Main.getPlugin(Main.class);

    public Motd(Main plugin) {
        Bukkit.getPluginCommand("motd").setExecutor(this);
        Bukkit.getPluginCommand("setmotd").setExecutor(this);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onServerPing(ServerListPingEvent e) {
        String motd = plugin.getlang().getString("Messages.MOTD");
        motd = motd.replaceAll("&", "\u00A7");
        e.setMotd(motd);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        plugin.prefix = plugin.prefix.replaceAll("&", "\u00A7");
        String motdA = plugin.getlang().getString("Messages.Access");
        motdA = motdA.replaceAll("&", "\u00A7");
        if (cmd.getName().equalsIgnoreCase("motd")) {
            if (!sender.hasPermission("ep.motd")) {
                sender.sendMessage(ChatColor.RED + "You do not have permission to use that command!");
                return true;
            }
            if (!sender.hasPermission("ep.motd")) {
                sender.sendMessage(plugin.prefix + motdA);
                return true;
            }
            String system = plugin.getlang().getString("Messages.MOTD");
            system = system.replaceAll("&", "\u00A7");
            sender.sendMessage(plugin.prefix + ChatColor.GREEN + "MOTD: " + system);
            return true;
        }
        if (cmd.getName().equalsIgnoreCase("setmotd")) {
            if (!sender.hasPermission("ep.setmotd")) {
                sender.sendMessage(ChatColor.RED + "You do not have permission to use that command!");
                return true;
            }
            if (!sender.hasPermission("ep.setmotd")) {
                if (!sender.hasPermission("ep.setmotd")) {
                    sender.sendMessage(ChatColor.RED + "You do not have permission to use that command!");
                    return true;
                }
                sender.sendMessage(plugin.prefix + motdA + ChatColor.GREEN + "- Has been set.");
                return true;
            }
            if (args.length == 0) {
                sender.sendMessage(ChatColor.RED + "Error: Please type a new MOTD!");
                return true;
            }
            StringBuilder str = new StringBuilder();
            int i = 0;
            while (i < args.length) {
                str.append(args[i] + " ");
                ++i;
            }
            String motd = str.toString();
            plugin.getlang().set("Messages.MOTD", (Object)motd);
            String system = plugin.getlang().getString("Messages.MOTD");
            system = system.replaceAll("&", "\u00A7");
            sender.sendMessage(plugin.prefix + ChatColor.GREEN + "MOTD: " + system);
            plugin.saveYamls();
            return true;
        }
        return false;
    }
}

