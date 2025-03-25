package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * RepairCommand allows players to repair the item held in their main hand.
 * It checks that the item is repairable (i.e. its meta is Damageable) and
 * then resets the damage. Appropriate messages are displayed using Adventure API.
 */
public class RepairCommand implements CommandExecutor {

    private static final Component ONLY_PLAYERS_ERROR = Component.text("✖ This command can only be used by players.", NamedTextColor.RED);
    private static final Component NO_PERMISSION_ERROR = Component.text("✖ You do not have permission to use this command.", NamedTextColor.RED);
    private static final Component NO_ITEM_ERROR = Component.text("✖ You are not holding any item.", NamedTextColor.RED);
    private static final Component NOT_REPAIRABLE_ERROR = Component.text("✖ This item cannot be repaired.", NamedTextColor.RED);
    private static final Component ITEM_REPAIRED = Component.text("✔ Item repaired.", NamedTextColor.GREEN);
    private static final Component NO_REPAIR_NEEDED = Component.text("ℹ The item you are holding does not need repair.", NamedTextColor.GRAY);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Ensure the sender is a player
        if (!(sender instanceof Player)) {
            sender.sendMessage(ONLY_PLAYERS_ERROR);
            return true;
        }
        Player player = (Player) sender;

        // Check permission
        if (!player.hasPermission("essentialspro.repair")) {
            player.sendMessage(NO_PERMISSION_ERROR);
            return true;
        }

        // Retrieve the item in the player's main hand
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null || item.getType().isAir()) {
            player.sendMessage(NO_ITEM_ERROR);
            return true;
        }

        // Get the item meta and check if it's repairable
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !(meta instanceof Damageable)) {
            player.sendMessage(NOT_REPAIRABLE_ERROR);
            return true;
        }

        Damageable damageable = (Damageable) meta;

        // If the item is damaged, repair it; otherwise, notify the player no repair is needed
        if (damageable.hasDamage()) {
            damageable.setDamage(0);
            item.setItemMeta(meta);
            player.sendMessage(ITEM_REPAIRED);
        } else {
            player.sendMessage(NO_REPAIR_NEEDED);
        }
        return true;
    }
}