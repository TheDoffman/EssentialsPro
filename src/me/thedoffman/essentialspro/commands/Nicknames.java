package me.thedoffman.essentialspro.commands;


import me.thedoffman.essentialspro.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class Nicknames implements CommandExecutor, Listener {
    private static Main plugin = Main.getPlugin(Main.class);

    public Nicknames(Main plugin) {
        Bukkit.getPluginCommand("nickname").setExecutor(this);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("nickname")) {
          if (args.length == 0) {
            sender.sendMessage(ChatColor.BLUE + "/Nickname [Nickname] <Player>");
         }
          if (args.length == 1) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(plugin.prefix + ChatColor.RED + "The console cannot use /nickname");
              return true;
           }

            Player p = (Player)sender;
            String nick = args[0];
            if (p.hasPermission("ep.nick.use")) {
              p.sendMessage(ChatColor.RED + "You Don't Have Permission To Use /Nickname!");
              return true;
           }
            if (nick.contains("&")) {
              if (p.hasPermission("ep.nick.color")) {
                ChatColor.translateAlternateColorCodes('&', nick);
                p.setDisplayName(nick);
                plugin.getplayers().set(p.getName(), nick);
                p.sendMessage(plugin.prefix + "Your Nickname Has Been Changed To '" + nick + ChatColor.WHITE + "'.");
                return true;
             }

              p.sendMessage(ChatColor.RED + "You Don't Have Permission To Use Colours In Your Nickname!");
              return true;
           }

            p.setDisplayName(nick);
            plugin.getplayers().set(p.getName(), nick);
            plugin.saveYamls();
            p.sendMessage(plugin.prefix + "Your Nickname Has Been Changed To '" + nick + "'.");
            return true;
         }

          if (args.length == 2) {
            if (Bukkit.getPlayer(args[1]) == null) {
              sender.sendMessage(ChatColor.RED + args[1] + " Doesn't Exist!");
              return true;
           }

			Player target = Bukkit.getPlayer(args[1]);
            Player p = (Player)sender;
            if (!p.hasPermission("ep.nick.use")) {
              p.sendMessage(ChatColor.RED + "You Don't Have Permission To Use /Nickname!");
              return true;
           }
            if (!p.hasPermission("ep.nick.others")) {
              p.sendMessage(ChatColor.RED + "You Don't Have Permission To Change Others' Nicknames!");
              return true;
           }
            String nick = args[0];
            if (nick.contains("&")) {
              if (p.hasPermission("ep.nick.color")) {
                ChatColor.translateAlternateColorCodes('&', nick);
                target.setDisplayName(nick);
                plugin.getplayers().set(target.getName(), nick);
                plugin.saveYamls();
                target.sendMessage(plugin.prefix + "Your Nickname Has Been Changed To '" + nick + "'.");
                p.sendMessage(plugin.prefix + "You Have Changed " + target.getName() + "'s Nickname To '" + nick + "'.");
                return true;
             }

              p.sendMessage(ChatColor.RED + "You Don't Have Permission To Use Colours In Nicknames!");
              return true;
           }

            target.setDisplayName(nick);
            plugin.getplayers().set(target.getName(), nick);
            plugin.saveYamls();
            target.sendMessage(plugin.prefix + "Your Nickname Has Been Changed To '" + nick + "'.");
            p.sendMessage(plugin.prefix + "You Have Changed " + target.getName() + "'s Nickname To '" + nick + "'.");
            return true;
         }

            sender.sendMessage(ChatColor.RED + "/Nickname [Nickname] <Player>");
       }

        return true;
     }
}