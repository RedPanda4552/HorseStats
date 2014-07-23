package bdubz4552.bukkitplugin.horsestats.commands;

import static org.bukkit.ChatColor.*;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import bdubz4552.bukkitplugin.horsestats.HorseStatsCommand;
import bdubz4552.bukkitplugin.horsestats.HorseStatsMain;
import bdubz4552.bukkitplugin.horsestats.utilities.Translate;

public class Horsestats extends HorseStatsCommand { 
	
	private String[] help =
	{ GREEN  + "========================"
	, YELLOW + "HorseStats " + Translate.horsestats("by") + " 'bdubz4552'"
	, YELLOW + Translate.horsestats("version") + " " + main.getDescription().getVersion()
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
