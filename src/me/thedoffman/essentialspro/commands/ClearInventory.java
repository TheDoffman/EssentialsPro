package me.thedoffman.essentialspro.commands;

import me.thedoffman.essentialspro.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClearInventory implements CommandExecutor {
    private Main plugin = Main.getPlugin(Main.class);

    public ClearInventory(Main plugin) {
        Bukkit.getPluginCommand("ci").setExecutor(this);
    }

    @SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player target;
        plugin.prefix = plugin.prefix.replaceAll("&", "\u00A7");
        if (cmd.getName().equalsIgnoreCase("ci")) {
            if (!sender.hasPermission("ep.clearinventory")) {
                sender.sendMessage(ChatColor.RED + "You do not have permission to use that command!");
                return true;
            }
            if (args.length == 0 && !(sender instanceof Player)) {
                String eplayer = plugin.getlang().getString(plugin.prefix + "Messages.EPlayer");
                eplayer = eplayer.replaceAll("&", "\u00A7");
                sender.sendMessage(eplayer);
                return true;
            }
        }
        if (args.length == 0) {
            target = (Player)sender;
        } else {
            target = Bukkit.getServer().getPlayer(args[0]);
            if (target == null) {
                String nplayer = plugin.getlang().getString(plugin.prefix + "Messages.NPlayer").replaceAll("%player%", args[0]);
                nplayer = nplayer.replaceAll("&", "\u00A7");
                sender.sendMessage(nplayer);
                return true;
            }
        }
        target.getInventory().clear();
        String CIS = plugin.getlang().getString("Messages.CIS").replaceAll("%targetplayer%", target.getName().toLowerCase());
        CIS = CIS.replaceAll("&", "\u00A7");
        String CIT = plugin.getlang().getString("Messages.CIT").replaceAll("%senderplayer%", sender.getName().toLowerCase());
        CIT = CIT.replaceAll("&", "\u00A7");
        sender.sendMessage(plugin.prefix + CIS);
        target.sendMessage(plugin.prefix + CIT);
        return true;
    }
}

