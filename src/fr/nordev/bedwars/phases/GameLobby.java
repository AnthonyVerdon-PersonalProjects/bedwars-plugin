package fr.nordev.bedwars.phases;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import fr.nordev.bedwars.Main;
import fr.nordev.bedwars.classes.Game;
import fr.nordev.bedwars.classes.Team;

public class GameLobby {
	
	public void playerConnected(Main main, Player player)
	{
		player.getInventory().setItem(4, main.createCustomItem(Material.BOOK, "choose a team"));
    	player.updateInventory();
    	Scoreboard board = main.getScoreboardManager().getNewScoreboard();
		Objective obj = board.registerNewObjective("inGameScoreBoard", Criteria.DUMMY, "Bedwars");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        Score nbPlayers = obj.getScore("nb players: " + main.getGame(player).getPlayers().size());
        nbPlayers.setScore(0);
        player.setScoreboard(board);
	}
	
	public void openChooseTeamBook(Main main, PlayerInteractEvent event)
	{
        Player player = event.getPlayer();
        updateTeamBook(main, player);
	}
	
	private void updateTeamBook(Main main, Player player)
	{
		Game game = main.getGame(player);
		if (game == null)
			return ;
        Inventory inventoryMenu = Bukkit.createInventory(null, 27, "choose a team");
        int index = 0;
        for (Material material : Material.values())
        {
        	if (!material.name().endsWith("_DYE"))
        		continue ;
        	inventoryMenu.setItem(index, createTeamDye(main, game, material));
        	index++;
        }
        player.openInventory(inventoryMenu);
	}
	
	private ItemStack createTeamDye(Main main, Game game, Material material)
	{
		ItemStack teamItem = main.createCustomItem(material, "TEAM " + material.name().split("_")[0]);
    	ItemMeta meta = teamItem.getItemMeta();
    	ArrayList<String> teamComposition = new ArrayList<String>();
    	Team team = game.getTeam(material.name());
    	ArrayList<Player> players = team.getPlayers();
    	int playerArraySize = players.size();
		for (int i = 0; i < playerArraySize; i++)
			teamComposition.add(players.get(i).getName());
    	meta.setLore(teamComposition);
    	teamItem.setItemMeta(meta);
    	return (teamItem);
	}

	public void chooseTeam(Main main, InventoryClickEvent event, ItemStack item)
	{
		if (!item.getType().name().endsWith("_DYE") || !item.hasItemMeta())
        	return ;
        ItemMeta meta = item.getItemMeta();
        if (!meta.hasDisplayName() || !meta.getDisplayName().startsWith("TEAM "))
        	return ;
        Player player = (Player)event.getWhoClicked();
        Game game = main.getGame(player);
        if (game != null)
        {
        	game.resetPlayerTeam(player);
        	game.addPlayerToTeam(player, meta.getDisplayName());
        	updateTeamBook(main, player);
        }
        //add player to the correct team (if player already in a team, erase it)
        //add a button to quit your team
	}
}
