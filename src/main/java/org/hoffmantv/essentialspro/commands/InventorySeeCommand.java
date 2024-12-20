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

    private static final Component NO_PERMISSION = Component.text("✖ You don't have permission to use this command.", NamedTextColor.RED);
    private static final Component ONLY_PLAYERS = Component.text("✖ This command can only be used by players.", NamedTextColor.RED);
    private static final Component USAGE = Component.text("✖ Usage: /inventorysee <player>", NamedTextColor.RED);
    private static final Component PLAYER_NOT_FOUND = Component.text("✖ Player not found or not online.", NamedTextColor.RED);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Ensure only players can use this command
        if (!(sender instanceof Player)) {
            sender.sendMessage(ONLY_PLAYERS);
            return true;
        }

        Player player = (Player) sender;

        // Check for permission
        if (!player.hasPermission("essentialspro.inventorysee")) {
            player.sendMessage(NO_PERMISSION);
            return true;
        }

        // Validate arguments
        if (args.length != 1) {
            player.sendMessage(USAGE);
            return true;
        }

        // Get the target player
        Player target = Bukkit.getPlayer(args[0]);

        // Validate the target player
        if (target == null || !target.isOnline()) {
            player.sendMessage(PLAYER_NOT_FOUND);
            return true;
        }

        // Clone the target player's inventory
        Inventory targetInventory = clonePlayerInventory(target);

        // Open the cloned inventory for the sender
        player.openInventory(targetInventory);

        // Optionally notify the sender that they are now viewing the player's inventory
        player.sendMessage(Component.text("✔ You are now viewing " + target.getName() + "'s inventory.", NamedTextColor.GREEN));

        return true;
    }

    /**
     * Clones the target player's inventory into a new inventory.
     */
    private Inventory clonePlayerInventory(Player target) {
        // Create a new inventory with a custom title
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