package fr.nordev.bedwars.phases;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.scheduler.BukkitRunnable;

import fr.nordev.bedwars.Main;
import fr.nordev.bedwars.classes.Game;
import fr.nordev.bedwars.classes.Team;
import net.md_5.bungee.api.ChatColor;

public class InGameLogic {

	public void onDamageApplied(Main main, EntityDamageEvent event) {
		if (!(event.getEntity() instanceof Player))
			return ;
		Player player = (Player)event.getEntity();
		if (player.getHealth() <= event.getFinalDamage())
		{
			event.setCancelled(true);
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

	public void onBedEnter(PlayerBedEnterEvent event) {
		event.setCancelled(true);
	}
	
	public void onBlockDestroyed(Main main, BlockBreakEvent event) {
		Block block = event.getBlock();
		if (block.getType() != Material.RED_BED)
			return ;
		Player player = event.getPlayer();
		Game game = main.getGame(player);
		if (game == null)
			return ;
		Location location = block.getLocation();
		double[] coords = { location.getX(), location.getY(), location.getZ() };
		Team team = game.getTeam(main, coords);
		if (team ==  null)
			return ;
		team.sendTitle(ChatColor.RED + "bed destroyed !", ChatColor.RED + "this is your last chance !");
		team.setSpawn(null);
		game.updateScoreboard(main);
	}
}
