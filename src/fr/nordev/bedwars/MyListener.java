package fr.nordev.bedwars;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.event.entity.EntityDamageEvent;

public class MyListener implements Listener {

	private Main main;
	
	public MyListener(Main main)
	{
		this.main = main;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		System.out.println(player + " connected");
		main.addPlayer(player);
		main.updateWorld(player, "lobby");
	}

	@EventHandler
	public void onDamageApplied(EntityDamageEvent event) {

		if (!(event.getEntity() instanceof Player))
			return ;
		Player player = (Player)event.getEntity();
		if (player.getHealth() <= event.getFinalDamage())
		{
			event.setCancelled(true);
			player.setGameMode(GameMode.SPECTATOR);
			player.sendTitle(ChatColor.RED + "You die !", ChatColor.RED + "wait for respawn ...", 10, 70, 20);
			new BukkitRunnable(){
	            public void run(){
	                main.respawnPlayer(player);
	            }
	        }.runTaskLater(main, 20L * 5);
			System.out.println(player.getDisplayName() + " died");
		}
	}
}
