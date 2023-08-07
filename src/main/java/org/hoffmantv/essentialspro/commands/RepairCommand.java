package org.hoffmantv.essentialspro.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class RepairCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "\u274C This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("essentialspro.repair")) {
            player.sendMessage(ChatColor.RED + "\u274C You do not have permission to use this command.");
            return true;
        }

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType().isAir()) {
            player.sendMessage(ChatColor.RED + "\u274C You are not holding any item.");
            return true;
        }

        if (!(item.getItemMeta() instanceof Damageable)) {
            player.sendMessage(ChatColor.RED + "\u274C This item cannot be repaired.");
            return true;
        }

        Damageable itemMeta = (Damageable) item.getItemMeta();
        if (itemMeta.hasDamage()) {
            itemMeta.setDamage(0);
            item.setItemMeta((ItemMeta) itemMeta);
            player.sendMessage(ChatColor.GREEN + "✔ Item repaired.");
        } else {
            player.sendMessage(ChatColor.GRAY + "ℹ The item you are holding does not need repair.");
        }

        return true;
    }
}
