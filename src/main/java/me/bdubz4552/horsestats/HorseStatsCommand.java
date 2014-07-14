package me.bdubz4552.horsestats;

import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;

//TODO Full refactor
public abstract class HorseStatsCommand implements CommandExecutor {
	
	public Message message;
	public HorseStatsMain main;
	public HorseStatsCommand(HorseStatsMain main, Message message) {
		this.message = message;
		this.main = main;
	}
}
