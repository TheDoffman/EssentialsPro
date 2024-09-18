package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class ReloadCommand implements CommandExecutor {

    private static final String RELOAD_PERMISSION = "essentialspro.reload";
    private static final Component NO_PERMISSION_MSG = Component.text("You don't have permission to use this command.", NamedTextColor.RED);
    private static final Component RELOAD_SUCCESS_MSG = Component.text("EssentialsPro configuration reloaded!", NamedTextColor.GREEN);

    private final JavaPlugin plugin;

    public ReloadCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission(RELOAD_PERMISSION)) {
            try {
                plugin.reloadConfig(); // Reload the configuration
                sender.sendMessage(RELOAD_SUCCESS_MSG); // Send success message
            } catch (Exception e) {
                // Handle any exceptions during config reload
                sender.sendMessage(Component.text("Failed to reload the configuration: " + e.getMessage(), NamedTextColor.RED));
                e.printStackTrace();
            }
        } else {
            sender.sendMessage(NO_PERMISSION_MSG); // Send no permission message
        }
        return true;
    }
}