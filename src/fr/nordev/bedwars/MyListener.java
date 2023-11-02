package fr.nordev.bedwars;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.nordev.bedwars.classes.Game;
import fr.nordev.bedwars.phases.GameLobby;
import fr.nordev.bedwars.phases.InGameLogic;
import fr.nordev.bedwars.phases.ServerLobby;

import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MyListener implements Listener {

	private Main main;
	private ServerLobby serverLobby;
	private GameLobby gameLobby;
	private InGameLogic inGameLogic;
	
	public MyListener(Main main)
	{
		this.main = main;
		serverLobby = new ServerLobby();
		gameLobby = new GameLobby();
		inGameLogic = new InGameLogic();
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		main.updateWorld(player, "lobby");
		player.getInventory().clear();
		player.getInventory().setItem(4, main.createCustomItem(Material.BOOK, "Menu"));
    	player.updateInventory();
    	player.setInvulnerable(true);
	}

	@EventHandler
	public void onDamageApplied(EntityDamageEvent event) {
		inGameLogic.onDamageApplied(main, event);
	}
	
	@EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if ((event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) || event.getItem() == null)
            return ;
        ItemStack item = event.getItem();
        if (!item.hasItemMeta())
        	return ;
        ItemMeta meta = item.getItemMeta();
        if (meta.hasDisplayName() && meta.getDisplayName().compareTo("Menu") == 0)
        	serverLobby.openMenuBook(main, event);
		else if (meta.hasDisplayName() && meta.getDisplayName().compareTo("choose a team") == 0)
			gameLobby.openChooseTeamBook(main, event);
    }
	
	@EventHandler
    public void onClick(InventoryClickEvent event) {
		if (event.getClick() != ClickType.LEFT || event.getCurrentItem() == null)
            return ;
		ItemStack item = event.getCurrentItem();
		if (event.getWhoClicked().getWorld().getName().compareTo("lobby") == 0)
		{
			serverLobby.startGame(main, event, item);
        	serverLobby.chooseGame(main, event, item);
		} else
			gameLobby.chooseTeam(main, event, item);
    }
	
	@EventHandler
    public void onChangeWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        Game game = main.getGame(player);
        if (game != null)
        	game.resetPlayerTeam(player);
        World world = player.getLocation().getWorld();
        player.getInventory().clear();
        System.out.println(player.getName() + " connected to world " + world.getName());
        if (world.getName().compareTo("lobby") == 0) {
        	serverLobby.playerConnected(main, player);
        } else if (world.getName().startsWith("game_")) {
        	gameLobby.playerConnected(main, player);
        }
    }
	
	@EventHandler
	public void onBedEnter(PlayerBedEnterEvent event) {
		inGameLogic.onBedEnter(event);
	}
	
	@EventHandler
	public void onBlockDestroyed(BlockBreakEvent event) {
		inGameLogic.onBlockDestroyed(main, event);
	}
}
