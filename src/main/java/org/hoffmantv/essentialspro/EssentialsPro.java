package org.hoffmantv.essentialspro;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.hoffmantv.essentialspro.commands.*;
import org.hoffmantv.essentialspro.events.ColoredSignsEvent;
import org.hoffmantv.essentialspro.events.DeathEvent;
import org.hoffmantv.essentialspro.listeners.ChatListener;
import org.hoffmantv.essentialspro.listeners.FreezeListener;
import org.hoffmantv.essentialspro.listeners.JoinLeaveListener;
import org.hoffmantv.essentialspro.listeners.SignListener;
import org.hoffmantv.essentialspro.managers.BanManager;
import org.hoffmantv.essentialspro.managers.FreezeManager;
import org.hoffmantv.essentialspro.managers.MuteManager;
import org.hoffmantv.essentialspro.managers.TeleportRequestManager;

import java.io.File;

public class EssentialsPro extends JavaPlugin {

    private String pluginVersion;
    private Location spawnLocation;
    private BanManager banManager;
    private FreezeManager freezeManager;
    private TeleportRequestManager teleportRequestManager;
    private MuteManager muteManager;

    // Plugin enable logic
    @Override
    public void onEnable() {
        EssentialsPro instance = this;
        banManager = new BanManager();
        freezeManager = new FreezeManager();
        teleportRequestManager = new TeleportRequestManager();
        muteManager = new MuteManager(getDataFolder());

        // Register plugin metrics
        Metrics metrics = new Metrics(this, 2215);

        // Check for updates
        PluginUpdater updater = new PluginUpdater(this, 97026);
        if (!updater.isPluginUpToDate()) {
            getLogger().info("A new version is available!");
        }

        // Register listeners and commands
        registerListeners();
        registerCommands();

        // Load plugin version and spawn location
        loadPluginVersion();
        loadSpawnLocation();

        // Display enable message
        sendColoredMessage(ChatColor.WHITE + "EssentialsPro v" + pluginVersion + " has been enabled!");

        // Save the default config if it doesn't exist
        if (!new File(getDataFolder(), "config.yml").exists()) {
            saveDefaultConfig();
        }

        createNicknamesConfig();
    }

    // Plugin disable logic
    @Override
    public void onDisable() {
        sendColoredMessage(NamedTextColor.RED + "EssentialsPro v" + pluginVersion + " has been disabled.");
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
    private void registerCommands() {
        registerCommand("kick", new KickCommand());
        registerCommand("broadcast", new BroadcastCommand(this));
        registerCommand("help", new HelpCommand(this));
        registerCommand("spawn", new SpawnCommand(this));
        registerCommand("setspawn", new SetSpawnCommand(this));
        registerCommand("gamemode", new GameModeCommand(this));
        registerCommand("time", new TimeCommand(this));
        registerCommand("weather", new WeatherCommand(this));
        registerCommand("fly", new FlyCommand(this));
        registerCommand("heal", new HealCommand(this));
        registerCommand("feed", new FeedCommand(this));
        registerCommand("tp", new TeleportCommand(this));
        registerCommand("listplayers", new ListPlayersCommand());
        registerCommand("motd", new MotdCommand(this));
        registerCommand("reloadessentials", new ReloadCommand(this));
        registerCommand("smite", new SmiteCommand());
        registerCommand("clearchat", new ClearChatCommand());
        registerCommand("inventorysee", new InventorySeeCommand());
        registerCommand("message", new MessageCommand());
        registerCommand("ban", new BanCommand(banManager));
        registerCommand("unban", new UnbanCommand(banManager));
        registerCommand("firework", new FireworkCommand());
        registerCommand("repair", new RepairCommand());
        registerCommand("tpa", new TpaCommand(teleportRequestManager));
        registerCommand("tpaccept", new TpAcceptCommand(teleportRequestManager));
        registerCommand("tpdeny", new TpDenyCommand(teleportRequestManager));
        registerCommand("mute", new MuteCommand(muteManager));
        registerCommand("unmute", new UnmuteCommand(muteManager));
        registerCommand("clearinventory", new ClearInventoryCommand());
        registerCommand("ShadowMode", new ShadowModeCommand(this));
    }

    // Helper method for registering commands
    private void registerCommand(String commandName, CommandExecutor executor) {
        PluginCommand command = this.getCommand(commandName);
        if (command != null) {
            command.setExecutor(executor);
        } else {
            getLogger().severe("Failed to register command: " + commandName);
        }
    }

    // Register all listeners
    private void registerListeners() {
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new FreezeListener(freezeManager), this);
        pluginManager.registerEvents(new SignListener(this), this);
        pluginManager.registerEvents(new DeathEvent(this), this);
        pluginManager.registerEvents(new JoinLeaveListener(this), this);
        pluginManager.registerEvents(new ChatListener(muteManager), this);
        pluginManager.registerEvents(new ColoredSignsEvent(), this);
        pluginManager.registerEvents(new ShadowModeCommand(this), this);
    }

    // Create the nicknames config file
    private void createNicknamesConfig() {
        File nicknamesFile = new File(getDataFolder(), "nickname.yml");
        if (!nicknamesFile.exists()) {
            saveResource("nickname.yml", false);
        }
    }
}