package me.hoffman.essentialspro.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import me.hoffman.essentialspro.main.Main;

public class Invsee
implements CommandExecutor {
    private Inventory inv;
    private Main plugin = Main.getPlugin(Main.class);

    public Invsee(Main plugin) {
        Bukkit.getPluginCommand("invsee").setExecutor(this);
    }

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("invsee")) {
            if (!sender.hasPermission("ep.invsee")) {
                sender.sendMessage(ChatColor.RED + "You do not have permission to use that command!");
                return true;
            }
            if (!(sender instanceof Player)) {
                sender.sendMessage(plugin.prefix + ChatColor.RED + "The console cannot use /invsee");
                return true;
            }
            Player p = (Player)sender;
            if (args.length == 0) {
                p.sendMessage(plugin.prefix + ChatColor.RED + "Invalid Syntax! Usage: /invsee <player...>");
                return false;
            }
            if (args.length == 1) {
                Player target = Bukkit.getServer().getPlayer(args[0]);
                if (target == null) {
                    p.sendMessage(plugin.prefix + ChatColor.RED + "That player is not online");
                    return false;
                }
                this.inv = target.getInventory();
                p.openInventory(this.inv);
                return true;
            }
        }
        return false;
    }
}

