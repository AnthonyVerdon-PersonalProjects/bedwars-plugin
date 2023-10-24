package fr.nordev.bedwars;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandExecutor implements org.bukkit.command.CommandExecutor {

	private Main main;
	
	public CommandExecutor(Main main)
	{
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player)sender;
			if (cmd.getName().compareTo("respawn") == 0)
				return (respawnCommand(player));
			else if (cmd.getName().compareTo("changeWorld") == 0)
				return (changeWorldCommand(player, args));
		}
		return false;
	}
	
	private boolean respawnCommand(Player player)
	{
		main.respawnPlayer(player);
		return true;
	}
	
	private boolean changeWorldCommand(Player player, String[] args)
	{
		if (args.length != 1)
		{
			player.sendMessage("command: /changeWorld <world name>");
			return false;
		}
		else if (args[0].compareTo("lobby") != 0 && args[0].compareTo("gameBlueprint") != 0)
		{
			player.sendMessage("worlds availables: gameBlueprint; lobby");
			return false;
		}
		main.updateWorld(player, args[0]);
		return true;
	}
}
