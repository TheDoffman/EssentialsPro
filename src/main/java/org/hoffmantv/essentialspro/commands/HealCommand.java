package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
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
import org.hoffmantv.essentialspro.util.MessageUtils;
import org.hoffmantv.essentialspro.util.PlayerLookup;
import org.hoffmantv.essentialspro.util.Permissions;

/**
 * HealCommand allows players to heal themselves or others.
 * It also enables healing via designated [Heal] signs.
 */
public class HealCommand implements CommandExecutor, Listener {

    // Pre-built Adventure components for common messages
    private static final Component ONLY_PLAYERS_ERROR = MessageUtils.onlyPlayers();
    private static final Component NO_PERMISSION = MessageUtils.noPermission();
    private static final Component PLAYER_NOT_FOUND = MessageUtils.playerNotFound();
    private static final Component HEALED_SELF = Component.text("✔ You have healed yourself.", NamedTextColor.GREEN);

    private final EssentialsPro plugin;
    private final PlainTextComponentSerializer plainTextSerializer = PlainTextComponentSerializer.plainText();

    /**
     * Constructs a new HealCommand and registers necessary event listeners.
     *
     * @param plugin The main plugin instance.
     */
    public HealCommand(EssentialsPro plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    /**
     * Handles the /heal command.
     * If a player is specified (and the sender has permission), it heals that player.
     * Otherwise, it heals the sender.
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Ensure the command sender is a player
        if (!(sender instanceof Player)) {
            sender.sendMessage(ONLY_PLAYERS_ERROR);
            return true;
        }
        Player player = (Player) sender;

        // Check permission to heal oneself
        if (!player.hasPermission(Permissions.HEAL)) {
            player.sendMessage(NO_PERMISSION);
            return true;
        }

        Player target = player; // Default: heal self

        // If an argument is provided and the sender has permission to heal others, attempt to heal the specified player
        if (args.length > 0 && player.hasPermission(Permissions.HEAL_OTHERS)) {
            target = PlayerLookup.findOnlinePlayer(args[0]);
            if (target == null) {
                player.sendMessage(PLAYER_NOT_FOUND);
                return true;
            }
            player.sendMessage(Component.text("✔ You have healed " + target.getName() + ".", NamedTextColor.GREEN));
            target.sendMessage(Component.text("✔ You have been healed by " + player.getName() + ".", NamedTextColor.GREEN));
        } else {
            // Heal self feedback
            player.sendMessage(HEALED_SELF);
        }

        // Execute the healing action
        healPlayer(target);
        return true;
    }

    /**
     * Fully restores the target player's health, food level, saturation, and clears negative effects.
     *
     * @param player The player to heal.
     */
    private void healPlayer(Player player) {
        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);
        player.setSaturation(5.0f);
        player.setFireTicks(0);
        // Remove all active potion effects
        player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));
    }

    /**
     * Listens for player interactions with signs and heals the player if the sign is a [Heal] sign.
     *
     * @param event The player interact event.
     */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        if (block == null) return;

        // Check if the block is a sign
        if (block.getState() instanceof Sign sign) {
            // If the first line of the sign equals "[Heal]" (ignoring case)
            String firstLine = plainTextSerializer.serialize(sign.line(0));
            if (firstLine.trim().equalsIgnoreCase("[Heal]")) {
                Player player = event.getPlayer();
                healPlayer(player);
                player.sendMessage(Component.text("✔ You have healed yourself via the healing sign.", NamedTextColor.GREEN));
            }
        }
    }

    /**
     * When a sign is placed with [Heal] on the first line, format it to display a green "[Heal]" on the second line.
     *
     * @param event The sign change event.
     */
    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        String firstLine = plainTextSerializer.serialize(event.line(0));
        if (firstLine.trim().equalsIgnoreCase("[Heal]")) {
            // Clear the first line and set the second line with a formatted label
            event.line(0, Component.empty());
            event.line(1, Component.text("[Heal]", NamedTextColor.GREEN));
        }
    }
}