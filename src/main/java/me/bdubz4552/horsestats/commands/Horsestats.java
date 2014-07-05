package me.bdubz4552.horsestats.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.bdubz4552.horsestats.HorseStatsCommand;
import me.bdubz4552.horsestats.HorseStatsMain;
import me.bdubz4552.horsestats.Message;

public class Horsestats extends HorseStatsCommand implements CommandExecutor {
	static ChatColor ccg = ChatColor.GREEN;
	static ChatColor ccy = ChatColor.YELLOW;
	
	static String[] help = {ccg + "========================", ccy + "HorseStats by 'bdubz4552'", ccg + "========================", ccy + "Stat Display", ccg + "Grab a lead and punch a horse to return a list of statistics:", ccg + "-MaxHealth -Health -Jump Height -Speed (Blocks per Second)", ccg + "-Can Breed -Is Adult -Owner", ccy + "Noteworthy Things:", ccg + "1) Horses will NOT take damage from the punch", ccg + "2) Speed and jump values are not infinitely precise.", ccy + "Horse Teleporting", ccg + "Grab an ender pearl and punch a horse to select it. The damage will be canceled, and the horse will be selected for teleporting. To teleport the horse, use '/htp' at the desired destination and the horse will teleport to you.", ccy + "To see HorseStats commands, use '/help horsestats'. If this does not work, contact an administrator."};
	
	public Horsestats(HorseStatsMain horseStatsMain) {
		this.main = horseStatsMain;
	}

	public boolean onCommand(CommandSender sender, Command command,	String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (label.equalsIgnoreCase("horsestats")) {
				p.sendMessage(help);
			}
		} else {
			sender.sendMessage(Message.CONSOLE.getString());
		}
		return true;
	}
}
