package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
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

    private static final Component ONLY_PLAYERS_ERROR = Component.text("✖ This command can only be used by players.", NamedTextColor.RED);
    private static final Component NO_PERMISSION = Component.text("✖ You don't have permission to use this command.", NamedTextColor.RED);
    private static final Component PLAYER_NOT_FOUND = Component.text("✖ Player not found or not online.", NamedTextColor.RED);
    private static final Component HEALED_SELF = Component.text("✔ You have healed yourself.", NamedTextColor.GREEN);

    private final EssentialsPro plugin;

    public HealCommand(EssentialsPro plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Ensure only players can execute this command
        if (!(sender instanceof Player)) {
            sender.sendMessage(ONLY_PLAYERS_ERROR);
            return true;
        }

        Player player = (Player) sender;

        // Permission check
        if (!player.hasPermission("ep.heal")) {
            player.sendMessage(NO_PERMISSION);
            return true;
        }

        Player target = player;

        // If an argument is provided and the player has permission, attempt to heal another player
        if (args.length > 0 && player.hasPermission("essentialspro.heal.others")) {
            target = Bukkit.getPlayer(args[0]);
            if (target == null || !target.isOnline()) {
                player.sendMessage(PLAYER_NOT_FOUND);
                return true;
            }

            player.sendMessage(Component.text("✔ You have healed " + target.getName() + ".", NamedTextColor.GREEN));
            target.sendMessage(Component.text("✔ You have been healed by " + player.getName() + ".", NamedTextColor.GREEN));
        } else {
            // Heal the command sender
            player.sendMessage(HEALED_SELF);
        }

        // Perform the healing action
        healPlayer(target);

        return true;
    }

    /**
     * Heals the specified player by restoring health, food level, saturation, and removing effects.
     */
    private void healPlayer(Player player) {
        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);
        player.setSaturation(5.0f);
        player.setFireTicks(0);
        player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();

        if (block == null) return;

        // Check if the block is a sign
        if (block.getState() instanceof Sign sign) {
            // Check if the first line of the sign is "[Heal]"
            if (sign.getLine(0).equalsIgnoreCase("[Heal]")) {
                Player player = event.getPlayer();
                healPlayer(player);
                player.sendMessage(Component.text("✔ You have healed yourself via the healing sign.", NamedTextColor.GREEN));
            }
        }
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        // Check if the player can place heal signs or if it's allowed by config, etc. (optional)

        if (event.getLine(0).equalsIgnoreCase("[Heal]")) {
            // Clear the first line and set the second line to green [Heal]
            event.setLine(0, "");
            event.setLine(1, Component.text("[Heal]", NamedTextColor.GREEN).toString());
        }
    }
}