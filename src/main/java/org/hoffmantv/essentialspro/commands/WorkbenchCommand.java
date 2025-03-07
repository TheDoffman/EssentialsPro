package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class WorkbenchCommand implements CommandExecutor {

    private static final Component ONLY_PLAYERS = Component.text("✖ This command can only be used by players.", NamedTextColor.RED);
    private static final Component NO_PERMISSION = Component.text("✖ You don't have permission to use this command.", NamedTextColor.RED);
    private static final Component WORKBENCH_OPENED = Component.text("✔ Workbench opened.", NamedTextColor.GREEN);
    private static final Component USAGE = Component.text("✖ Usage: /workbench", NamedTextColor.RED);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Ensure the command is executed by a player
        if (!(sender instanceof Player)) {
            sender.sendMessage(ONLY_PLAYERS);
            return true;
        }

        Player player = (Player) sender;

        // Permission check (use the permission node "essentialspro.workbench")
        if (!player.hasPermission("essentialspro.workbench")) {
            player.sendMessage(NO_PERMISSION);
            return true;
        }

        // Optionally, you might check for extra arguments if needed
        if (args.length != 0) {
            player.sendMessage(USAGE);
            return true;
        }

        // Create a workbench (crafting table) inventory interface
        // Standard crafting table GUI uses a 3x3 grid (9 slots) but can be expanded for convenience
        Inventory workbenchInventory = Bukkit.createInventory(null, 9 * 3, Component.text("Crafting", NamedTextColor.GOLD));

        // Open the inventory for the player
        player.openInventory(workbenchInventory);
        player.sendMessage(WORKBENCH_OPENED);

        return true;
    }
}