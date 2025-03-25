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

/**
 * SpawnCommand sets the server spawn location to the player's current location and teleports players to the spawn.
 * It also handles sign changes: when a sign's first line is "[Spawn]", it formats it for clarity.
 */
public class SpawnCommand implements CommandExecutor, Listener {

    private static final String PERMISSION_SPAWN = "essentialspro.spawn";

    // Predefined messages using Adventure API with Unicode symbols
    private static final Component MSG_ONLY_PLAYERS = Component.text("✖ This command can only be used by players.", NamedTextColor.RED);
    private static final Component MSG_NO_PERMISSION = Component.text("✖ You don't have permission to use this command.", NamedTextColor.RED);
    private static final Component MSG_NO_SPAWN_SET = Component.text("✖ The spawn location is not set.", NamedTextColor.RED);
    private static final Component MSG_WELCOME_SPAWN = Component.text("✔ Welcome back to the spawn!", NamedTextColor.GREEN);

    private final EssentialsPro plugin;
    private final LegacyComponentSerializer legacySerializer = LegacyComponentSerializer.legacySection();

    /**
     * Constructs a new SpawnCommand and registers the event listeners.
     *
     * @param plugin The main plugin instance.
     */
    public SpawnCommand(EssentialsPro plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    /**
     * Executes the /spawn command.
     * If the spawn location is set in the config, teleports the player there.
     *
     * @param sender  The command sender.
     * @param command The command.
     * @param label   The command label.
     * @param args    Command arguments (none expected).
     * @return true after processing.
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Check for permission first
        if (!sender.hasPermission(PERMISSION_SPAWN)) {
            sender.sendMessage(MSG_NO_PERMISSION);
            return true;
        }

        // Ensure the sender is a player
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

    /**
     * When a player respawns, set their respawn location to the configured spawn.
     *
     * @param event The PlayerRespawnEvent.
     */
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Location spawnLocation = plugin.getSpawnLocation();
        if (spawnLocation != null) {
            event.setRespawnLocation(spawnLocation);
        }
    }

    /**
     * Formats signs when they are changed.
     * If the first line is "[Spawn]", it clears that line and sets the second line to a green "[Spawn]".
     *
     * @param event The SignChangeEvent.
     */
    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        String firstLine = event.getLine(0);
        if (firstLine != null && firstLine.equalsIgnoreCase("[Spawn]")) {
            // Build a green "[Spawn]" component and convert it to legacy format for the sign
            Component spawnText = Component.text("[Spawn]", NamedTextColor.GREEN);
            String legacySpawnText = legacySerializer.serialize(spawnText);

            event.setLine(0, ""); // Clear first line
            event.setLine(1, legacySpawnText);
        }
    }
}