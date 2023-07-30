package org.hoffmantv.essentialspro;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.hoffmantv.essentialspro.commands.*;
import org.hoffmantv.essentialspro.listeners.PlayerJoinListener;

import java.io.File;
import java.io.IOException;

public class EssentialsPro extends JavaPlugin {

    private static EssentialsPro instance;
    private String pluginVersion;
    private Location spawnLocation;
    private FileConfiguration nicknamesConfig;

    @Override
    public void onEnable() {
        instance = this;
        loadPluginVersion();

        // Load the spawn location from the config
        loadSpawnLocation();

        // Display colored text when the plugin loads up
        sendColoredMessage(ChatColor.GREEN + "EssentialsPro v" + pluginVersion + " has been enabled!");

        // Save the default config if it doesn't exist
        saveDefaultConfig();

        // Load nicknames.yml
        nicknamesConfig = new YamlConfiguration();
        File nicknamesFile = new File(getDataFolder(), "nicknames.yml");
        if (!nicknamesFile.exists()) {
            saveResource("nicknames.yml", false);
        }
        try {
            nicknamesConfig.load(nicknamesFile);
        } catch (IOException | org.bukkit.configuration.InvalidConfigurationException e) {
            e.printStackTrace();
        }

        // Register commands
        registerCommands();

        // Register the PlayerJoinListener
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);

        // Set the default MOTD in the config if it doesn't exist
        FileConfiguration config = getConfig();
        if (!config.contains("motd.default")) {
            config.set("motd.default", "&a[Welcome to &eOur &fMinecraft &bServer!&a]");
            saveConfig();
        }
    }

    @Override
    public void onDisable() {
        // Display colored text when the plugin is disabled
        sendColoredMessage(ChatColor.RED + "EssentialsPro v" + pluginVersion + " has been disabled.");

        // Save the spawn location to the config
        saveSpawnLocation();

        // Save nicknames.yml
        File nicknamesFile = new File(getDataFolder(), "nicknames.yml");
        try {
            nicknamesConfig.save(nicknamesFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadPluginVersion() {
        pluginVersion = getDescription().getVersion();
    }

    private void sendColoredMessage(String message) {
        Bukkit.getServer().getConsoleSender().sendMessage(message);
    }

    private void loadSpawnLocation() {
        FileConfiguration config = getConfig();
        if (config.contains("spawn")) {
            spawnLocation = config.getLocation("spawn");
        }
    }

    private void saveSpawnLocation() {
        FileConfiguration config = getConfig();
        if (spawnLocation != null) {
            config.set("spawn", spawnLocation);
            saveConfig();
        }
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public void setSpawnLocation(Location location) {
        spawnLocation = location;
        saveSpawnLocation(); // Save the spawn location immediately after setting it.
    }

    public FileConfiguration getNicknamesConfig() {
        return nicknamesConfig;
    }

    public void saveNicknamesConfig() {
        File nicknamesFile = new File(getDataFolder(), "nicknames.yml");
        try {
            nicknamesConfig.save(nicknamesFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Provide a static method to access the plugin instance
    public static EssentialsPro getInstance() {
        return instance;
    }

    // Other methods in the EssentialsPro class

    private void registerCommands() {
        getCommand("kick").setExecutor(new KickCommand());
        getCommand("broadcast").setExecutor(new BroadcastCommand());
        getCommand("help").setExecutor(new HelpCommand(this));
        getCommand("spawn").setExecutor(new SpawnCommand(this));
        getCommand("setspawn").setExecutor(new SetSpawnCommand(this));
        getCommand("gamemode").setExecutor(new GameModeCommand(this));
        getCommand("time").setExecutor(new TimeCommand(this));
        getCommand("weather").setExecutor(new WeatherCommand(this));
        getCommand("fly").setExecutor(new FlyCommand(this));
        getCommand("heal").setExecutor(new HealCommand(this));
        getCommand("feed").setExecutor(new FeedCommand(this));
        getCommand("tp").setExecutor(new TeleportCommand(this));
        getCommand("listplayers").setExecutor(new ListPlayersCommand());
        getCommand("nickname").setExecutor(new NicknameCommand(this));
        getCommand("motd").setExecutor(new MotdCommand(this));
        getCommand("reloadessentials").setExecutor(new ReloadCommand(this));
    }
}
