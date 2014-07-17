package me.bdubz4552.horsestats.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;

import me.bdubz4552.horsestats.HorseStatsCommand;
import me.bdubz4552.horsestats.HorseStatsMain;
import me.bdubz4552.horsestats.utilities.Translate;

public class Delname extends HorseStatsCommand {
	
	public Delname(HorseStatsMain horseStatsMain) {
		super(horseStatsMain);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command,	String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			Horse h = null;
			if (p.isInsideVehicle()) {
				if (p.getVehicle() instanceof Horse) {
					h = (Horse) p.getVehicle();
				}
			}
			this.run(p, h);
		} else {
			sender.sendMessage(Translate.generic("console"));
		}
		return true;
	}
	
	public void run(Player p, Horse h) {
		if (h != null) {
			if (h.getOwner() == p || main.override(p)) {
				h.setCustomName(null);
				this.sendNormal(p, Translate.delname, "nameDelete");
			} else {
				this.sendError(p, Translate.generic, "owner");
			}
		} else {
			this.sendError(p, Translate.generic, "riding");
		}
	}
}
