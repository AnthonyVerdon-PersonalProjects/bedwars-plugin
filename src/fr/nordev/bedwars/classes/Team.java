package fr.nordev.bedwars.classes;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Team {

	private String name;
	private Location spawn;
	private ArrayList<Player> players;
	
	public Team()
	{
		players = new ArrayList<Player>();
	}
	
	public Location getSpawn() {
		return spawn;
	}
	public void setSpawn(Location spawn) {
		this.spawn = spawn;
	}
	
	public ArrayList<Player> getPlayers() {
		return players;
	}
	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}
	public void addPlayer(Player player) {
		players.add(player);
	}

	public void removePlayer(Player player) {
		players.remove(player);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void sendTitle(String title, String subtitle)
	{
		int playerArraySize = players.size();
		for (int i = 0; i < playerArraySize; i++)
			players.get(i).sendTitle(title, subtitle, 10, 70, 20);
	}
}
