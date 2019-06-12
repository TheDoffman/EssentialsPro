package me.thedoffman.essentialspro.commands;

import me.thedoffman.essentialspro.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PrivateMessage
implements CommandExecutor {
    private static Main plugin = Main.getPlugin(Main.class);

    public PrivateMessage(Main plugin) {
        Bukkit.getPluginCommand("pm").setExecutor(this);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        plugin.prefix = plugin.prefix.replaceAll("&", "\u00A7");
        if (cmd.getName().equalsIgnoreCase("pm")) {
            if (!sender.hasPermission("ep.pm")) {
                sender.sendMessage(ChatColor.RED + "You do not have permission to use that command!");
                return true;
            }
            if (sender instanceof Player) {
                Player p = (Player)sender;
                if (args.length == 0) {
                    p.sendMessage(plugin.prefix + ChatColor.RED + "Syntax: /pm <To> <Message>");
                } else {
                    @SuppressWarnings("deprecation")
					Player target = Bukkit.getPlayer(args[0]);
                    if (target != null) {
                        String message = "";
                        int i = 1;
                        while (i != args.length) {
                            message = message + args[1] + " ";
                            ++i;
                        }
                        target.sendMessage(plugin.prefix + sender.getName() + ChatColor.WHITE + " > " + "YOU " + ChatColor.WHITE + ": " + ChatColor.GREEN + message);
                    } else {
                        sender.sendMessage(plugin.prefix + ChatColor.RED + "ERROR: This player is not connected on the server!");
                    }
                }
            } else {
                sender.sendMessage(plugin.prefix + ChatColor.RED + "The console cannot use /fly");
            }
        }
        return true;
    }
}

