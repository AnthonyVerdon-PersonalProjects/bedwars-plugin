package fr.nordev.bedwars;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class Team {

	private Material bed;
	private Location spawn;
	private ArrayList<Player> players;
	
	
	public Material getBed() {
		return bed;
	}
	public void setBed(Material bed) {
		this.bed = bed;
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
}
