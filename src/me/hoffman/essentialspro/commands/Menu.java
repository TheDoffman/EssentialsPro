package me.hoffman.essentialspro.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.hoffman.essentialspro.main.Main;

public class Menu
implements CommandExecutor {
    public Menu(Main plugin) {
        Bukkit.getPluginCommand("essentialspro").setExecutor(this);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player)sender;
        if (!sender.hasPermission("ep.essentialspro")) {
        if (cmd.getName().equalsIgnoreCase("essentialspro")) {
            player.sendMessage(ChatColor.GREEN + "--------------[" + ChatColor.WHITE + "EssentialsPro Command List" + ChatColor.GREEN + "]--------------");
            player.sendMessage("");
            player.sendMessage(ChatColor.GREEN + "/heal - " + ChatColor.WHITE + "Heal yourself or another player.");
            player.sendMessage(ChatColor.GREEN + "/feed - " + ChatColor.WHITE + "Feed yourself or another player.");
            player.sendMessage(ChatColor.GREEN + "/clearchat - " + ChatColor.WHITE + "Globally clear chat.");
            player.sendMessage(ChatColor.GREEN + "/teleport - " + ChatColor.WHITE + "Teleport to another player.");
            player.sendMessage(ChatColor.GREEN + "/gamemode - " + ChatColor.WHITE + "Change you gamemode between Survival, Creative and Adventure.");
            player.sendMessage(ChatColor.GREEN + "/spawn - " + ChatColor.WHITE + "Sends you to spawn.");
            player.sendMessage(ChatColor.GREEN + "/setspawn - " + ChatColor.WHITE + "Set the spawn location.");
            player.sendMessage(ChatColor.GREEN + "/ci - " + ChatColor.WHITE + "Clears players inventory.");
            player.sendMessage(ChatColor.WHITE + "Page " + ChatColor.GREEN + "1 " + ChatColor.WHITE + "of " + ChatColor.GREEN + "4");
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("1")) {
                    player.sendMessage(ChatColor.GREEN + "--------------[" + ChatColor.WHITE + "EssentialsPro Command List" + ChatColor.GREEN + "]--------------");
                    player.sendMessage("");
                    player.sendMessage(ChatColor.GREEN + "/heal - " + ChatColor.WHITE + "Heal yourself or another player.");
                    player.sendMessage(ChatColor.GREEN + "/feed - " + ChatColor.WHITE + "Feed yourself or another player.");
                    player.sendMessage(ChatColor.GREEN + "/clearchat - " + ChatColor.WHITE + "Globally clear chat.");
                    player.sendMessage(ChatColor.GREEN + "/teleport - " + ChatColor.WHITE + "Teleport to another player.");
                    player.sendMessage(ChatColor.GREEN + "/gamemode - " + ChatColor.WHITE + "Change you gamemode between Survival, Creative and Adventure.");
                    player.sendMessage(ChatColor.GREEN + "/spawn - " + ChatColor.WHITE + "Sends you to spawn.");
                    player.sendMessage(ChatColor.GREEN + "/setspawn - " + ChatColor.WHITE + "Set the spawn location.");
                    player.sendMessage(ChatColor.GREEN + "/ci - " + ChatColor.WHITE + "Clears players inventory.");
                    player.sendMessage(ChatColor.WHITE + "Page " + ChatColor.WHITE + "1 " + ChatColor.WHITE + "of " + ChatColor.GREEN + "4");
                } else if (args[0].equalsIgnoreCase("2")) {
                    player.sendMessage(ChatColor.GREEN + "--------------[" + ChatColor.WHITE + "EssentialsPro Command List" + ChatColor.GREEN + "]--------------");
                    player.sendMessage("");
                    player.sendMessage(ChatColor.GREEN + "/staffchat - " + ChatColor.WHITE + "Talk in a private chat with other staff.");
                    player.sendMessage(ChatColor.GREEN + "/kick - " + ChatColor.WHITE + "Kick a player.");
                    player.sendMessage(ChatColor.GREEN + "/ban - " + ChatColor.WHITE + "Ban a player.");
                    player.sendMessage(ChatColor.GREEN + "/unban - " + ChatColor.WHITE + "Unbans a player.");
                    player.sendMessage(ChatColor.GREEN + "/warp - " + ChatColor.WHITE + "Warp to a selcted location.");
                    player.sendMessage(ChatColor.GREEN + "/setwarp - " + ChatColor.WHITE + "Set a warp location.");
                    player.sendMessage(ChatColor.GREEN + "/delwarp - " + ChatColor.WHITE + "Removes the specified warp.");
                    player.sendMessage(ChatColor.GREEN + "/motd - " + ChatColor.WHITE + "Set the MOTD people see before joining.");
                    player.sendMessage(ChatColor.WHITE + "Page " + ChatColor.GREEN + "2 " + ChatColor.WHITE + "of " + ChatColor.GREEN + "4");
                } else if (args[0].equalsIgnoreCase("3")) {
                    player.sendMessage(ChatColor.GREEN + "--------------[" + ChatColor.BLUE + "EssentialsPro Command List" + ChatColor.GREEN + "]--------------");
                    player.sendMessage("");
                    player.sendMessage(ChatColor.GREEN + "/time - " + ChatColor.WHITE + "Set the time of the world.");
                    player.sendMessage(ChatColor.GREEN + "/weather - " + ChatColor.WHITE + "Set the weather of the world.");
                    player.sendMessage(ChatColor.GREEN + "/fly - " + ChatColor.WHITE + "Allows a player to enable and disable flying.");
                    player.sendMessage(ChatColor.GREEN + "/mute - " + ChatColor.WHITE + "Prevents a player from talking and seeing chat.");
                    player.sendMessage(ChatColor.GREEN + "/unmute - " + ChatColor.WHITE + "Allows a player to talk and see chat again.");
                    player.sendMessage(ChatColor.GREEN + "/invsee - " + ChatColor.WHITE + "Look into a players inventory.");
                    player.sendMessage(ChatColor.GREEN + "/vanish - " + ChatColor.WHITE + "Hides you in plain sight.");
                    player.sendMessage(ChatColor.GREEN + "/sethome - " + ChatColor.WHITE + "Allows you to set a home.");
                    player.sendMessage(ChatColor.WHITE + "Page " + ChatColor.GREEN + "3 " + ChatColor.WHITE + "of " + ChatColor.GREEN + "4");
                } else if (args[0].equalsIgnoreCase("4")) {
                    player.sendMessage(ChatColor.GREEN + "--------------[" + ChatColor.WHITE + "EssentialsPro Command List" + ChatColor.GREEN + "]--------------");
                    player.sendMessage("");
                    player.sendMessage(ChatColor.GREEN + "/home - " + ChatColor.WHITE + "Sends you to your home.");
                    player.sendMessage(ChatColor.GREEN + "/pm - " + ChatColor.WHITE + "Private message a player.");
                    player.sendMessage(ChatColor.GREEN + "/nickname - " + ChatColor.WHITE + "Set a new nickname.");
                    player.sendMessage(ChatColor.GREEN + "/nickname off - " + ChatColor.WHITE + "remove nickname.");
                    player.sendMessage(ChatColor.WHITE + "Page " + ChatColor.GREEN + "4 " + ChatColor.WHITE + "of " + ChatColor.GREEN + "4");
                }
            }
        }
    }
		return false;
    }
}

