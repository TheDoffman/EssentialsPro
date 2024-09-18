package org.hoffmantv.essentialspro.commands;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hoffmantv.essentialspro.EssentialsPro;

public class GameModeCommand implements CommandExecutor {

    private EssentialsPro plugin;

    public GameModeCommand(EssentialsPro plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Check if the sender is a player
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "\u274C This command can only be used by players!");
            return true;
        }

        Player player = (Player) sender;

        // Check if the player has permission to use the command
        if (!player.hasPermission("essentialspro.gamemode")) {
            player.sendMessage(ChatColor.RED + "\u274C You do not have permission to use this command.");
            return true;
        }

        // Check the number of arguments
        if (args.length != 1) {
            player.sendMessage(ChatColor.RED + "➡ Usage: /gamemode <1|2|3|4>");
            return true;
        }

        // Get the game mode alias from the first argument
        String modeAlias = args[0];

        // Map the aliases to the corresponding GameMode
        GameMode gameMode;
        switch (modeAlias.toLowerCase()) {
            case "1":
            case "c":
                gameMode = GameMode.CREATIVE;
                break;
            case "2":
            case "a":
                gameMode = GameMode.ADVENTURE;
                break;
            case "3":
            case "s":
                gameMode = GameMode.SURVIVAL;
                break;
            case "4":
            case "sp":
                gameMode = GameMode.SPECTATOR;
                break;
            default:
                player.sendMessage(ChatColor.RED + "✖ Invalid game mode alias. Use 1, 2, 3, or 4.");
                return true;
        }

        // Check if the player is already in the requested game mode
        if (player.getGameMode() == gameMode) {
            player.sendMessage(ChatColor.YELLOW + "⚠ You are already in " + gameMode.name() + " mode.");
            return true;
        }

        // Set the player's game mode
        player.setGameMode(gameMode);

        player.sendMessage(ChatColor.GREEN + "✔ Your game mode has been set to " + gameMode.name() + ".");

        return true;
    }
}
