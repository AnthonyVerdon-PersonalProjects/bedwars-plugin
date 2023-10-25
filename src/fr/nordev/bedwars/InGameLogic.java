package fr.nordev.bedwars;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatColor;

public class InGameLogic {

	public void onDamageApplied(Main main, EntityDamageEvent event) {
		if (!(event.getEntity() instanceof Player))
			return ;
		Player player = (Player)event.getEntity();
		if (player.getHealth() <= event.getFinalDamage())
		{
			//event.setCancelled(true);
			player.setGameMode(GameMode.SPECTATOR);
			if (player.getBedSpawnLocation() == null)
				player.sendTitle(ChatColor.RED + "You lost !", "", 10, 70, 20);
			else
			{
				player.sendTitle(ChatColor.RED + "You die !", ChatColor.RED + "wait for respawn ...", 10, 70, 20);
				new BukkitRunnable(){
		            public void run(){
		                main.respawnPlayer(player);
		            }
		        }.runTaskLater(main, 20L * 5); //1L = 1 tick, 20L = 1 sec
			}
			System.out.println(player.getDisplayName() + " died");
		}
	}
}
