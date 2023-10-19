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
			main.respawnPlayer((Player)sender);
		}
		return false;
	}

}
