package org.hoffmantv.essentialspro.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.block.Sign;
import org.bukkit.block.Block;
import org.bukkit.Material;
import org.hoffmantv.essentialspro.EssentialsPro;

public class HealCommand implements CommandExecutor, Listener {

    private final EssentialsPro plugin;

    public HealCommand(EssentialsPro plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "\u274C This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("ep.heal")) {
            player.sendMessage(ChatColor.RED + "\u274C You don't have permission to use this command.");
            return true;
        }

        Player target = player;

        // Check if the player wants to heal another player
        if (args.length > 0 && player.hasPermission("essentialspro.heal.others")) {
            target = Bukkit.getPlayer(args[0]);
            if (target == null || !target.isOnline()) {
                player.sendMessage(ChatColor.RED + "\u274C Player not found or not online.");
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

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();

        // Check if the block is a sign
        if (block != null && (block.getType() == Material.OAK_SIGN || block.getType() == Material.OAK_WALL_SIGN ||
                block.getType() == Material.SPRUCE_SIGN || block.getType() == Material.SPRUCE_WALL_SIGN ||
                block.getType() == Material.BIRCH_SIGN || block.getType() == Material.BIRCH_WALL_SIGN ||
                block.getType() == Material.JUNGLE_SIGN || block.getType() == Material.JUNGLE_WALL_SIGN ||
                block.getType() == Material.ACACIA_SIGN || block.getType() == Material.ACACIA_WALL_SIGN ||
                block.getType() == Material.DARK_OAK_SIGN || block.getType() == Material.DARK_OAK_WALL_SIGN ||
                block.getType() == Material.CRIMSON_SIGN || block.getType() == Material.CRIMSON_WALL_SIGN ||
                block.getType() == Material.WARPED_SIGN || block.getType() == Material.WARPED_WALL_SIGN)) {
            Sign sign = (Sign) block.getState();

            // Check if the first line of the sign is "[Heal]"
            if (sign.getLine(0).equalsIgnoreCase("[Heal]")) {
                Player player = event.getPlayer();

                // Heal the player
                healPlayer(player);

                player.sendMessage(ChatColor.GREEN + "✔ You have healed yourself via the healing sign.");
            }
        }
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        if (event.getLine(0).equalsIgnoreCase("[Heal]")) {
            event.setLine(0, "");
            event.setLine(1, ChatColor.GREEN + "[Heal]");
        }
    }
}
