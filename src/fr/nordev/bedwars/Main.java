package fr.nordev.bedwars;

import java.io.File;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.block.Block;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;

public class Main extends JavaPlugin {

	@Override
	public void onEnable() {
		System.out.println("plugin started");
		getServer().getPluginManager().registerEvents(new MyListener(this), this);
		getCommand("respawn").setExecutor(new CommandExecutor(this));
		getCommand("changeWorld").setExecutor(new CommandExecutor(this));
		createWorld("lobby", World.Environment.NORMAL, WorldType.FLAT);
		createWorld("gameBlueprint", World.Environment.NORMAL, WorldType.FLAT);
	}

	@Override
	public void onDisable() {
		//delete the game when it ended
		int nbGames = Game.getNbGames();
		for (int i = 0; i < nbGames; i++)
		{
			World gameWorld = Bukkit.getWorld("game_" + i);
			if (gameWorld == null)
				continue ;
			File folder = gameWorld.getWorldFolder();
			Bukkit.unloadWorld(gameWorld, false);
			folder.delete();
		}
		System.out.println("plugin stopped");
	}
	
	private void createWorld(String worldname, Environment env, WorldType type)
	{
		WorldCreator wc = new WorldCreator(worldname);
		wc.environment(env);
		if (worldname.compareTo("gameBlueprint") == 0)
		{
			wc.generatorSettings("{\"layers\": []}");
			wc.generateStructures(false);
		}
		wc.type(type);
		World newWorld = wc.createWorld();
		if (worldname.compareTo("gameBlueprint") == 0)
		{
			Block block = newWorld.getBlockAt(0, 0, 0);
			block.setType(Material.BEDROCK);
		}
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
	
	public World getGameBlueprint() {
		return (getServer().getWorld("gameBlueprint"));
	}
}
