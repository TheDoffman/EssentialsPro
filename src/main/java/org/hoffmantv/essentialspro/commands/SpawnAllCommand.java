package org.hoffmantv.essentialspro.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hoffmantv.essentialspro.EssentialsPro;
import org.hoffmantv.essentialspro.util.MessageUtils;
import org.hoffmantv.essentialspro.util.Permissions;

public class SpawnAllCommand implements CommandExecutor {

    private final EssentialsPro plugin;

    public SpawnAllCommand(EssentialsPro plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission(Permissions.SPAWNALL)) {
            sender.sendMessage(MessageUtils.noPermission());
            return true;
        }

        Location spawn = plugin.getSpawnLocation();
        if (spawn == null) {
            sender.sendMessage(MessageUtils.error("Spawn location is not set."));
            return true;
        }

        for (Player player : plugin.getServer().getOnlinePlayers()) {
            player.teleport(spawn);
            player.sendMessage(MessageUtils.success("Teleported to spawn."));
        }
        sender.sendMessage(MessageUtils.success("Teleported all players to spawn."));
        return true;
    }
}
