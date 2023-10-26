package fr.nordev.bedwars.classes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import fr.nordev.bedwars.Main;
import fr.nordev.bedwars.enums.bedLocation;

public class Game {

	private ArrayList<Team> teams;
	private ArrayList<Player> players;
	private World world;
	private String name;
	private static int nbGames = 0;
	
	public Game(World map) {
		name = "game_" + nbGames;
		new File(name).mkdir();
		copy(map.getWorldFolder(), name);
		WorldCreator worldCreator = new WorldCreator(name);
		world = worldCreator.createWorld();
		players = new ArrayList<Player>();
		initTeams();
		nbGames++;
	}
	
	private void copy(File folder, String name)
	{
		for (File file: folder.listFiles())
		{
			if (file.getName().compareTo("uid.dat") == 0)
				continue ;
			File newFile = new File(name + File.separator + file.getName());
			if (file.isDirectory())
			{
				newFile.mkdir();
				copy(file, name + File.separator + newFile.getName());
				continue ;
			}
			try {
				FileInputStream inputStream = new FileInputStream(file);
				FileOutputStream outputStream = new FileOutputStream(newFile);
				int len;
				byte[] bytes = new byte[1024];
				while ((len = inputStream.read(bytes)) > 0)
					outputStream.write(bytes, 0, len);
				inputStream.close();
				outputStream.close();
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}
	
	private void initTeams()
	{
		teams = new ArrayList<Team>();
        for (Material material : Material.values())
        {
        	if (!material.name().endsWith("_DYE"))
        		continue ;
        	Team newTeam = new Team();
        	newTeam.setName("TEAM " + material.name().split("_")[0]);
        	double[] coords = bedLocation.RED_BED;
        	newTeam.setSpawn(new Location(world, coords[0], coords[1], coords[2]));
        	teams.add(newTeam);
        }
	}
	public ArrayList<Team> getTeams() {
		return teams;
	}
	public void setTeams(ArrayList<Team> teams) {
		this.teams = teams;
	}
	public Team getTeam(String materialName)
	{
		int teamArraySize = teams.size();
		for (int i = 0; i < teamArraySize; i++)
		{
			if (teams.get(i).getName().compareTo("TEAM " + materialName.split("_")[0]) == 0)
				return (teams.get(i));
		}
		return (null);
	}
	
	public Team getTeam(Player player)
	{
		int teamArraySize = teams.size();
		for (int i = 0; i < teamArraySize; i++)
		{
			ArrayList<Player> players = teams.get(i).getPlayers();
			int playerArraySize = players.size();
			for (int j = 0; j < playerArraySize; j++)
			{
				if (players.get(j) == player)
					return (teams.get(i));
			}
		}
		return (null);
	}
	
	public Team getTeam(Main main, double[] coords)
	{
		int teamArraySize = teams.size();
		for (int i = 0; i < teamArraySize; i++)
		{
			if (main.samePosition(teams.get(i).getSpawn(), coords))
				return (teams.get(i));
		}
		return (null);
	}
	
	public World getWorld() {
		return world;
	}
	public void setWorld(World world) {
		this.world = world;
	}
	
	public static int getNbGames() {
		return nbGames;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}
	
	public void playerJoined(Player player) {
		players.add(player);
	}
	
	public void addPlayerToTeam(Player player, String teamName) {
		int teamArraySize = teams.size();
		for (int i = 0; i < teamArraySize; i++)
		{
			if (teams.get(i).getName().compareTo(teamName) == 0)
			{
				teams.get(i).addPlayer(player);
				return ;
			}
		}
	}
	
	public void resetPlayerTeam(Player player) {
		int teamArraySize = teams.size();
		for (int i = 0; i < teamArraySize; i++)
		{
			teams.get(i).removePlayer(player);
		}
	}
	
	public void start(Main main)
	{
		Scoreboard board = main.getScoreboardManager().getNewScoreboard();
		Objective obj = board.registerNewObjective("inGameScoreBoard", Criteria.DUMMY, "Bedwars");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        int teamArraySize = teams.size();
        for (int i = 0; i < teamArraySize; i++)
        {
        	String text = teams.get(i).getName() + ": ";
        	if (teams.get(i).getSpawn() == null)
        		text += "§4BED";
        	else
        		text += "§2BED";
        	Score teamBed = obj.getScore(text);
        	teamBed.setScore(i);
        }
		for (int i = 0; i < teamArraySize; i++)
		{
			ArrayList<Player> players = teams.get(i).getPlayers();
			int playerArraySize = players.size();
			for (int j = 0; j < playerArraySize; j++)
			{
				Player player = players.get(j);
				player.getInventory().clear();
				player.teleport(teams.get(i).getSpawn());
				player.setInvulnerable(false);
				player.setScoreboard(board);
			}
		}
	}
	
	public void end(Main main)
	{
		int playerArraySize = players.size();
		for (int i = 0; i < playerArraySize; i++)
			main.updateWorld(players.get(i), "lobby");
	}
	
	public void updateScoreboard(Main main)
	{
		Scoreboard board = main.getScoreboardManager().getNewScoreboard();
		Objective obj = board.registerNewObjective("inGameScoreBoard", Criteria.DUMMY, "Bedwars");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		int teamArraySize = teams.size();
        for (int i = 0; i < teamArraySize; i++)
        {
        	String text = teams.get(i).getName() + ": ";
        	System.out.println(teams.get(i).getSpawn());
        	if (teams.get(i).getSpawn() == null)
        		text += "§4BED";
        	else
        		text += "§2BED";
        	Score teamBed = obj.getScore(text);
        	teamBed.setScore(i);
        }
        for (int i = 0; i < teamArraySize; i++)
		{
			ArrayList<Player> players = teams.get(i).getPlayers();
			int playerArraySize = players.size();
			for (int j = 0; j < playerArraySize; j++)
				players.get(j).setScoreboard(board);
		}
	}
}
