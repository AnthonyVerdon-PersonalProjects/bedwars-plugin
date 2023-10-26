package fr.nordev.bedwars;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ServerLobby {

	public void startGame(Main main, InventoryClickEvent event, ItemStack item)
	{
		if (item.getType() != Material.COMPASS || !item.hasItemMeta())
        	return ;
        ItemMeta meta = item.getItemMeta();
        if (!meta.hasDisplayName() || meta.getDisplayName().compareTo("start game") != 0)
        	return ;
        Game newGame = new Game(main.getGameBlueprint());
        Player player = (Player)event.getWhoClicked();
        newGame.playerJoined(player);
        main.addGame(newGame);
        main.updateWorld(player, newGame.getName());
	}
	
	public void chooseGame(Main main, InventoryClickEvent event, ItemStack item)
	{
		if (item.getType() != Material.GREEN_WOOL || !item.hasItemMeta())
        	return ;
		ItemMeta meta = item.getItemMeta();
        if (!meta.hasDisplayName() || !meta.getDisplayName().startsWith("game_"))
        	return ;
		ArrayList<Game> games = main.getGames();
		Player player = (Player)event.getWhoClicked();
		int gameArraySize = games.size();
		Game game;
        for (int i = 0; i < gameArraySize; i++)
        {
        	if (games.get(i).getName().compareTo(meta.getDisplayName()) == 0)
        	{
        		game = games.get(i);
        		main.updateWorld(player, game.getWorld().getName());
        		break;
        	}
        }
		return ;
	}
	
	public void openMenuBook(Main main, PlayerInteractEvent event)
	{
        Player player = event.getPlayer();
        Inventory inventoryMenu = Bukkit.createInventory(null, 27, "Menu");
        inventoryMenu.setItem(0, main.createCustomItem(Material.COMPASS, "start game"));
        ArrayList<Game> games = main.getGames();
        int gameArraySize = 0;
        if (games != null)
        	gameArraySize = games.size();
        for (int i = 0; i < gameArraySize; i++)
        {
        	if (i > 25)
        		break ;
        	inventoryMenu.setItem(i + 1, main.createCustomItem(Material.GREEN_WOOL, games.get(i).getName()));
        }
        player.openInventory(inventoryMenu);
	}
}
