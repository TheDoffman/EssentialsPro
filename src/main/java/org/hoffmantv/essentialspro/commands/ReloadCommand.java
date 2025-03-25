package org.hoffmantv.essentialspro.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * ReloadCommand allows players with the proper permission to reload the plugin's configuration.
 * Upon successful reload, the configuration is updated and a confirmation message is sent.
 */
public class ReloadCommand implements CommandExecutor {

    private static final String RELOAD_PERMISSION = "essentialspro.reload";
    private static final Component NO_PERMISSION_MSG = Component.text("✖ You don't have permission to use this command.", NamedTextColor.RED);
    private static final Component RELOAD_SUCCESS_MSG = Component.text("✔ EssentialsPro configuration reloaded!", NamedTextColor.GREEN);

    private final JavaPlugin plugin;

    /**
     * Constructs a new ReloadCommand.
     *
     * @param plugin The plugin instance.
     */
    public ReloadCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Executes the reload command.
     * Checks for permissions, reloads the config, and sends appropriate messages.
     *
     * @param sender  The command sender.
     * @param command The command.
     * @param label   The command label.
     * @param args    Command arguments.
     * @return true after processing.
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Verify permission for reloading configuration
        if (!sender.hasPermission(RELOAD_PERMISSION)) {
            sender.sendMessage(NO_PERMISSION_MSG);
            return true;
        }

        try {
            // Reload the plugin configuration file
            plugin.reloadConfig();
            sender.sendMessage(RELOAD_SUCCESS_MSG);
            plugin.getLogger().info(sender.getName() + " reloaded the configuration.");
        } catch (Exception e) {
            // Inform the sender if reloading fails and log the error
            sender.sendMessage(Component.text("✖ Failed to reload the configuration: " + e.getMessage(), NamedTextColor.RED));
            e.printStackTrace();
        }

        return true;
    }
}