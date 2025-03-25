package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hoffmantv.essentialspro.EssentialsPro;

/**
 * SetSpawnCommand allows players with the required permission to set the server spawn location
 * to their current position. The command sends clear feedback using Adventure components.
 */
public class SetSpawnCommand implements CommandExecutor {

    // Pre-defined messages using Adventure API with Unicode symbols
    private static final Component ONLY_PLAYERS_MSG = Component.text("✖ This command can only be used by players.", NamedTextColor.RED);
    private static final Component NO_PERMISSION_MSG = Component.text("✖ You don't have permission to use this command.", NamedTextColor.RED);
    private static final Component SPAWN_SET_SUCCESS_MSG = Component.text("✔ Spawn location set successfully!", NamedTextColor.GREEN);
    private static final Component ERROR_MSG_PREFIX = Component.text("✖ An error occurred while setting the spawn location: ", NamedTextColor.RED);

    private final EssentialsPro plugin;

    /**
     * Constructs a new SetSpawnCommand.
     *
     * @param plugin The main plugin instance.
     */
    public SetSpawnCommand(EssentialsPro plugin) {
        this.plugin = plugin;
    }

    /**
     * Handles the /setspawn command.
     * Sets the spawn location to the executing player's current location.
     *
     * @param sender  The command sender.
     * @param command The command.
     * @param label   The command label.
     * @param args    Command arguments.
     * @return true once the command has been processed.
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Ensure the command sender is a player.
        if (!(sender instanceof Player)) {
            sender.sendMessage(ONLY_PLAYERS_MSG);
            return true;
        }
        Player player = (Player) sender;

        // Check for the required permission.
        if (!player.hasPermission("essentialspro.setspawn")) {
            player.sendMessage(NO_PERMISSION_MSG);
            return true;
        }

        // Attempt to set the spawn location and provide feedback.
        try {
            plugin.setSpawnLocation(player.getLocation());
            player.sendMessage(SPAWN_SET_SUCCESS_MSG);
        } catch (Exception e) {
            // Send an error message to the player and log the error.
            player.sendMessage(ERROR_MSG_PREFIX.append(Component.text(e.getMessage(), NamedTextColor.RED)));
            plugin.getLogger().warning("Failed to set spawn location: " + e.getMessage());
        }
        return true;
    }
}