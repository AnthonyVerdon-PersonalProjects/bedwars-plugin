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
		System.out.println(player + " connected");
		main.updateWorld(player, "lobby");
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
        serverLobby.startGame(main, event, item);
        serverLobby.chooseGame(main, event, item);
        gameLobby.chooseTeam(main, event, item);
    }
	
	@EventHandler
    public void onChangeWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        World world = player.getLocation().getWorld();
        player.getInventory().clear();
        System.out.println(player.getName() + " connected to world " + world.getName());
        if (world.getName().compareTo("lobby") == 0) {
        	System.out.println("give " + player.getName() + " a menu book");
        	player.getInventory().setItem(4, main.createCustomItem(Material.BOOK, "Menu"));
        	player.updateInventory();
        } else if (world.getName().startsWith("game_")) {
        	player.getInventory().setItem(4, main.createCustomItem(Material.BOOK, "choose a team"));
        	player.updateInventory();
        }
    }
	
	@EventHandler
	public void onBedEnter(PlayerBedEnterEvent event) {
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onBlockDestroyed(BlockBreakEvent event) {
		return ;
		/*
		 * need to associate a bed to a team, and if a bed is destroyed, send a message to player from this team to inform them
		 */
		/*
		Player player = event.getPlayer();
		if (event.getBlock().getType() == Material.RED_BED)
			player.sendTitle(ChatColor.RED + "bed destroyed !", ChatColor.RED + "this is your last chance !", 10, 70, 20);
		*/
	}
}
