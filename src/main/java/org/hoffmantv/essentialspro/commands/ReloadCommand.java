package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class ReloadCommand implements CommandExecutor {

    private static final String RELOAD_PERMISSION = "essentialspro.reload";
    private static final Component NO_PERMISSION_MSG = Component.text("✖ You don't have permission to use this command.", NamedTextColor.RED);
    private static final Component RELOAD_SUCCESS_MSG = Component.text("✔ EssentialsPro configuration reloaded!", NamedTextColor.GREEN);

    private final JavaPlugin plugin;

    public ReloadCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Check for the required permission
        if (!sender.hasPermission(RELOAD_PERMISSION)) {
            sender.sendMessage(NO_PERMISSION_MSG);
            return true;
        }

        try {
            // Reload the plugin's configuration file
            plugin.reloadConfig();
            sender.sendMessage(RELOAD_SUCCESS_MSG);

            // Log the successful reload to the console for debugging/tracking
            plugin.getLogger().info(sender.getName() + " reloaded the configuration.");

        } catch (Exception e) {
            // If there's an error reloading, inform the sender and log the stack trace
            sender.sendMessage(Component.text("✖ Failed to reload the configuration: " + e.getMessage(), NamedTextColor.RED));
            e.printStackTrace();
        }

        return true;
    }
}