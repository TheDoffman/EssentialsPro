package org.hoffmantv.essentialspro.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.hoffmantv.essentialspro.EssentialsPro;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SignListener implements Listener {

    private final EssentialsPro plugin;
    private final Set<Material> signMaterials;

    public SignListener(EssentialsPro plugin) {
        this.plugin = plugin;
        // Initialize supported sign materials
        this.signMaterials = new HashSet<>(Arrays.asList(
                Material.OAK_SIGN, Material.OAK_WALL_SIGN,
                Material.SPRUCE_SIGN, Material.SPRUCE_WALL_SIGN,
                Material.BIRCH_SIGN, Material.BIRCH_WALL_SIGN,
                Material.JUNGLE_SIGN, Material.JUNGLE_WALL_SIGN,
                Material.ACACIA_SIGN, Material.ACACIA_WALL_SIGN,
                Material.DARK_OAK_SIGN, Material.DARK_OAK_WALL_SIGN,
                Material.CRIMSON_SIGN, Material.CRIMSON_WALL_SIGN,
                Material.WARPED_SIGN, Material.WARPED_WALL_SIGN
        ));
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();

        // Check if the clicked block is a sign
        if (block != null && signMaterials.contains(block.getType())) {
            Sign sign = (Sign) block.getState();
            Player player = event.getPlayer();

            // Check if the first line of the sign contains "[Spawn]"
            if (sign.getLine(0).equalsIgnoreCase("[Spawn]")) {
                handleSpawnTeleport(player);
            }
        }
    }

    private void handleSpawnTeleport(Player player) {
        Location spawnLocation = plugin.getSpawnLocation();

        // Check if the spawn location is set
        if (spawnLocation != null) {
            player.teleport(spawnLocation);
            player.sendMessage(Component.text("Teleported to spawn.", NamedTextColor.GREEN));
        } else {
            player.sendMessage(Component.text("The spawn location is not set.", NamedTextColor.RED));
        }
    }
}