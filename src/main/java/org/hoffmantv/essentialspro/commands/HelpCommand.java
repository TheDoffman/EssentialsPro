package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hoffmantv.essentialspro.EssentialsPro;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HelpCommand implements CommandExecutor {

    private final EssentialsPro plugin;
    private static final int PAGE_SIZE = 8;

    public HelpCommand(EssentialsPro plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Ensure the command is being used by a player
        if (!(sender instanceof Player)) {
            sender.sendMessage(Component.text("✖ This command can only be used by players.", NamedTextColor.RED));
            return true;
        }

        Player player = (Player) sender;
        List<String> allowedCommands = getAllowedCommands(player);

        // Calculate total number of pages
        int totalPages = (allowedCommands.size() + PAGE_SIZE - 1) / PAGE_SIZE;
        int page = parsePageNumber(args, totalPages);

        if (page == -1) {
            player.sendMessage(Component.text("✖ Invalid page number. Please enter a number between 1 and " + totalPages + ".", NamedTextColor.RED));
            return true;
        }

        displayCommands(player, allowedCommands, page, totalPages);
        return true;
    }

    /**
     * Fetches a list of commands the player has permission to use, sorted alphabetically.
     */
    private List<String> getAllowedCommands(Player player) {
        List<String> allowed = new ArrayList<>();
        for (String cmd : plugin.getDescription().getCommands().keySet()) {
            if (player.hasPermission("essentialspro." + cmd)) {
                allowed.add(cmd);
            }
        }
        Collections.sort(allowed, String.CASE_INSENSITIVE_ORDER);
        return allowed;
    }

    /**
     * Attempts to parse the page number from args, returns -1 if invalid.
     */
    private int parsePageNumber(String[] args, int totalPages) {
        if (args.length > 0 && args[0].matches("\\d+")) {
            int page = Integer.parseInt(args[0]);
            if (page <= 0 || page > totalPages) {
                return -1;
            }
            return page;
        }
        return 1; // Default to page 1 if no valid page is provided
    }

    /**
     * Displays the commands for the given page.
     */
    private void displayCommands(Player player, List<String> commands, int page, int totalPages) {
        player.sendMessage(Component.text("---- Available Commands (Page " + page + "/" + totalPages + ") ----", NamedTextColor.YELLOW));

        int startIndex = (page - 1) * PAGE_SIZE;
        int endIndex = Math.min(startIndex + PAGE_SIZE, commands.size());

        for (int i = startIndex; i < endIndex; i++) {
            String cmd = commands.get(i);
            String usage = getCommandUsage(cmd);
            // Build the line: "/cmd usage"
            Component line = Component.text("/")
                    .color(NamedTextColor.GREEN)
                    .append(Component.text(cmd, NamedTextColor.GREEN))
                    .append(Component.text(" "))
                    .append(Component.text(usage, NamedTextColor.YELLOW));

            player.sendMessage(line);
        }
    }

    /**
     * Retrieves the usage information for a given command name.
     */
    private String getCommandUsage(String commandName) {
        Command cmd = plugin.getCommand(commandName);
        if (cmd != null && cmd.getUsage() != null) {
            return cmd.getUsage();
        }
        return ""; // If no usage is provided, return an empty string
    }
}