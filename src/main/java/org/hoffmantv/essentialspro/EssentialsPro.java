package org.hoffmantv.essentialspro;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.hoffmantv.essentialspro.commands.*;
import org.hoffmantv.essentialspro.events.ColoredSignsEvent;
import org.hoffmantv.essentialspro.events.DeathEvent;
import org.hoffmantv.essentialspro.listeners.*;
import org.hoffmantv.essentialspro.managers.*;

import java.io.File;

public class EssentialsPro extends JavaPlugin {

    private String pluginVersion;
    private Location spawnLocation;
    private BanManager banManager;
    private FreezeManager freezeManager;
    private TeleportRequestManager teleportRequestManager;
    private MuteManager muteManager;

    @Override
    public void onEnable() {
        // Initialize managers
        banManager = new BanManager();
        freezeManager = new FreezeManager();
        teleportRequestManager = new TeleportRequestManager();
        muteManager = new MuteManager(getDataFolder());

        // Register metrics for plugin stats
        new Metrics(this, 2215);

        // Check for updates
        PluginUpdater updater = new PluginUpdater(this, 97026);
        if (!updater.isPluginUpToDate()) {
            getLogger().info("A new version of EssentialsPro is available!");
        }

        // Register commands and listeners
        registerCommands();
        registerListeners();

        // Load plugin version and configuration
        loadPluginVersion();
        loadSpawnLocation();

        // Display plugin enabled message
        sendConsoleMessage(Component.text("EssentialsPro v" + pluginVersion + " has been enabled!", NamedTextColor.GREEN));

        // Save default config if it doesn't exist
        saveDefaultConfigIfMissing();
    }

    @Override
    public void onDisable() {
        sendConsoleMessage(Component.text("EssentialsPro v" + pluginVersion + " has been disabled.", NamedTextColor.RED));
        saveSpawnLocation();
    }

    private void loadPluginVersion() {
        pluginVersion = getDescription().getVersion();
    }

    private void sendConsoleMessage(Component message) {
        Bukkit.getServer().getConsoleSender().sendMessage(message);
    }

    private void loadSpawnLocation() {
        FileConfiguration config = getConfig();
        if (config.contains("spawn")) {
            spawnLocation = config.getLocation("spawn");
        }
    }

    private void saveSpawnLocation() {
        if (spawnLocation != null) {
            FileConfiguration config = getConfig();
            config.set("spawn", spawnLocation);
            saveConfig();
        }
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public void setSpawnLocation(Location location) {
        spawnLocation = location;
        saveSpawnLocation();
    }

    public BanManager getBanManager() {
        return banManager;
    }

    private void registerCommands() {
        registerCommand("kick", new KickCommand());
        registerCommand("broadcast", new BroadcastCommand(this));
        registerCommand("help", new HelpCommand(this));
        registerCommand("spawn", new SpawnCommand(this));
        registerCommand("setspawn", new SetSpawnCommand(this));
        registerCommand("gamemode", new GameModeCommand(this));
        registerCommand("time", new TimeCommand(this));
        registerCommand("weather", new WeatherCommand());
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
        registerCommand("shadowmode", new ShadowModeCommand(this));
        registerCommand("freezetime", new FreezeTimeCommand(this));
        registerCommand("workbench", new WorkbenchCommand());
    }

    private void registerCommand(String commandName, CommandExecutor executor) {
        PluginCommand command = this.getCommand(commandName);
        if (command != null) {
            command.setExecutor(executor);
        } else {
            getLogger().warning("Failed to register command: " + commandName);
        }
    }

    private void registerListeners() {
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new FreezeListener(freezeManager), this);
        pluginManager.registerEvents(new SignListener(this), this);
        pluginManager.registerEvents(new DeathEvent(this), this);
        pluginManager.registerEvents(new JoinLeaveListener(this), this);
        pluginManager.registerEvents(new ChatListener(muteManager), this);
        pluginManager.registerEvents(new ColoredSignsEvent(), this);
    }

    private void saveDefaultConfigIfMissing() {
        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            saveDefaultConfig();
        }
    }
}