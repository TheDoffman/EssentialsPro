package me.thedoffman.essentialspro.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;

import me.thedoffman.essentialspro.main.Main;

public class DeathEvent implements Listener {
	
	private Main plugin = Main.getPlugin(Main.class);

    public DeathEvent(Main plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		
        Entity entity = e.getEntity();
        EntityDamageEvent ldc = entity.getLastDamageCause();
        DamageCause dc = ldc.getCause();
		
        String EA = plugin.getconfigs().getString("Deathmessages.entity").replaceAll("%player%", e.getEntity().getPlayer().getName().toString());
        EA = EA.replaceAll("&", "\u00A7");
        
        String EE = plugin.getconfigs().getString("Deathmessages.explosion").replaceAll("%player%", e.getEntity().getPlayer().getName().toString());
        EE = EE.replaceAll("&", "\u00A7");
        
        String L = plugin.getconfigs().getString("Deathmessages.lightning").replaceAll("%player%", e.getEntity().getPlayer().getName().toString());
        L = L.replaceAll("&", "\u00A7");
        
        String D = plugin.getconfigs().getString("Deathmessages.drowning").replaceAll("%player%", e.getEntity().getPlayer().getName().toString());
        D = D.replaceAll("&", "\u00A7");
        
        String F = plugin.getconfigs().getString("Deathmessages.falling").replaceAll("%player%", e.getEntity().getPlayer().getName().toString());
        F = F.replaceAll("&", "\u00A7");
        
        String Fire = plugin.getconfigs().getString("Deathmessages.fire").replaceAll("%player%", e.getEntity().getPlayer().getName().toString());
        Fire = Fire.replaceAll("&", "\u00A7");
        
        String S = plugin.getconfigs().getString("Deathmessages.starvation").replaceAll("%player%", e.getEntity().getPlayer().getName().toString());
        S = S.replaceAll("&", "\u00A7");
        
        String C = plugin.getconfigs().getString("Deathmessages.contact").replaceAll("%player%", e.getEntity().getPlayer().getName().toString());
        D = C.replaceAll("&", "\u00A7");
        
        String P = plugin.getconfigs().getString("Deathmessages.projectile").replaceAll("%player%", e.getEntity().getPlayer().getName().toString());
        P = P.replaceAll("&", "\u00A7");
		
        String V = plugin.getconfigs().getString("Deathmessages.void").replaceAll("%player%", e.getEntity().getPlayer().getName().toString());
        V = V.replaceAll("&", "\u00A7");
        
	// Death Messages	
        if (plugin.getconfigs().getBoolean("Deathmessages.enable") == true) {
		if (entity instanceof Player && dc == DamageCause.ENTITY_ATTACK) {
		e.setDeathMessage(EA);
		}
		
		if (entity instanceof Player && dc == DamageCause.ENTITY_EXPLOSION) {
		e.setDeathMessage(EE);
		}
		
		if (entity instanceof Player && dc == DamageCause.LIGHTNING) {
		e.setDeathMessage(L);
		}
		
		if (entity instanceof Player && dc == DamageCause.DROWNING) {
		e.setDeathMessage(D);
		}
		
		if (entity instanceof Player && dc == DamageCause.FALL) {
		e.setDeathMessage(F);
		}
		
		if (entity instanceof Player && dc == DamageCause.FIRE) {
		e.setDeathMessage(Fire);
		}
		
		if (entity instanceof Player && dc == DamageCause.FIRE_TICK) {
		e.setDeathMessage(Fire);
		}
		
		if (entity instanceof Player && dc == DamageCause.STARVATION) {
		e.setDeathMessage(S);
		}
		
		if (entity instanceof Player && dc == DamageCause.CONTACT) {
		e.setDeathMessage(C);
		}
		
		if (entity instanceof Player && dc == DamageCause.PROJECTILE) {
		e.setDeathMessage(P);
		}
		
		if (entity instanceof Player && dc == DamageCause.VOID) {
		e.setDeathMessage(V);
		}
	}
        
	}
}
