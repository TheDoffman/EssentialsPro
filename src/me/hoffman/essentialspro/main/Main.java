package me.hoffman.essentialspro.main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import me.hoffman.essentialspro.commands.Ban;
import me.hoffman.essentialspro.commands.Broadcast;
import me.hoffman.essentialspro.commands.ClearChat;
import me.hoffman.essentialspro.commands.ClearInventory;
import me.hoffman.essentialspro.commands.Feed;
import me.hoffman.essentialspro.commands.Fly;
import me.hoffman.essentialspro.commands.Gamemode;
import me.hoffman.essentialspro.commands.Heal;
import me.hoffman.essentialspro.commands.Home;
import me.hoffman.essentialspro.commands.Invsee;
import me.hoffman.essentialspro.commands.Kick;
import me.hoffman.essentialspro.commands.Menu;
import me.hoffman.essentialspro.commands.Motd;
import me.hoffman.essentialspro.commands.Mute;
import me.hoffman.essentialspro.commands.Nicknames;
import me.hoffman.essentialspro.commands.PrivateMessage;
import me.hoffman.essentialspro.commands.SetSpawn;
import me.hoffman.essentialspro.commands.Spawn;
import me.hoffman.essentialspro.commands.StaffChat;
import me.hoffman.essentialspro.commands.TP_Position;
import me.hoffman.essentialspro.commands.Teleport;
import me.hoffman.essentialspro.commands.Time;
import me.hoffman.essentialspro.commands.UnBan;
import me.hoffman.essentialspro.commands.Warp;
import me.hoffman.essentialspro.commands.WarpList;
import me.hoffman.essentialspro.commands.Weather;
import me.hoffman.essentialspro.events.DeathEvent;
import me.hoffman.essentialspro.events.JoinEvent;
import me.hoffman.essentialspro.events.MuteEvent;
import me.hoffman.essentialspro.events.PlayerCommandProcess;
import me.hoffman.essentialspro.events.QuitEvent;
import me.hoffman.essentialspro.events.RespawnEvent;
import me.hoffman.essentialspro.main.Updater.UpdateType;
import me.hoffman.essentialspro.signs.ColoredSignsEvent;
import me.hoffman.essentialspro.signs.FeedSignEvent;
import me.hoffman.essentialspro.signs.HealSignEvent;
import me.hoffman.essentialspro.signs.SpawnSignEvent;
import me.hoffman.essentialspro.signs.TimeSignEvent;
import me.hoffman.essentialspro.signs.WarpSingEvent;

public class Main extends JavaPlugin implements Listener {
	
	// Updater
	Updater updater = new Updater(this, 97026, this.getFile(), UpdateType.DEFAULT, true);
    
    File configFile;
    File langFile;
    File playersFile;
    File warpsFile;

    FileConfiguration config;
    FileConfiguration lang;
    FileConfiguration players;
    FileConfiguration warps;
    
	@Override
    public void onDisable() {
		
        getLogger().info("Shutting down EssentialsPro...");
		
		loadYamls();
    }

	@Override
    public void onEnable() {
		
        getLogger().info("Starting up EssentialsPro...");
        getLogger().info("Constructing additional pylons!");
        getLogger().info("Loaded EssentialsPro...");
		
// commands		
		new Ban(this);
		new Warp(this);
		new Heal(this);
		new Broadcast(this);
		new Feed(this);
		new Gamemode(this);
		new Kick(this);
		new Menu(this);
		new Motd(this);
		new Mute(this);
		new Nicknames(this);
		new SetSpawn(this);
		new Spawn(this);
		new StaffChat(this);
		new Teleport(this);
		new TP_Position(this);
		new Time(this);
		new UnBan(this);
		new WarpList(this);
		new Weather(this);
		new Fly(this);
		new ClearInventory(this);
		new Invsee(this);
		new Home(this);
		new PrivateMessage(this);
		new ClearChat(this);
		
// Events		
        new JoinEvent(this);
        new QuitEvent(this);
        new MuteEvent(this);
        new WarpSingEvent(this);
        new HealSignEvent(this);
        new FeedSignEvent(this);
        new SpawnSignEvent(this);
        new PlayerCommandProcess(this);
        new ColoredSignsEvent(this);
        new TimeSignEvent(this);
        new DeathEvent(this);
        new RespawnEvent(this);
        
        new Metrics(this);
        
// YML File Setup		
        configFile = new File(getDataFolder(), "config.yml");
        langFile = new File(getDataFolder(), "language.yml");
        playersFile = new File(getDataFolder(), "players.yml");
        warpsFile = new File(getDataFolder(), "warps.yml");
        
        try {
            firstRun();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        config = new YamlConfiguration();
        lang = new YamlConfiguration();
        players = new YamlConfiguration();
        warps = new YamlConfiguration();
        loadYamls();
    }
	
    private void firstRun() throws Exception {
        if(!configFile.exists()){
            configFile.getParentFile().mkdirs();
            copy(getResource("config.yml"), configFile);
        }
        if(!langFile.exists()){
            langFile.getParentFile().mkdirs();
            copy(getResource("language.yml"), langFile);
        }
        if(!playersFile.exists()){
            playersFile.getParentFile().mkdirs();
            copy(getResource("players.yml"), playersFile);
        }
        if(!warpsFile.exists()){
            warpsFile.getParentFile().mkdirs();
            copy(getResource("warps.yml"), warpsFile);
        }
    }
    
    private void copy(InputStream in, File file) {
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len=in.read(buf))>0){
                out.write(buf,0,len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadYamls() {
        try {
            config.load(configFile);
            lang.load(langFile);
            players.load(playersFile);
            warps.load(warpsFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveYamls() {
        try {
            config.save(configFile);
            lang.save(langFile);
            players.save(playersFile);
            warps.save(warpsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public FileConfiguration getlang() {
    	
        return lang;
    }
    public FileConfiguration getplayers() {
    	
        return players;
    }
    public FileConfiguration getwarps() {
    	
        return warps;
    }
    public FileConfiguration getconfigs() {
    	return config;
    }
    
    public String prefix = ChatColor.GRAY + "[" + ChatColor.BLUE + "EP" + ChatColor.GRAY + "] " + ChatColor.RESET;
}

