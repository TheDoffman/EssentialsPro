package org.hoffmantv.essentialspro.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventorySeeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "\u274C This command can only be used by players.");
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "\u274C Usage: /inventorysee <player>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null || !target.isOnline()) {
            sender.sendMessage(ChatColor.RED + "\u274C Player not found or not online.");
            return true;
        }

        // Clone the target player's inventory
        Inventory targetInventory = clonePlayerInventory(target);

        // Open the cloned inventory for the sender
        ((Player) sender).openInventory(targetInventory);

        return true;
    }

    private Inventory clonePlayerInventory(Player target) {
        // Create an inventory with the target player's name
        Inventory targetInventory = Bukkit.createInventory(null, 45, ChatColor.DARK_GRAY + target.getName() + "'s Inventory");

        // Clone the content from the target's inventory to the created inventory
        ItemStack[] contents = target.getInventory().getContents();
        for (int i = 0; i < contents.length; i++) {
            ItemStack item = contents[i];
            if (item != null) {
                targetInventory.setItem(i, item.clone());
            }
        }

        return targetInventory;
    }
}
