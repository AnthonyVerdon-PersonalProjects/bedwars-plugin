package fr.nordev.bedwars;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	@Override
	public void onEnable() {
		System.out.println("plugin started");
	}

	@Override
	public void onDisable() {
		System.out.println("plugin stopped");
	}
}
