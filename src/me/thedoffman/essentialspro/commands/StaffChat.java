package me.thedoffman.essentialspro.commands;

import com.google.common.base.Joiner;
import me.thedoffman.essentialspro.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StaffChat
implements CommandExecutor {
    private Main plugin = Main.getPlugin(Main.class);

    public StaffChat(Main plugin) {
        Bukkit.getPluginCommand("staffchat").setExecutor(this);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        plugin.prefix = plugin.prefix.replaceAll("&", "\u00A7");
        String CE = plugin.getlang().getString("Messages.ConsoleE");
        CE = CE.replaceAll("&", "\u00A7");
        if (cmd.getName().equalsIgnoreCase("staffchat")) {
            if (sender.hasPermission("ep.staffchat")) {
                sender.sendMessage(ChatColor.RED + "You do not have permission to use that command!");
                return true;
            }
            Player player = (Player)sender;
            if (sender instanceof Player) {
                if (args.length == 0) {
                    player.sendMessage(plugin.prefix + ChatColor.RED + "Error: /staffchat <message>");
                } else {
                    String message = Joiner.on((String)" ").join((Object[])args);
                    for (Player Player2 : Bukkit.getOnlinePlayers()) {
                        if (Player2.hasPermission("ep.staffchat")) continue;
                        Player2.sendMessage(ChatColor.GREEN + "[StaffChat] " + ChatColor.WHITE + " <" + Player2.getName() + ChatColor.WHITE + "> " + ChatColor.GRAY + message);
                    }
                }
            } else {
                sender.sendMessage(plugin.prefix + CE);
            }
            return true;
        }
        return true;
    }
}

