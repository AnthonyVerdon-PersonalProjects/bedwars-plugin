package fr.nordev.bedwars;

import java.util.ArrayList;

import org.bukkit.World;
import org.bukkit.WorldCreator;

public class Game {

	private ArrayList<Team> teams;
	private World world;
	private String name;
	private static int nbGames = 0;
	
	public Game(World map) {
		teams = new ArrayList<Team>();
		name = "game_" + nbGames;
		WorldCreator worldCreator  = new WorldCreator(name);
		worldCreator.copy(map);
		worldCreator.createWorld();
		nbGames++;
	}
	
	public ArrayList<Team> getTeams() {
		return teams;
	}
	public void setTeams(ArrayList<Team> teams) {
		this.teams = teams;
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
}
