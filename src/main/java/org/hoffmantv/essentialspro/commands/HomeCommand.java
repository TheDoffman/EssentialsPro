package org.hoffmantv.essentialspro.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.hoffmantv.essentialspro.EssentialsPro;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class HomeCommand implements CommandExecutor {

    public HomeCommand(EssentialsPro plugin) {
        this.plugin = plugin;
    }

    public void reloadHomesConfig() {
        homesConfig = YamlConfiguration.loadConfiguration(homesFile);
    }
    private File homesFile = new File("plugins/EssentialsPro/homes.yml");
    private YamlConfiguration homesConfig = YamlConfiguration.loadConfiguration(homesFile);
    private EssentialsPro plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can execute this command!");
            return true;
        }
        Player player = (Player) sender;
        UUID playerUuid = player.getUniqueId();
// The /delhome command
        if (command.getName().equalsIgnoreCase("delhome")) {
            if (!player.hasPermission("essentialspro.delhome")) {
                player.sendMessage("§c✗ Insufficient Permissions: You do not have permission to delete a home.");
                return true;
            }
            if (args.length == 0) {
                player.sendMessage("§c✗ Incorrect Usage! Try §e/delhome <name>");
                return true;
            }
            String homeName = args[0].toLowerCase(); // convert home name to lowercase
            String playerHomePath = playerUuid.toString().toLowerCase();
            if (!homesConfig.contains(playerHomePath + "." + homeName)) {
                player.sendMessage("§c✗ Home Not Found: §e" + homeName);
                return true;
            }

            homesConfig.set(playerHomePath + "." + homeName, null);

            try {
                homesConfig.save(homesFile);  // save the config to the file
                this.reloadHomesConfig();  // reload the config
                player.sendMessage("§a✓ Home Deleted: §e" + homeName);
            } catch (IOException e) {
                player.sendMessage("§c✗ Error: Could not delete home. Please contact an admin.");
                e.printStackTrace();
            }

            return true;
        }

// The /homes command
        if (command.getName().equalsIgnoreCase("homes")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("§c✗ Only players can execute this command!");
                return true;
            }

            if (!player.hasPermission("essentialspro.homes")) {
                player.sendMessage("§c✗ Insufficient Permissions: You do not have permission to list homes.");
                return true;
            }

            String playerUUID = player.getUniqueId().toString();
            ConfigurationSection playerHomes = homesConfig.getConfigurationSection(playerUuid.toString().toLowerCase());

            if (playerHomes == null) {
                player.sendMessage("§c✗ No Homes: You have not set any homes yet.");
                return true;
            }

            StringBuilder homesList = new StringBuilder("§aYour Homes: §e");
            for (String homeName : playerHomes.getKeys(false)) {
                homesList.append(homeName).append(", ");
            }
            // remove trailing comma and space
            if (homesList.length() > 0) {
                homesList.setLength(homesList.length() - 2);
            }

            player.sendMessage(homesList.toString());

            return true;
        }


// The /sethome command
        if (command.getName().equalsIgnoreCase("sethome")) {
            if (!player.hasPermission("essentialspro.sethome")) {
                player.sendMessage("§c✗ Insufficient Permissions: You do not have permission to set a home.");
                return true;
            }
            if (args.length == 0) {
                player.sendMessage("§c✗ Please specify a home name. Usage: /sethome <name>");
                return true;
            }
            String homeName = args[0].toLowerCase(); // convert home name to lowercase
            Location currentLocation = player.getLocation(); // get player's current location

            // Check if the player already has too many homes
            ConfigurationSection playerHomes = homesConfig.getConfigurationSection(playerUuid.toString().toLowerCase());
            int homeLimitDefault = plugin.getConfig().getInt("homeLimit.default");
            int homeLimitMultiple = plugin.getConfig().getInt("homeLimit.multiple");
            int homeLimit = player.hasPermission("essentialspro.homes.multiple") ? homeLimitMultiple : homeLimitDefault;

            // Only check home limit if playerHomes is not null
            if (playerHomes != null && playerHomes.getKeys(false).size() >= homeLimit) {
                player.sendMessage("§c✗ You have already reached your maximum number of homes.");
                return true;
            }

            String homePath = playerUuid.toString().toLowerCase() + "." + homeName;
            homesConfig.set(homePath + ".world", currentLocation.getWorld().getName());
            homesConfig.set(homePath + ".x", currentLocation.getX());
            homesConfig.set(homePath + ".y", currentLocation.getY());
            homesConfig.set(homePath + ".z", currentLocation.getZ());

            try {
                homesConfig.save(homesFile);
                this.reloadHomesConfig();  // reload the config
                player.sendMessage("§a✓ Your home '" + homeName + "' has been successfully set at your current location.");
            } catch (IOException e) {
                player.sendMessage("§c✗ An error occurred while saving your home. Please contact a server administrator.");
                e.printStackTrace();
            } catch (Exception e) {
                // This will print any other exceptions that might be occurring
                player.sendMessage("§c✗ An unexpected error occurred. Please contact a server administrator.");
                e.printStackTrace();
            }

            return true;
        }


// The /home command
        else if (command.getName().equalsIgnoreCase("home")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("§c✗ Only players can execute this command!");
                return true;
            }

            if (!player.hasPermission("essentialspro.home")) {
                player.sendMessage("§c✗ Insufficient Permissions: You do not have permission to teleport to home.");
                return true;
            }
            if (args.length == 0) {
                player.sendMessage("§c✗ Please specify a home name. Usage: /home <name>");
                return true;
            }

            String homeName = args[0].toLowerCase(); // convert home name to lowercase
            String playerUUID = player.getUniqueId().toString();

            if (!homesConfig.contains(playerUuid.toString().toLowerCase() + "." + homeName)) {
                player.sendMessage("§c✗ You do not have a home with that name. Use /sethome <name> to establish your home.");
                return true;
            }

            String worldName = homesConfig.getString(playerUuid.toString().toLowerCase() + "." + homeName + ".world");
            double x = homesConfig.getDouble(playerUuid.toString().toLowerCase() + "." + homeName + ".x");
            double y = homesConfig.getDouble(playerUuid.toString().toLowerCase() + "." + homeName + ".y");
            double z = homesConfig.getDouble(playerUuid.toString().toLowerCase() + "." + homeName + ".z");

            Location home = new Location(Bukkit.getWorld(worldName), x, y, z);

            player.teleport(home);
            player.sendMessage("§a✓ Welcome to home " + homeName + "!");

            return true;
        }


        return false;
    }
}