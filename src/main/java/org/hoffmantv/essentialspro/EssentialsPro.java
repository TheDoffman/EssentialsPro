package org.hoffmantv.essentialspro;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.hoffmantv.essentialspro.commands.*;
import org.hoffmantv.essentialspro.events.ColoredSignsEvent;
import org.hoffmantv.essentialspro.events.DeathEvent;
import org.hoffmantv.essentialspro.listeners.FreezeListener;
import org.hoffmantv.essentialspro.listeners.SignListener;
import org.hoffmantv.essentialspro.managers.BanManager;
import org.hoffmantv.essentialspro.managers.FreezeManager;
import org.hoffmantv.essentialspro.managers.TeleportRequestManager;

import java.io.File;

public class EssentialsPro extends JavaPlugin {

    private static EssentialsPro instance;
    private String pluginVersion;
    private Location spawnLocation;
    private BanManager banManager;
    private FreezeManager freezeManager;
    private File nicknamesFile;
    private TeleportRequestManager teleportRequestManager;


    // Plugin enable logic
    @Override
    public void onEnable() {
        instance = this;
        banManager = new BanManager();
        freezeManager = new FreezeManager();
        this.teleportRequestManager = new TeleportRequestManager();

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

        // Load plugin version and spawn location
        loadPluginVersion();
        loadSpawnLocation();

        // Display a message when the plugin is enabled
        sendColoredMessage(ChatColor.GREEN + "EssentialsPro v" + pluginVersion + " has been enabled!");

        // Save default config if it doesn't exist
        if (!new File(getDataFolder(), "config.yml").exists()) {
            saveDefaultConfig();
        }

        createNicknamesConfig();

        // Register commands and listeners
        registerCommands();

        PluginCommand freezeCommand = this.getCommand("freeze");
        if (freezeCommand != null) {
            freezeCommand.setExecutor(new FreezeCommand(freezeManager));
        } else {
            getLogger().severe("Failed to get freeze command. Make sure it's declared in your plugin.yml file.");
        }

        // Register the event listeners
        getServer().getPluginManager().registerEvents(new FreezeListener(freezeManager), this);
        getServer().getPluginManager().registerEvents(new SignListener(this), this);
        getServer().getPluginManager().registerEvents(new DeathEvent(this), this);


    }


    // Plugin disable logic
    @Override
    public void onDisable() {
        // Display a message when the plugin is disabled
        sendColoredMessage(ChatColor.RED + "EssentialsPro v" + pluginVersion + " has been disabled.");

        // Save the spawn location to the config
        saveSpawnLocation();

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
        if (spawnLocation != null) {
            reloadConfig(); // Reload the config to ensure we have the latest version
            FileConfiguration config = getConfig();
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



        // Register all commands and their executors
// Register all commands and their executors
        private void registerCommands () {
            getCommand("kick").setExecutor(new KickCommand());
            getCommand("broadcast").setExecutor(new BroadcastCommand(this));
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
            getCommand("motd").setExecutor(new MotdCommand(this));
            getCommand("reloadessentials").setExecutor(new ReloadCommand(this));
            getCommand("smite").setExecutor(new SmiteCommand());
            getCommand("clearchat").setExecutor(new ClearChatCommand());
            getCommand("inventorysee").setExecutor(new InventorySeeCommand());
            getCommand("message").setExecutor(new MessageCommand());
            getCommand("ban").setExecutor(new BanCommand(banManager));
            getCommand("unban").setExecutor(new UnbanCommand(banManager));
            getCommand("firework").setExecutor(new FireworkCommand());
            getCommand("repair").setExecutor(new RepairCommand());
            getCommand("repair").setExecutor(new RepairCommand());
            getCommand("tpa").setExecutor(new TpaCommand(teleportRequestManager));
            getCommand("tpaccept").setExecutor(new TpAcceptCommand(teleportRequestManager));
            getCommand("tpdeny").setExecutor(new TpDenyCommand(teleportRequestManager));
            this.getCommand("clearinventory").setExecutor(new ClearInventoryCommand());

        }

    private void createNicknamesConfig() {
        nicknamesFile = new File(getDataFolder(), "nickname.yml");
        if (!nicknamesFile.exists()) {
            saveResource("nickname.yml", false);
        }
    }
    }

