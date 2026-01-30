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
import org.hoffmantv.essentialspro.util.MessageUtils;
import org.hoffmantv.essentialspro.util.PlayerLookup;
import org.hoffmantv.essentialspro.util.Permissions;

/**
 * InventorySeeCommand allows a player to view the inventory of another online player.
 * Usage: /inventorysee <player>
 */
public class InventorySeeCommand implements CommandExecutor {

    // Pre-defined messages using Adventure API with Unicode symbols.
    private static final Component ONLY_PLAYERS = MessageUtils.onlyPlayers();
    private static final Component NO_PERMISSION = MessageUtils.noPermission();
    private static final Component USAGE = Component.text("✖ Usage: /inventorysee <player>", NamedTextColor.RED);
    private static final Component PLAYER_NOT_FOUND = MessageUtils.playerNotFound();
    private static final Component VIEW_SUCCESS = Component.text("✔ You are now viewing ", NamedTextColor.GREEN);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Ensure the sender is a player
        if (!(sender instanceof Player)) {
            sender.sendMessage(ONLY_PLAYERS);
            return true;
        }
        Player player = (Player) sender;

        // Check for permission
        if (!player.hasPermission(Permissions.INVENTORYSEE)) {
            player.sendMessage(NO_PERMISSION);
            return true;
        }

        // Validate that exactly one argument is provided
        if (args.length != 1) {
            player.sendMessage(USAGE);
            return true;
        }

        // Retrieve the target player
        Player target = PlayerLookup.findOnlinePlayer(args[0]);
        if (target == null) {
            player.sendMessage(PLAYER_NOT_FOUND);
            return true;
        }

        // Clone the target's inventory and open it for the sender
        Inventory clonedInventory = clonePlayerInventory(target);
        player.openInventory(clonedInventory);

        // Inform the sender that they are now viewing the target's inventory
        player.sendMessage(VIEW_SUCCESS.append(Component.text(target.getName(), NamedTextColor.GREEN))
                .append(Component.text("'s inventory.", NamedTextColor.GREEN)));
        return true;
    }

    /**
     * Creates a clone of the target player's inventory.
     *
     * @param target The player whose inventory should be cloned.
     * @return A new Inventory containing clones of the target's item stacks.
     */
    private Inventory clonePlayerInventory(Player target) {
        // Create an inventory with 45 slots and a title using the target's name
        Inventory clonedInventory = Bukkit.createInventory(null, 45,
                Component.text(target.getName() + "'s Inventory", NamedTextColor.DARK_GRAY));

        // Copy items from the target's inventory
        ItemStack[] contents = target.getInventory().getContents();
        for (int i = 0; i < contents.length; i++) {
            ItemStack item = contents[i];
            if (item != null) {
                clonedInventory.setItem(i, item.clone());
            }
        }
        return clonedInventory;
    }
}