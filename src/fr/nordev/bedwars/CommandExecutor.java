package fr.nordev.bedwars;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.nordev.bedwars.classes.Game;

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
			else if (cmd.getName().compareTo("lobby") == 0)
				return (lobbyCommand(player));
			else if (cmd.getName().compareTo("gameBlueprint") == 0)
				return (gameBlueprintCommand(player));
			else if (cmd.getName().compareTo("start") == 0)
				return (startCommand(player));
			else if (cmd.getName().compareTo("end") == 0)
				return (endCommand(player));
		}
		return false;
	}
	
	private boolean respawnCommand(Player player)
	{
		main.respawnPlayer(player);
		return true;
	}
	
	private boolean lobbyCommand(Player player)
	{
		main.updateWorld(player, "lobby");
		return true;
	}
	
	private boolean gameBlueprintCommand(Player player)
	{
		main.updateWorld(player, "gameBlueprint");
		return true;
	}
	
	private boolean startCommand(Player player)
	{
		Game game = main.getGame(player);
		if (game == null)
		{
			player.sendMessage("you need to be in a game lobby to execute this command");
			return (false);
		}
		game.start(main);
		return true;
	}
	
	private boolean endCommand(Player player)
	{
		Game game = main.getGame(player);
		if (game == null)
		{
			player.sendMessage("you need to be in a game lobby to execute this command");
			return (false);
		}
		game.end(main, "none");
		return true;
	}
}
