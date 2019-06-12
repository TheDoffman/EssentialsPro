package me.thedoffman.essentialspro.signs;
 
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import me.thedoffman.essentialspro.main.Main;
 
public class HealSignEvent implements Listener {
	
    public HealSignEvent(Main plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
        @EventHandler
        public void onSignChange(SignChangeEvent e) {
        	if(e.getPlayer().hasPermission("ep.singheal")){
                if (e.getLine(0).equalsIgnoreCase("[Heal]")) {
                        e.setLine(0, ChatColor.BLUE+ChatColor.BOLD.toString() + "[Heal]");
                        e.setLine(1, ChatColor.GREEN + "Click here");
                        e.setLine(2, ChatColor.GREEN + "to get healed!");
                }
        	}
        }
       
        @EventHandler
        public void onPlayerInteract(PlayerInteractEvent e) {
        	if(e.getPlayer().hasPermission("ep.singhealuse")){
                if (!(e.getAction() == Action.RIGHT_CLICK_BLOCK)) return;
                if (e.getClickedBlock().getState() instanceof Sign) {
                        Sign s = (Sign) e.getClickedBlock().getState();
                        if (s.getLine(0).equalsIgnoreCase("[Heal]")) {
                                e.getPlayer().setHealth(20);
                                e.getPlayer().sendMessage(ChatColor.GREEN + "You were healed!");
                        }
                }
        }
        }
}