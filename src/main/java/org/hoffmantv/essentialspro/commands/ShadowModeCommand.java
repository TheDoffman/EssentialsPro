package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * ShadowModeCommand toggles a player's "shadow mode," a state in which the player becomes invisible (via spectator mode)
 * and is prevented from performing actions like breaking/placing blocks, interacting, attacking, chatting, or moving.
 *
 * Note: Sending a message on every blocked action may be spammy; consider adding a cooldown mechanism if necessary.
 */
public class ShadowModeCommand implements CommandExecutor, Listener {

    private final JavaPlugin plugin;
    private final Set<UUID> shadowPlayers = new HashSet<>();

    // Message constants using Adventure API with Unicode symbols
    private static final Component ONLY_PLAYERS_MSG = Component.text("✖ This command can only be used by players.", NamedTextColor.RED);
    private static final Component ENTER_SHADOW_MSG = Component.text("✔ You are now in Shadow Mode. You are invisible and cannot interact with the world.", NamedTextColor.GRAY);
    private static final Component EXIT_SHADOW_MSG = Component.text("✔ You have exited Shadow Mode.", NamedTextColor.GREEN);
    private static final Component ENABLED_SHADOW_MSG = Component.text("✔ You are now in Shadow Mode!", NamedTextColor.DARK_GRAY);
    private static final Component DISABLED_SHADOW_MSG = Component.text("✔ You have left Shadow Mode.", NamedTextColor.DARK_GREEN);

    // Action-blocked messages
    private static final Component BLOCK_BREAK_MSG = Component.text("✖ You cannot break blocks in Shadow Mode!", NamedTextColor.RED);
    private static final Component BLOCK_PLACE_MSG = Component.text("✖ You cannot place blocks in Shadow Mode!", NamedTextColor.RED);
    private static final Component ITEM_INTERACT_MSG = Component.text("✖ You cannot interact with items in Shadow Mode!", NamedTextColor.RED);
    private static final Component ATTACK_MSG = Component.text("✖ You cannot attack players in Shadow Mode!", NamedTextColor.RED);
    private static final Component CHAT_MSG = Component.text("✖ You cannot chat while in Shadow Mode!", NamedTextColor.RED);
    private static final Component MOVE_MSG = Component.text("✖ You cannot move while in Shadow Mode!", NamedTextColor.RED);

    public ShadowModeCommand(JavaPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    /**
     * Toggles Shadow Mode for the player.
     * If the player is currently in Shadow Mode, it will disable it; otherwise, it will enable it.
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ONLY_PLAYERS_MSG);
            return true;
        }
        Player player = (Player) sender;

        if (shadowPlayers.contains(player.getUniqueId())) {
            disableShadowMode(player);
            player.sendMessage(EXIT_SHADOW_MSG);
        } else {
            enableShadowMode(player);
            player.sendMessage(ENTER_SHADOW_MSG);
        }
        return true;
    }

    /**
     * Enables Shadow Mode for the given player.
     *
     * @param player The player to enable Shadow Mode for.
     */
    private void enableShadowMode(Player player) {
        shadowPlayers.add(player.getUniqueId());
        player.setGameMode(GameMode.SPECTATOR);  // Switch to spectator mode for invisibility.
        player.setSilent(true);                  // Silence the player.
        player.sendMessage(ENABLED_SHADOW_MSG);
    }

    /**
     * Disables Shadow Mode for the given player.
     *
     * @param player The player to disable Shadow Mode for.
     */
    private void disableShadowMode(Player player) {
        shadowPlayers.remove(player.getUniqueId());
        player.setGameMode(GameMode.SURVIVAL);  // Revert to survival mode.
        player.setSilent(false);               // Re-enable sound.
        player.sendMessage(DISABLED_SHADOW_MSG);
    }

    // Event Listeners

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        shadowPlayers.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (shadowPlayers.contains(player.getUniqueId())) {
            event.setCancelled(true);
            player.sendMessage(BLOCK_BREAK_MSG);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (shadowPlayers.contains(player.getUniqueId())) {
            event.setCancelled(true);
            player.sendMessage(BLOCK_PLACE_MSG);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (shadowPlayers.contains(player.getUniqueId())) {
            event.setCancelled(true);
            player.sendMessage(ITEM_INTERACT_MSG);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player player && shadowPlayers.contains(player.getUniqueId())) {
            event.setCancelled(true);
            player.sendMessage(ATTACK_MSG);
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (shadowPlayers.contains(player.getUniqueId())) {
            event.setCancelled(true);
            player.sendMessage(CHAT_MSG);
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (shadowPlayers.contains(player.getUniqueId())) {
            event.setCancelled(true);
            player.sendMessage(MOVE_MSG);
        }
    }
}