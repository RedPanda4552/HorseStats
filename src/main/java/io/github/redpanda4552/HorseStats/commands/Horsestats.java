package io.github.redpanda4552.HorseStats.commands;

import static org.bukkit.ChatColor.*;
import io.github.redpanda4552.HorseStats.HorseStatsCommand;
import io.github.redpanda4552.HorseStats.HorseStatsMain;
import io.github.redpanda4552.HorseStats.utilities.Translate;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class Horsestats extends HorseStatsCommand { 
	
	private String[] help =
	{ GREEN  + "========================"
	, YELLOW + "HorseStats " + main.getDescription().getVersion() + " " + Translate.horsestats("by") + " 'bdubz4552'"
	, GREEN  + "========================"
	, YELLOW + Translate.horsestats("statDisplay")
	, GREEN  + Translate.horsestats("instruction")
	, GREEN  + Translate.horsestats("statLine1")
	, GREEN  + Translate.horsestats("statLine2")
	, YELLOW + Translate.horsestats("noteworthy")
	, GREEN  + Translate.horsestats("note1")
	, GREEN  + Translate.horsestats("note2")
	, YELLOW  + Translate.horsestats("teleportTitle")
	, GREEN  + Translate.horsestats("teleport1") + YELLOW + " /htp " + GREEN + Translate.horsestats("teleport2")
	, YELLOW + Translate.horsestats("command") + YELLOW + " '/help horsestats'."
	};

	public Horsestats(HorseStatsMain horseStatsMain) {
		super(horseStatsMain);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,	String label, String[] args) {
		sender.sendMessage(help);
		return true;
	}
}
