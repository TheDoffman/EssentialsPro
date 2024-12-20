package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.GameMode;
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

public class ShadowModeCommand implements CommandExecutor, Listener {

    private final JavaPlugin plugin;
    private final Set<UUID> shadowPlayers = new HashSet<>();

    // Message constants
    private static final Component MSG_ONLY_PLAYERS = Component.text("✖ This command can only be used by players.", NamedTextColor.RED);
    private static final Component MSG_ENTER_SHADOW = Component.text("You are now in Shadow Mode. You are invisible and cannot interact with the world.", NamedTextColor.GRAY);
    private static final Component MSG_EXIT_SHADOW = Component.text("You have exited Shadow Mode.", NamedTextColor.GREEN);
    private static final Component MSG_ENABLED_SHADOW = Component.text("You are now in Shadow Mode!", NamedTextColor.DARK_GRAY);
    private static final Component MSG_DISABLED_SHADOW = Component.text("You have left Shadow Mode.", NamedTextColor.DARK_GREEN);

    // Actions blocked messages
    private static final Component MSG_BLOCK_BREAK = Component.text("You cannot break blocks in Shadow Mode!", NamedTextColor.RED);
    private static final Component MSG_BLOCK_PLACE = Component.text("You cannot place blocks in Shadow Mode!", NamedTextColor.RED);
    private static final Component MSG_ITEM_INTERACT = Component.text("You cannot interact with items in Shadow Mode!", NamedTextColor.RED);
    private static final Component MSG_ATTACK = Component.text("You cannot attack players in Shadow Mode!", NamedTextColor.RED);
    private static final Component MSG_NO_CHAT = Component.text("You cannot chat while in Shadow Mode!", NamedTextColor.RED);
    private static final Component MSG_NO_MOVE = Component.text("You cannot move while in Shadow Mode!", NamedTextColor.RED);

    public ShadowModeCommand(JavaPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MSG_ONLY_PLAYERS);
            return true;
        }

        Player player = (Player) sender;

        if (shadowPlayers.contains(player.getUniqueId())) {
            disableShadowMode(player);
            player.sendMessage(MSG_EXIT_SHADOW);
        } else {
            enableShadowMode(player);
            player.sendMessage(MSG_ENTER_SHADOW);
        }

        return true;
    }

    private void enableShadowMode(Player player) {
        shadowPlayers.add(player.getUniqueId());
        player.setGameMode(GameMode.SPECTATOR); // Makes them invisible
        player.setSilent(true); // Silences their sounds
        player.sendMessage(MSG_ENABLED_SHADOW);
    }

    private void disableShadowMode(Player player) {
        shadowPlayers.remove(player.getUniqueId());
        player.setGameMode(GameMode.SURVIVAL); // Back to normal
        player.setSilent(false); // Enable their sounds again
        player.sendMessage(MSG_DISABLED_SHADOW);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        shadowPlayers.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (shadowPlayers.contains(player.getUniqueId())) {
            event.setCancelled(true);
            player.sendMessage(MSG_BLOCK_BREAK);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (shadowPlayers.contains(player.getUniqueId())) {
            event.setCancelled(true);
            player.sendMessage(MSG_BLOCK_PLACE);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (shadowPlayers.contains(player.getUniqueId())) {
            event.setCancelled(true);
            player.sendMessage(MSG_ITEM_INTERACT);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player player) {
            if (shadowPlayers.contains(player.getUniqueId())) {
                event.setCancelled(true);
                player.sendMessage(MSG_ATTACK);
            }
        }
    }

    // Use AsyncPlayerChatEvent instead of PlayerChatEvent (deprecated)
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (shadowPlayers.contains(player.getUniqueId())) {
            event.setCancelled(true);
            player.sendMessage(MSG_NO_CHAT);
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (shadowPlayers.contains(player.getUniqueId())) {
            event.setCancelled(true);
            player.sendMessage(MSG_NO_MOVE);
        }
    }
}