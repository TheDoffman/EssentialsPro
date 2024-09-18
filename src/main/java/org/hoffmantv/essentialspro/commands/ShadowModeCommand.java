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
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ShadowModeCommand implements CommandExecutor, Listener {

    private final JavaPlugin plugin;
    private final Set<UUID> shadowPlayers;

    public ShadowModeCommand(JavaPlugin plugin) {
        this.plugin = plugin;
        this.shadowPlayers = new HashSet<>();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Component.text("Only players can use this command.", NamedTextColor.RED));
            return true;
        }

        Player player = (Player) sender;

        if (shadowPlayers.contains(player.getUniqueId())) {
            disableShadowMode(player);
            player.sendMessage(Component.text("You have exited Shadow Mode.", NamedTextColor.GREEN));
        } else {
            enableShadowMode(player);
            player.sendMessage(Component.text("You are now in Shadow Mode. You are invisible and cannot interact with the world.", NamedTextColor.GRAY));
        }

        return true;
    }

    private void enableShadowMode(Player player) {
        shadowPlayers.add(player.getUniqueId());
        player.setGameMode(GameMode.SPECTATOR); // Makes them invisible
        player.setSilent(true); // Silences their sounds
        player.sendMessage(Component.text("You are now in Shadow Mode!", NamedTextColor.DARK_GRAY));
    }

    private void disableShadowMode(Player player) {
        shadowPlayers.remove(player.getUniqueId());
        player.setGameMode(GameMode.SURVIVAL); // Back to normal
        player.setSilent(false); // Enable their sounds again
        player.sendMessage(Component.text("You have left Shadow Mode.", NamedTextColor.DARK_GREEN));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        shadowPlayers.remove(event.getPlayer().getUniqueId());
    }

    // Prevent block breaking
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (shadowPlayers.contains(player.getUniqueId())) {
            event.setCancelled(true);
            player.sendMessage(Component.text("You cannot break blocks in Shadow Mode!", NamedTextColor.RED));
        }
    }

    // Prevent block placing
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (shadowPlayers.contains(player.getUniqueId())) {
            event.setCancelled(true);
            player.sendMessage(Component.text("You cannot place blocks in Shadow Mode!", NamedTextColor.RED));
        }
    }

    // Prevent interacting with items
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (shadowPlayers.contains(player.getUniqueId())) {
            event.setCancelled(true);
            player.sendMessage(Component.text("You cannot interact with items in Shadow Mode!", NamedTextColor.RED));
        }
    }

    // Prevent attacking others
    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            if (shadowPlayers.contains(player.getUniqueId())) {
                event.setCancelled(true);
                player.sendMessage(Component.text("You cannot attack players in Shadow Mode!", NamedTextColor.RED));
            }
        }
    }

    // Prevent chatting
    @EventHandler
    public void onPlayerChat(PlayerChatEvent event) {
        Player player = event.getPlayer();
        if (shadowPlayers.contains(player.getUniqueId())) {
            event.setCancelled(true);
            player.sendMessage(Component.text("You cannot chat while in Shadow Mode!", NamedTextColor.RED));
        }
    }

    // Prevent movement
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (shadowPlayers.contains(player.getUniqueId())) {
            event.setCancelled(true);
            player.sendMessage(Component.text("You cannot move while in Shadow Mode!", NamedTextColor.RED));
        }
    }
}