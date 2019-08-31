package me.thedoffman.essentialspro.commands;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import me.thedoffman.essentialspro.main.Main;

public class Vanish implements CommandExecutor {

	private Main plugin = Main.getPlugin(Main.class);

    public Vanish(Main plugin) {
        Bukkit.getPluginCommand("vanish").setExecutor(this);
    }
	
    public ArrayList<Player> inVanish = new ArrayList<Player>();

    @SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(sender instanceof Player) {
            Player player = (Player) sender;
        
            if(cmd.getName().equalsIgnoreCase("vanish")) {
                Collection<? extends Player> onlinePlayers = Bukkit.getServer().getOnlinePlayers();
                if(!inVanish.contains(player)) {
                    if(player.hasPermission("ep.vanish")) {
            
                    for(Player p : onlinePlayers ) {
                        if(p.hasPermission("ep.seevanish")){
                            p.showPlayer(player);
                       
                        } else {
                            p.hidePlayer(player);
                        }
                  
                      }
                    player.setAllowFlight(true);
                    player.setFlying(true);
                    player.sendMessage(plugin.prefix + ChatColor.GREEN + "You are now in vanish");
                    inVanish.add(player);
                    } else {
                        player.sendMessage(plugin.prefix + ChatColor.RED + "You dont have permission to do that");
                    }
                } else {
                     for(Player p : onlinePlayers ) {
                         p.showPlayer(player);
                     }
                     player.setAllowFlight(false);
                     player.setFlying(false);
                     player.sendMessage(plugin.prefix + ChatColor.GREEN + "You are now visable");
                     inVanish.remove(player);
                 
                }
            }
        
        } else {
            sender.sendMessage(plugin.prefix + ChatColor.RED + "You are not a player!");
        }
        return true;
    }

@SuppressWarnings("deprecation")
@EventHandler
public void onPlayerJoin(PlayerJoinEvent e) {
 for (Player p : inVanish) {
    if(e.getPlayer().hasPermission("ep.seevanish")) {
        e.getPlayer().showPlayer(p);
    } else {
        e.getPlayer().hidePlayer(p);
    }

 }
}
}
