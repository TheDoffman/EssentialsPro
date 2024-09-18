package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventorySeeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Ensure only players can use this command
        if (!(sender instanceof Player)) {
            sender.sendMessage(Component.text("This command can only be used by players.", NamedTextColor.RED));
            return true;
        }

        // Ensure the proper number of arguments
        if (args.length != 1) {
            sender.sendMessage(Component.text("Usage: /inventorysee <player>", NamedTextColor.RED));
            return true;
        }

        // Get the target player
        Player target = Bukkit.getPlayer(args[0]);

        // Validate target player
        if (target == null || !target.isOnline()) {
            sender.sendMessage(Component.text("Player not found or not online.", NamedTextColor.RED));
            return true;
        }

        // Clone the target player's inventory
        Inventory targetInventory = clonePlayerInventory(target);

        // Open the cloned inventory for the sender
        ((Player) sender).openInventory(targetInventory);

        return true;
    }

    // Method to clone the target player's inventory
    private Inventory clonePlayerInventory(Player target) {
        // Create a new inventory with the target player's name
        Inventory targetInventory = Bukkit.createInventory(null, 45, Component.text(target.getName() + "'s Inventory", NamedTextColor.DARK_GRAY));

        // Clone items from the target player's inventory into the new inventory
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