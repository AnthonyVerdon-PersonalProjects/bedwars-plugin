package fr.nordev.bedwars;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class Game {

	private ArrayList<Team> teams;
	private ArrayList<Player> players;
	private World world;
	private String name;
	private static int nbGames = 0;
	
	public Game(World map) {
		players = new ArrayList<Player>();
		name = "game_" + nbGames;
		WorldCreator worldCreator  = new WorldCreator(name);
		worldCreator.copy(map);
		world = worldCreator.createWorld();
		initTeams();
		nbGames++;
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
	
	public void start(Scoreboard board)
	{
		int teamArraySize = teams.size();
		Objective obj = board.registerNewObjective("ServerName", Criteria.DUMMY, "Test Server");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        for (int i = 0; i < teamArraySize; i++)
        {
        	String text = teams.get(i).getName() + ": ";
        	if (teams.get(i).getSpawn() == null)
        		text += "§4BED";
        	else
        		text += "§2BED";
        	Score score = obj.getScore(text);
        	score.setScore(i);
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
}
