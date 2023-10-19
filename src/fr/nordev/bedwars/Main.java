package fr.nordev.bedwars;

import java.util.ArrayList;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class Main extends JavaPlugin {

	private ArrayList<Player> players;

	@Override
	public void onEnable() {
		System.out.println("plugin started");
		getServer().getPluginManager().registerEvents(new MyListener(this), this);
		getServer().getWorld("world").setHardcore(true);
		getCommand("respawn").setExecutor(new CommandExecutor(this));
		players = new ArrayList<Player>();
	}

	@Override
	public void onDisable() {
		System.out.println("plugin stopped");
	}
	
	public void addPlayer(Player player)
	{
		players.add(player);
	}
	
	public void respawnPlayer(Player player)
	{
		player.setGameMode(GameMode.SURVIVAL);
		player.setHealth(20.0);
		player.setAbsorptionAmount(100);
		player.setFoodLevel(100);;
		player.teleport(player.getWorld().getSpawnLocation());
	}
}
