package fr.nordev.bedwars;

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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.event.entity.EntityDamageEvent;

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
    public void onRightClick (PlayerInteractEvent event) {
        //Player player = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_AIR) {
            if (event.getItem() != null && event.getItem().getType() == Material.BOOK) {
                System.out.println("right click while holding book");
            }
        }
    }
	
	@EventHandler
    public void onChangeWorld (PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        World world = player.getLocation().getWorld();
        player.getInventory().clear();
        System.out.println(player.getName() + " has changed of world");
        if (world.getName() == "lobby") {
        	ItemStack customBook = new ItemStack(Material.BOOK, 1);
        	ItemMeta customMeta = customBook.getItemMeta();
        	customMeta.setDisplayName("Menu");
        	customBook.setItemMeta(customMeta);
        	player.getInventory().addItem(customBook);
        	player.updateInventory();
        }
    }
}
