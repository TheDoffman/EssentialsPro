package me.thedoffman.essentialspro.signs;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import me.thedoffman.essentialspro.main.Main;

public class WarpSingEvent implements Listener {

	private  Main plugin = Main.getPlugin(Main.class);
	
    public WarpSingEvent(Main plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
	
    @EventHandler
    public void onSignChange(SignChangeEvent e) {
        if (e.getLine(0).equalsIgnoreCase("[Warp]")) {
            if (plugin.getwarps().get("warps."+e.getLine(1)) == null) {
                e.getPlayer().sendMessage(ChatColor.RED+"The specified warp does not exist! Please create a new sign with a valid warp.");
                return;
            }
        	if(e.getPlayer().hasPermission("ep.singwarp")){
            e.getPlayer().sendMessage(ChatColor.BLUE+"Successfully created a new warp sign for the warp " + ChatColor.GREEN+e.getLine(1) + ChatColor.BLUE+"!");
            e.setLine(0, ChatColor.BLUE+ChatColor.BOLD.toString() + "[Warp]");
            e.setLine(1, ChatColor.GREEN + e.getLine(1));
        }
    	}
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
    	if(e.getPlayer().hasPermission("ep.signwarpuse")){
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (e.getClickedBlock().getState() instanceof Sign) {
                Sign sign = (Sign) e.getClickedBlock().getState();

                if (sign.getLine(0).equalsIgnoreCase(ChatColor.BLUE+ChatColor.BOLD.toString()+"[Warp]")) {
                    World w = Bukkit.getServer().getWorld(plugin.getwarps().getString("warps." + ChatColor.stripColor(sign.getLine(1)) + ".world"));
                    double x = plugin.getwarps().getDouble("warps." + ChatColor.stripColor(sign.getLine(1)) + ".x");
                    double y = plugin.getwarps().getDouble("warps." + ChatColor.stripColor(sign.getLine(1)) + ".y");
                    double z = plugin.getwarps().getDouble("warps." + ChatColor.stripColor(sign.getLine(1)) + ".z");
                    e.getPlayer().teleport(new Location(w, x, y, z));
                    e.getPlayer().sendMessage(ChatColor.BLUE + "You successfully teleported to the warp: " + ChatColor.GREEN + ChatColor.stripColor(sign.getLine(1)) + ChatColor.BLUE+"!");
                }

            }
        }
        }
    }

}