package org.hoffmantv.essentialspro.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class JailCommand implements CommandExecutor, Listener {

    private final JavaPlugin plugin;
    private final HashSet<UUID> jailedPlayers = new HashSet<>();
    private final HashMap<UUID, Location> previousLocations = new HashMap<>();
    private final Location jailLocation; // Location where players will be jailed

    public JailCommand(JavaPlugin plugin) {
        this.plugin = plugin;
        // Define the jail location with a Y-coordinate of -63
        jailLocation = new Location(Bukkit.getWorld("world"), 0, -63, 0);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("essentialspro.jail")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
            return true;
        }

        if (args.length != 1) {
            player.sendMessage(ChatColor.RED + "Usage: /jail <player>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(ChatColor.RED + "Player not found.");
            return true;
        }

        UUID targetUUID = target.getUniqueId();
        if (jailedPlayers.contains(targetUUID)) {
            player.sendMessage(ChatColor.RED + target.getName() + " is already jailed.");
            return true;
        }

        // Save the player's previous location
        previousLocations.put(targetUUID, target.getLocation());

        // Jail the player by teleporting them to the bedrock jail at Y -63
        jailedPlayers.add(targetUUID);
        target.teleport(jailLocation);
        target.sendMessage(ChatColor.RED + "✖ You have been jailed!");

        player.sendMessage(ChatColor.GREEN + target.getName() + " has been jailed.");
        return true;
    }

    // Handle Block Breaking in Jail
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (jailedPlayers.contains(player.getUniqueId())) {
            event.setCancelled(true); // Prevent the block break
            player.sendMessage(ChatColor.RED + "✖ You cannot break blocks while in jail.");
        }
    }

    // Handle Item Dropping in Jail
    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (jailedPlayers.contains(player.getUniqueId())) {
            event.setCancelled(true); // Prevent item dropping
            player.sendMessage(ChatColor.RED + "✖ You cannot drop items while in jail.");
        }
    }

    // Handle Movement in Jail
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (jailedPlayers.contains(player.getUniqueId())) {
            Location to = event.getTo();
            Location from = event.getFrom();

            // Only send the message when they try to move outside of the jail block
            if (to.getBlockX() != from.getBlockX() || to.getBlockZ() != from.getBlockZ() || to.getBlockY() != from.getBlockY()) {
                // Teleport them back to the jail center if they move out of the block
                player.teleport(jailLocation);
                player.sendMessage(ChatColor.RED + "✖ You cannot move while you are jailed.");
            }
        }
    }

    // Release a player from jail
    public void unJailPlayer(Player player) {
        UUID playerUUID = player.getUniqueId();
        if (jailedPlayers.contains(playerUUID)) {
            jailedPlayers.remove(playerUUID);
            player.sendMessage(ChatColor.GREEN + "✔ You have been released from jail!");

            // Teleport the player back to their previous location
            Location previousLocation = previousLocations.remove(playerUUID);
            if (previousLocation != null) {
                player.teleport(previousLocation);
            }
        }
    }
}