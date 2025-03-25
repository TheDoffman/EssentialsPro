package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hoffmantv.essentialspro.EssentialsPro;

/**
 * A command to allow players to change their game mode.
 *
 * <p>Usage: /gamemode &lt;1|2|3|4&gt; or /gamemode &lt;c|a|s|sp&gt;
 * <br>Where:
 * <br>1 or c = Creative
 * <br>2 or a = Adventure
 * <br>3 or s = Survival
 * <br>4 or sp = Spectator</p>
 */
public class GameModeCommand implements CommandExecutor {

    private final EssentialsPro plugin;
    private static final Component USAGE_MESSAGE = Component.text("➡ Usage: /gamemode <1|2|3|4>", NamedTextColor.RED);

    public GameModeCommand(EssentialsPro plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Ensure the command sender is a player
        if (!(sender instanceof Player)) {
            sender.sendMessage(Component.text("✖ This command can only be used by players!", NamedTextColor.RED));
            return true;
        }
        Player player = (Player) sender;

        // Permission check
        if (!player.hasPermission("essentialspro.gamemode")) {
            player.sendMessage(Component.text("✖ You do not have permission to use this command.", NamedTextColor.RED));
            return true;
        }

        // Validate arguments
        if (args.length != 1) {
            player.sendMessage(USAGE_MESSAGE);
            return true;
        }

        // Parse the game mode alias
        GameMode newGameMode = parseGameModeAlias(args[0]);
        if (newGameMode == null) {
            player.sendMessage(Component.text("✖ Invalid game mode alias. Use 1, 2, 3, or 4.", NamedTextColor.RED));
            return true;
        }

        // Check if the player is already in the requested game mode
        if (player.getGameMode() == newGameMode) {
            player.sendMessage(Component.text("⚠ You are already in " + newGameMode.name() + " mode.", NamedTextColor.YELLOW));
            return true;
        }

        // Set the player's game mode and notify them
        player.setGameMode(newGameMode);
        player.sendMessage(Component.text("✔ Your game mode has been set to " + newGameMode.name() + ".", NamedTextColor.GREEN));

        return true;
    }

    /**
     * Parses a game mode alias into the corresponding GameMode.
     *
     * @param alias The alias provided by the player (e.g., "1", "c", "2", "a", "3", "s", "4", "sp").
     * @return The corresponding GameMode, or null if the alias is invalid.
     */
    private GameMode parseGameModeAlias(String alias) {
        switch (alias.toLowerCase()) {
            case "1":
            case "c":
                return GameMode.CREATIVE;
            case "2":
            case "a":
                return GameMode.ADVENTURE;
            case "3":
            case "s":
                return GameMode.SURVIVAL;
            case "4":
            case "sp":
                return GameMode.SPECTATOR;
            default:
                return null;
        }
    }
}