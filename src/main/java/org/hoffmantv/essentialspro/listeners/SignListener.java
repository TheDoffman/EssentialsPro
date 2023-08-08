package org.hoffmantv.essentialspro.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.block.Block;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.hoffmantv.essentialspro.EssentialsPro;

public class SignListener implements Listener {

    private EssentialsPro plugin;

    public SignListener(EssentialsPro plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();

        if (block != null && (block.getType() == Material.OAK_SIGN || block.getType() == Material.OAK_WALL_SIGN ||
                block.getType() == Material.SPRUCE_SIGN || block.getType() == Material.SPRUCE_WALL_SIGN ||
                block.getType() == Material.BIRCH_SIGN || block.getType() == Material.BIRCH_WALL_SIGN ||
                block.getType() == Material.JUNGLE_SIGN || block.getType() == Material.JUNGLE_WALL_SIGN ||
                block.getType() == Material.ACACIA_SIGN || block.getType() == Material.ACACIA_WALL_SIGN ||
                block.getType() == Material.DARK_OAK_SIGN || block.getType() == Material.DARK_OAK_WALL_SIGN ||
                block.getType() == Material.CRIMSON_SIGN || block.getType() == Material.CRIMSON_WALL_SIGN ||
                block.getType() == Material.WARPED_SIGN || block.getType() == Material.WARPED_WALL_SIGN)) {
            Sign sign = (Sign) block.getState();
            Player player = event.getPlayer();

            if (sign.getLine(0).equalsIgnoreCase("[Spawn]")) {
                Location spawnLocation = plugin.getSpawnLocation();

                if (spawnLocation != null) {
                    player.teleport(spawnLocation);
                    player.sendMessage(ChatColor.GREEN + "✓ Teleported to spawn.");
                } else {
                    player.sendMessage(ChatColor.RED + "✗ The spawn location is not set.");
                }
            }
        }
    }
}
