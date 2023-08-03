package org.hoffmantv.essentialspro.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hoffmantv.essentialspro.EssentialsPro;

public class HealCommand implements CommandExecutor {

    private final EssentialsPro plugin;

    public HealCommand(EssentialsPro plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "✖ This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("ep.heal")) {
            player.sendMessage(ChatColor.RED + "✖ You don't have permission to use this command.");
            return true;
        }

        Player target = player;

        // Check if the player wants to heal another player
        if (args.length > 0 && player.hasPermission("essentialspro.heal.others")) {
            target = Bukkit.getPlayer(args[0]);
            if (target == null || !target.isOnline()) {
                player.sendMessage(ChatColor.RED + "✖ Player not found or not online.");
                return true;
            }
            player.sendMessage(ChatColor.GREEN + "✔ You have healed " + target.getName() + ".");
            target.sendMessage(ChatColor.GREEN + "✔ You have been healed by " + player.getName() + ".");
        } else {
            player.sendMessage(ChatColor.GREEN + "✔ You have healed yourself.");
        }

        // Heal the target player
        healPlayer(target);

        return true;
    }

    private void healPlayer(Player player) {
        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);
        player.setSaturation(5.0f);
        player.setFireTicks(0);
        player.getActivePotionEffects().forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));
    }
}
