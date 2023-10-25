package fr.nordev.bedwars;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

public class Game {

	private ArrayList<Team> teams;
	private ArrayList<Player> players;
	private World world;
	private String name;
	private static int nbGames = 0;
	
	public Game(World map) {
		initTeams();
		players = new ArrayList<Player>();
		name = "game_" + nbGames;
		WorldCreator worldCreator  = new WorldCreator(name);
		worldCreator.copy(map);
		worldCreator.createWorld();
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
	
	public void start()
	{
		System.out.println("game started");
	}
}
