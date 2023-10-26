package fr.nordev.bedwars;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.ScoreboardManager;
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
	
	public void addGame(Game game)
	{
		games.add(game);
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
	
	public ItemStack createCustomItem(Material materialname, String name)
	{
		ItemStack customItem = new ItemStack(materialname, 1);
    	ItemMeta customMeta = customItem.getItemMeta();
    	customMeta.setDisplayName(name);
    	customItem.setItemMeta(customMeta);
    	return (customItem);
	}

	public ScoreboardManager getScoreboardManager() {
		return manager;
	}

	public void setScoreboardManager(ScoreboardManager manager) {
		this.manager = manager;
	}
	
	public boolean samePosition(Location location, double[] coords)
	{
		if (location.getX() == coords[0]
			&& location.getY() == coords[1]
			&& location.getZ() == coords[2])
			return (true);
		return (false);
	}
}
