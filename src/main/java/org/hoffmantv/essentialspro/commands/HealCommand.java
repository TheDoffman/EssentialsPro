package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
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
            sender.sendMessage(Component.text("✖ This command can only be used by players.", NamedTextColor.RED));
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("ep.heal")) {
            player.sendMessage(Component.text("✖ You don't have permission to use this command.", NamedTextColor.RED));
            return true;
        }

        Player target = player;

        // Check if the player wants to heal another player
        if (args.length > 0 && player.hasPermission("essentialspro.heal.others")) {
            target = Bukkit.getPlayer(args[0]);
            if (target == null || !target.isOnline()) {
                player.sendMessage(Component.text("✖ Player not found or not online.", NamedTextColor.RED));
                return true;
            }
            player.sendMessage(Component.text("✔ You have healed " + target.getName() + ".", NamedTextColor.GREEN));
            target.sendMessage(Component.text("✔ You have been healed by " + player.getName() + ".", NamedTextColor.GREEN));
        } else {
            player.sendMessage(Component.text("✔ You have healed yourself.", NamedTextColor.GREEN));
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
        if (block != null && block.getBlockData() instanceof org.bukkit.block.data.type.Sign) {
            Sign sign = (Sign) block.getState();

            // Check if the first line of the sign is "[Heal]"
            if (sign.getLine(0).equalsIgnoreCase("[Heal]")) {
                Player player = event.getPlayer();

                // Heal the player
                healPlayer(player);

                player.sendMessage(Component.text("✔ You have healed yourself via the healing sign.", NamedTextColor.GREEN));
            }
        }
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        if (event.getLine(0).equalsIgnoreCase("[Heal]")) {
            event.setLine(0, "");
            event.setLine(1, Component.text("[Heal]").color(NamedTextColor.GREEN).toString());
        }
    }
}