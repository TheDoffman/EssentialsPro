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

public class RepairCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Component.text("This command can only be used by players.", NamedTextColor.RED));
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("essentialspro.repair")) {
            player.sendMessage(Component.text("You do not have permission to use this command.", NamedTextColor.RED));
            return true;
        }

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType().isAir()) {
            player.sendMessage(Component.text("You are not holding any item.", NamedTextColor.RED));
            return true;
        }

        if (!(item.getItemMeta() instanceof Damageable)) {
            player.sendMessage(Component.text("This item cannot be repaired.", NamedTextColor.RED));
            return true;
        }

        Damageable itemMeta = (Damageable) item.getItemMeta();
        if (itemMeta.hasDamage()) {
            itemMeta.setDamage(0);
            item.setItemMeta((ItemMeta) itemMeta);
            player.sendMessage(Component.text("Item repaired.", NamedTextColor.GREEN));
        } else {
            player.sendMessage(Component.text("The item you are holding does not need repair.", NamedTextColor.GRAY));
        }

        return true;
    }
}