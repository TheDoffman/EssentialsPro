package org.hoffmantv.essentialspro.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.hoffmantv.essentialspro.EssentialsPro;

public class SignListener implements Listener {

    private final EssentialsPro plugin;

    public SignListener(EssentialsPro plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        if (event.getLine(0).equalsIgnoreCase("[Spawn]") || event.getLine(0).equalsIgnoreCase("[Heal]") || event.getLine(0).equalsIgnoreCase("[Feed]")) {
            if (event.getPlayer().hasPermission("essentialspro.sign.create")) {
                event.setLine(0, ChatColor.GREEN + event.getLine(0));
            } else {
                event.getPlayer().sendMessage(ChatColor.RED + "You do not have permission to create special signs.");
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        final String MSG_WELCOME_SPAWN = ChatColor.GREEN + "Welcome back to the spawn!";
        final String MSG_NO_SPAWN_SET = ChatColor.RED + "The spawn location is not set.";

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Block block = event.getClickedBlock();

        if (block.getType() != Material.OAK_WALL_SIGN && block.getType() != Material.OAK_SIGN) return;

        Sign sign = (Sign) block.getState();
        String line = ChatColor.stripColor(sign.getLine(0));

        Player player = event.getPlayer();

        if ("/spawn".equals(line) && player.hasPermission("essentialspro.spawn")) {
            Location spawnLocation = plugin.getSpawnLocation();
            if (spawnLocation != null) {
                player.teleport(spawnLocation);
                player.sendMessage(MSG_WELCOME_SPAWN);
                event.setCancelled(true);
            } else {
                player.sendMessage(MSG_NO_SPAWN_SET);
            }
        } else if ("/heal".equals(line) && player.hasPermission("essentialspro.heal")) {
            player.setHealth(player.getMaxHealth());
            event.setCancelled(true);
        } else if ("/feed".equals(line) && player.hasPermission("essentialspro.feed")) {
            player.setFoodLevel(20);
            event.setCancelled(true);
        }
    }


}
