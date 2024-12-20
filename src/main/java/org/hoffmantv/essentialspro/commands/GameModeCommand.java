package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hoffmantv.essentialspro.EssentialsPro;

public class GameModeCommand implements CommandExecutor {

    private final EssentialsPro plugin;

    public GameModeCommand(EssentialsPro plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Ensure that only players can use this command
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

        // Check arguments
        if (args.length != 1) {
            player.sendMessage(Component.text("➡ Usage: /gamemode <1|2|3|4>", NamedTextColor.RED));
            return true;
        }

        // Attempt to parse the game mode alias
        GameMode gameMode = parseGameModeAlias(args[0]);
        if (gameMode == null) {
            player.sendMessage(Component.text("✖ Invalid game mode alias. Use 1, 2, 3, or 4.", NamedTextColor.RED));
            return true;
        }

        // Check if the player is already in that game mode
        if (player.getGameMode() == gameMode) {
            player.sendMessage(Component.text("⚠ You are already in " + gameMode.name() + " mode.", NamedTextColor.YELLOW));
            return true;
        }

        // Set the player's game mode
        player.setGameMode(gameMode);
        player.sendMessage(Component.text("✔ Your game mode has been set to " + gameMode.name() + ".", NamedTextColor.GREEN));

        return true;
    }

    /**
     * Converts a mode alias (1,2,3,4 or c,a,s,sp) into a corresponding GameMode.
     *
     * @param alias The alias entered by the player.
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