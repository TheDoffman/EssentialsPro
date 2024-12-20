package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
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

    private static final Component MSG_ONLY_PLAYERS = Component.text("✖ This command can only be used by players.", NamedTextColor.RED);
    private static final Component MSG_NO_PERMISSION = Component.text("✖ You don't have permission to use this command.", NamedTextColor.RED);
    private static final Component MSG_NO_SPAWN_SET = Component.text("✖ The spawn location is not set.", NamedTextColor.RED);
    private static final Component MSG_WELCOME_SPAWN = Component.text("Welcome back to the spawn!", NamedTextColor.GREEN);

    private final EssentialsPro plugin;
    private final LegacyComponentSerializer legacySerializer = LegacyComponentSerializer.legacySection();

    public SpawnCommand(EssentialsPro plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Permission check first, so console without perm won't get player-only message
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
        // If first line is [Spawn], rewrite sign
        String line0 = event.getLine(0);
        if (line0 != null && line0.equalsIgnoreCase("[Spawn]")) {
            // Create a green "[Spawn]" component and convert it back to legacy for the sign
            Component spawnText = Component.text("[Spawn]", NamedTextColor.GREEN);
            String legacySpawnText = legacySerializer.serialize(spawnText);

            event.setLine(0, ""); // Clear first line
            event.setLine(1, legacySpawnText);
        }
    }
}