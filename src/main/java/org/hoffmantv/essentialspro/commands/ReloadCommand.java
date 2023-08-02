package org.hoffmantv.essentialspro.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class ReloadCommand implements CommandExecutor {

    private static final String RELOAD_PERMISSION = "essentialspro.reload";
    private static final String NO_PERMISSION_MSG = ChatColor.RED + "You don't have permission to use this command.";
    private static final String RELOAD_SUCCESS_MSG = ChatColor.GREEN + "EssentialsPro configuration reloaded!";

    private final JavaPlugin plugin;
    private File homesFile = new File("plugins/EssentialsPro/homes.yml");
    private YamlConfiguration homesConfig = YamlConfiguration.loadConfiguration(homesFile);

    public ReloadCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    public void reloadHomesConfig() {
        homesConfig = YamlConfiguration.loadConfiguration(homesFile);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission(RELOAD_PERMISSION)) {
            plugin.reloadConfig();
            this.reloadHomesConfig();
            sender.sendMessage(RELOAD_SUCCESS_MSG);
        } else {
            sender.sendMessage(NO_PERMISSION_MSG);
        }
        return true;
    }
}
