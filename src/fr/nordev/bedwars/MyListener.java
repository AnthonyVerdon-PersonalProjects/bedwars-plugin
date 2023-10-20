package fr.nordev.bedwars;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MyListener implements Listener {

	private Main main;
	
	public MyListener(Main main)
	{
		this.main = main;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		System.out.println(player + " connected");
		main.addPlayer(player);
		main.updateWorld(player, "lobby");
	}

	@EventHandler
	public void onDamageApplied(EntityDamageEvent event) {

		if (!(event.getEntity() instanceof Player))
			return ;
		Player player = (Player)event.getEntity();
		if (player.getHealth() <= event.getFinalDamage())
		{
			event.setCancelled(true);
			player.setGameMode(GameMode.SPECTATOR);
			player.sendTitle(ChatColor.RED + "You die !", ChatColor.RED + "wait for respawn ...", 10, 70, 20);
			new BukkitRunnable(){
	            public void run(){
	                main.respawnPlayer(player);
	            }
	        }.runTaskLater(main, 20L * 5); //1L = 1 tick, 20L = 1 sec
			System.out.println(player.getDisplayName() + " died");
		}
	}
	
	@EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR || event.getItem() == null)
            return ;
        ItemStack item = event.getItem();
        if (!item.hasItemMeta())
        	return ;
        ItemMeta meta = item.getItemMeta();
        if (!meta.hasDisplayName() || meta.getDisplayName().compareTo("Menu") != 0)
        	return ;
        Player player = event.getPlayer();
        Inventory inventoryMenu = Bukkit.createInventory(null, 27, "Menu");
        inventoryMenu.setItem(9 * 1 + 4, createCustomItem(Material.COMPASS, "start game"));
        player.openInventory(inventoryMenu);
    }
	
	@EventHandler
    public void onClick(InventoryClickEvent event) {
		if (event.getClick() != ClickType.LEFT || event.getCurrentItem() == null)
            return ;
		ItemStack item = event.getCurrentItem();
        if (!item.hasItemMeta())
        	return ;
        ItemMeta meta = item.getItemMeta();
        if (!meta.hasDisplayName() || meta.getDisplayName().compareTo("start game") != 0)
        	return ;
        main.updateWorld((Player)event.getWhoClicked(), "game");
    }
	
	@EventHandler
    public void onChangeWorld (PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        World world = player.getLocation().getWorld();
        player.getInventory().clear();
        System.out.println(player.getName() + " connected to world " + world.getName());
        if (world.getName().compareTo("lobby") == 0) {
        	System.out.println("give " + player.getName() + " a menu book");
        	player.getInventory().setItem(4, createCustomItem(Material.BOOK, "Menu"));
        	player.updateInventory();
        }
    }
	
	private ItemStack createCustomItem(Material materialname, String name)
	{
		ItemStack customItem = new ItemStack(materialname, 1);
    	ItemMeta customMeta = customItem.getItemMeta();
    	customMeta.setDisplayName(name);
    	customItem.setItemMeta(customMeta);
    	return (customItem);
	}
}
