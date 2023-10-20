package fr.nordev.bedwars;

import java.util.ArrayList;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.GameMode;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;

public class Main extends JavaPlugin {

	private ArrayList<Player> players;

	@Override
	public void onEnable() {
		System.out.println("plugin started");
		getServer().getPluginManager().registerEvents(new MyListener(this), this);
		getCommand("respawn").setExecutor(new CommandExecutor(this));
		getCommand("changeWorld").setExecutor(new CommandExecutor(this));
		createWorld("lobby", World.Environment.NORMAL, WorldType.FLAT);
		createWorld("game", World.Environment.NORMAL, WorldType.FLAT);
		players = new ArrayList<Player>();
	}

	@Override
	public void onDisable() {
		System.out.println("plugin stopped");
	}
	
	private void createWorld(String worldname, Environment env, WorldType type)
	{
		WorldCreator wc = new WorldCreator(worldname);
		wc.environment(env);
		wc.type(type);
		wc.createWorld();
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
		player.setFoodLevel(100);
		player.teleport(player.getWorld().getSpawnLocation());
	}
	
	public void updateWorld(Player player, String worldname)
	{
		player.teleport(getServer().getWorld(worldname).getSpawnLocation());
	}
}
