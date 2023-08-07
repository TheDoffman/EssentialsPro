package org.hoffmantv.essentialspro.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.hoffmantv.essentialspro.EssentialsPro;

public class SpawnCommand implements CommandExecutor, Listener {

    private static final String PERMISSION_SPAWN = "essentialspro.spawn";
    private static final String MSG_ONLY_PLAYERS = ChatColor.RED + "\u274C This command can only be used by players.";
    private static final String MSG_NO_PERMISSION = ChatColor.RED + "\u274C You don't have permission to use this command.";
    private static final String MSG_NO_SPAWN_SET = ChatColor.RED + "\u274C The spawn location is not set.";
    private static final String MSG_WELCOME_SPAWN = ChatColor.GREEN + "Welcome back to the spawn!";

    private final EssentialsPro plugin;

    public SpawnCommand(EssentialsPro plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission(PERMISSION_SPAWN)) {
            sender.sendMessage(MSG_NO_PERMISSION);
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage(MSG_ONLY_PLAYERS);
            return true;
        }

        Player player = (Player) sender;
        Location spawnLocation = plugin.getSpawnLocation();

        if (spawnLocation == null) {
            player.sendMessage(MSG_NO_SPAWN_SET);
            return true;
        }

        player.teleport(spawnLocation);
        player.sendMessage(MSG_WELCOME_SPAWN);

        return true;
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Location spawnLocation = plugin.getSpawnLocation();
        if (spawnLocation != null) {
            event.setRespawnLocation(spawnLocation);
        }
    }
    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        if (event.getLine(0).equalsIgnoreCase("[Spawn]")) {
            event.setLine(1, ChatColor.GREEN + "[Spawn]");
            event.setLine(0, "");
        }
    }
}
