package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

/**
 * WorkbenchCommand opens a crafting table interface for the player.
 * Usage: /workbench
 */
public class WorkbenchCommand implements CommandExecutor {

    // Predefined messages using Adventure API with Unicode symbols
    private static final Component ONLY_PLAYERS_MSG = Component.text("✖ This command can only be used by players.", NamedTextColor.RED);
    private static final Component NO_PERMISSION_MSG = Component.text("✖ You don't have permission to use this command.", NamedTextColor.RED);
    private static final Component USAGE_MSG = Component.text("✖ Usage: /workbench", NamedTextColor.RED);
    private static final Component WORKBENCH_OPENED_MSG = Component.text("✔ Workbench opened.", NamedTextColor.GREEN);

    /**
     * Executes the /workbench command. Opens a crafting table GUI for the player.
     *
     * @param sender  The command sender.
     * @param command The command.
     * @param label   The command alias.
     * @param args    Command arguments (none expected).
     * @return true after processing.
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Ensure that the sender is a player
        if (!(sender instanceof Player)) {
            sender.sendMessage(ONLY_PLAYERS_MSG);
            return true;
        }
        Player player = (Player) sender;

        // Check for proper permission
        if (!player.hasPermission("essentialspro.workbench")) {
            player.sendMessage(NO_PERMISSION_MSG);
            return true;
        }

        // Validate that no extra arguments are provided
        if (args.length != 0) {
            player.sendMessage(USAGE_MSG);
            return true;
        }

        // Create a 27-slot workbench inventory (crafting table GUI)
        Inventory workbenchInventory = Bukkit.createInventory(null, 27, Component.text("Crafting", NamedTextColor.GOLD));

        // Open the inventory for the player
        player.openInventory(workbenchInventory);
        player.sendMessage(WORKBENCH_OPENED_MSG);

        return true;
    }
}