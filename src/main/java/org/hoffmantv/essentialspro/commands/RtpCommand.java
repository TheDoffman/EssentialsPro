package org.hoffmantv.essentialspro.commands;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.hoffmantv.essentialspro.util.MessageUtils;
import org.hoffmantv.essentialspro.util.Permissions;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RtpCommand implements CommandExecutor {

    private final JavaPlugin plugin;

    public RtpCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(MessageUtils.onlyPlayers());
            return true;
        }
        if (!player.hasPermission(Permissions.RTP)) {
            player.sendMessage(MessageUtils.noPermission());
            return true;
        }

        World world = player.getWorld();
        List<String> allowedWorlds = plugin.getConfig().getStringList("rtp.worlds");
        if (!allowedWorlds.isEmpty() && !allowedWorlds.contains(world.getName())) {
            player.sendMessage(MessageUtils.error("Random teleport is disabled in this world."));
            return true;
        }

        int minRadius = plugin.getConfig().getInt("rtp.minRadius", 1000);
        int maxRadius = plugin.getConfig().getInt("rtp.maxRadius", 5000);
        int attempts = plugin.getConfig().getInt("rtp.attempts", 10);

        Location target = findSafeLocation(world, minRadius, maxRadius, attempts);
        if (target == null) {
            player.sendMessage(MessageUtils.error("Failed to find a safe location. Try again."));
            return true;
        }

        player.teleport(target);
        player.sendMessage(MessageUtils.success("Teleported to a random location."));
        return true;
    }

    private Location findSafeLocation(World world, int minRadius, int maxRadius, int attempts) {
        int min = Math.min(minRadius, maxRadius);
        int max = Math.max(minRadius, maxRadius);
        for (int i = 0; i < attempts; i++) {
            int x = randomCoord(min, max) + world.getSpawnLocation().getBlockX();
            int z = randomCoord(min, max) + world.getSpawnLocation().getBlockZ();
            Block highest = world.getHighestBlockAt(x, z);
            if (isSafeSpawn(highest)) {
                return highest.getLocation().add(0.5, 1, 0.5);
            }
        }
        return null;
    }

    private int randomCoord(int minRadius, int maxRadius) {
        int value = ThreadLocalRandom.current().nextInt(minRadius, maxRadius + 1);
        return ThreadLocalRandom.current().nextBoolean() ? value : -value;
    }

    private boolean isSafeSpawn(Block highest) {
        Material type = highest.getType();
        if (type.isAir() || type.isEmpty() || !type.isSolid()) {
            return false;
        }
        return highest.getRelative(0, 1, 0).getType().isAir();
    }
}
