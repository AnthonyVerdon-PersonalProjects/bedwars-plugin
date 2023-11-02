package fr.nordev.bedwars;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.ScoreboardManager;

import fr.nordev.bedwars.classes.Game;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.block.Block;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Main extends JavaPlugin {

	private ArrayList<Game> games;
	private ScoreboardManager manager;
	
	@Override
	public void onEnable() {
		System.out.println("plugin started");
		getServer().getPluginManager().registerEvents(new MyListener(this), this);
		CommandExecutor commands = new CommandExecutor(this);
		getCommand("respawn").setExecutor(commands);
		getCommand("lobby").setExecutor(commands);
		getCommand("gameBlueprint").setExecutor(commands);
		getCommand("start").setExecutor(commands);
		getCommand("end").setExecutor(commands);
		createWorld("lobby", World.Environment.NORMAL, WorldType.FLAT);
		createWorld("gameBlueprint", World.Environment.NORMAL, WorldType.FLAT);
		games = new ArrayList<Game>();
		manager = Bukkit.getScoreboardManager();
	}
	
	@Override
	public void onDisable() {
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
	
	/*
	 * generate a world with specifics arguments
	 * gameBlueprint world is an equivalent of a map builders could create
	 */
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
			Block block = newWorld.getBlockAt(0, -60, 0);
			block.setType(Material.BEDROCK);
			newWorld.setSpawnLocation(new Location(newWorld, 0.5, -59.0, 0.5));
		}
	}
	
	/*
	 * reset player state in game
	 */
	public void respawnPlayer(Player player)
	{
		player.setGameMode(GameMode.SURVIVAL);
		player.setHealth(20.0);
		player.setAbsorptionAmount(100);
		player.setFoodLevel(100);
		player.teleport(player.getWorld().getSpawnLocation());
	}
	
	/*
	 * teleport the player to a specific world
	 */
	public void updateWorld(Player player, String worldname)
	{
		player.teleport(getServer().getWorld(worldname).getSpawnLocation());
	}
	
	/*
	 * store a game instance into the games dataset
	 */
	public void addGame(Game game)
	{
		games.add(game);
	}
	
	/*
	 * erase a game instance from the games dataset
	 */
	public void deleteGame(Game game)
	{
		games.remove(game);
	}
	
	/*
	 * create a new item based on it name and it material
	 */
	public ItemStack createCustomItem(Material materialname, String name)
	{
		ItemStack customItem = new ItemStack(materialname, 1);
    	ItemMeta customMeta = customItem.getItemMeta();
    	customMeta.setDisplayName(name);
    	customItem.setItemMeta(customMeta);
    	return (customItem);
	}
	
	/*
	 * method to compare coordinate to a location, for bed destruction detection
	 */
	public boolean samePosition(Location location, double[] coords)
	{
		if (location.getX() == coords[0]
			&& location.getY() == coords[1]
			&& location.getZ() == coords[2])
			return (true);
		return (false);
	}
	
	/*
	 * getter for different attributes
	 */
	public World getGameBlueprint() {
		return (getServer().getWorld("gameBlueprint"));
	}
	
	public Game getGame(Player player)
	{
		int gameArraySize = games.size();
		for (int i = 0; i < gameArraySize; i++)
		{
			ArrayList<Player> players = games.get(i).getPlayers();
			int playerArraySize = players.size();
			for (int j = 0; j < playerArraySize; j++)
			{
				if (players.get(j) == player)
					return (games.get(i));
			}
		}
		return (null);
	}
	
	public ArrayList<Game> getGames()
	{
		return (games);
	}
	
	public ScoreboardManager getScoreboardManager() {
		return manager;
	}
}
