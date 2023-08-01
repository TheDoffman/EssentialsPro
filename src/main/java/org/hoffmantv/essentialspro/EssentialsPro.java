package org.hoffmantv.essentialspro;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.hoffmantv.essentialspro.commands.*;
import org.hoffmantv.essentialspro.events.ColoredSignsEvent;
import org.hoffmantv.essentialspro.listeners.ChatSpamPrevention;
import org.hoffmantv.essentialspro.listeners.PlayerJoinListener;
import org.hoffmantv.essentialspro.managers.BanManager;
import org.hoffmantv.essentialspro.managers.FreezeManager;

import java.io.File;
import java.io.IOException;

public class EssentialsPro extends JavaPlugin {

    private static EssentialsPro instance;
    private String pluginVersion;
    private Location spawnLocation;
    private FileConfiguration nicknamesConfig;
    private BanManager banManager;
    private FreezeManager freezeManager;


    private int chatDelay; // Store the chatDelay value

    // Plugin enable logic
    @Override
    public void onEnable() {
        instance = this;
        banManager = new BanManager();

        int pluginId = 2215; // <-- Replace with the id of your plugin!
        Metrics metrics = new Metrics(this, pluginId);

        // Check for updates
        PluginUpdater updater = new PluginUpdater(this, 97026);
        boolean upToDate = updater.isPluginUpToDate();

        if (!upToDate) {
            // Optionally, you can perform additional actions when an update is available.
            // For example, you might want to log a warning, send a message to the console, or notify the server admins.
        }

        // Create an instance of the ColoredSignsEvent and pass 'this' (the plugin instance) to the constructor
        ColoredSignsEvent coloredSignsEvent = new ColoredSignsEvent();

        // Get the Bukkit PluginManager and register the listener
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(coloredSignsEvent, this);

        // Register the ChatSpamPrevention listener with the provided chat delay
        ChatSpamPrevention chatSpamPrevention = new ChatSpamPrevention(this);
        pluginManager.registerEvents(chatSpamPrevention, this);

        // Load plugin version and spawn location
        loadPluginVersion();
        loadSpawnLocation();
        // Load the chat delay from the configuration
        loadChatDelay();

        // Display a message when the plugin is enabled
        sendColoredMessage(ChatColor.GREEN + "EssentialsPro v" + pluginVersion + " has been enabled!");

        // Save default config if it doesn't exist
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

        // Register commands and listeners
        registerCommands();
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);

        // Set the default MOTD in the config if it doesn't exist
        FileConfiguration config = getConfig();
        if (!config.contains("motd.default")) {
            config.set("motd.default", "&a[Welcome to &eOur &fMinecraft &bServer!&a]");
            saveConfig();
        }

        // Initialize FreezeManager
        freezeManager = new FreezeManager();

    }

    // Plugin disable logic
    @Override
    public void onDisable() {
        // Display a message when the plugin is disabled
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

    // Load the plugin version from the plugin description
    private void loadPluginVersion() {
        pluginVersion = getDescription().getVersion();
    }

    // Send a colored message to the console
    private void sendColoredMessage(String message) {
        Bukkit.getServer().getConsoleSender().sendMessage(message);
    }

    // Load the spawn location from the config
    private void loadSpawnLocation() {
        FileConfiguration config = getConfig();
        if (config.contains("spawn")) {
            spawnLocation = config.getLocation("spawn");
        }
    }

    // Save the spawn location to the config
    private void saveSpawnLocation() {
        FileConfiguration config = getConfig();
        if (spawnLocation != null) {
            config.set("spawn", spawnLocation);
            saveConfig();
        }
    }

    // Get the current spawn location
    public Location getSpawnLocation() {
        return spawnLocation;
    }

    // Set the spawn location and save it to the config
    public void setSpawnLocation(Location location) {
        spawnLocation = location;
        saveSpawnLocation();
    }

    // Get the BanManager instance
    public BanManager getBanManager() {
        return banManager;
    }

    // Get the nicknames config
    public FileConfiguration getNicknamesConfig() {
        return nicknamesConfig;
    }

    // Save the nicknames config to file
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

    private void loadChatDelay() {
        FileConfiguration config = getConfig();
        chatDelay = config.getInt("chatDelay", 3); // Default value is 3 seconds
        config.set("chatDelay", chatDelay);
        saveConfig();
    }

    public int getChatDelay() {
        return chatDelay;
    }

    // Register all commands and their executors
// Register all commands and their executors
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
        getCommand("smite").setExecutor(new SmiteCommand());
        getCommand("clearchat").setExecutor(new ClearChatCommand());
        getCommand("inventorysee").setExecutor(new InventorySeeCommand());
        getCommand("message").setExecutor(new MessageCommand());
        getCommand("ban").setExecutor(new BanCommand(banManager));
        getCommand("unban").setExecutor(new UnbanCommand(banManager));
        getCommand("freeze").setExecutor(new FreezeCommand(freezeManager));

    }

}
