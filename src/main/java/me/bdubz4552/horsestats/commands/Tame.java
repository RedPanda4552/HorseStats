package me.bdubz4552.horsestats.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;

import me.bdubz4552.horsestats.HorseStatsCommand;
import me.bdubz4552.horsestats.HorseStatsMain;
import me.bdubz4552.horsestats.Message;

public class Tame extends HorseStatsCommand implements CommandExecutor {
	
	public Tame(HorseStatsMain horseStatsMain) {
		this.main = horseStatsMain;
	}
	
	public boolean onCommand(CommandSender sender, Command command,	String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			Horse h = null;
			if (p.isInsideVehicle()) {
				if (p.getVehicle() instanceof Horse) {
					h = (Horse) p.getVehicle();
				}
			}
			if (label.equalsIgnoreCase("tame")) {
				if (this.permCheck(p, "tame")) {
					this.run(p, h);
				}
			}
		} else {
			sender.sendMessage(Message.CONSOLE.getString());
		}
		return true;
	}
	
	public void run(Player p, Horse h) {
		if (h != null) {
			if (h.getOwner() == null) {
				h.setOwner(p);
				Message.NOW_OWN.send(p);
			} else if (h.getOwner() == p) {
				Message.ALREADY_OWN.send(p);
			} else {
				Message.OWNER.send(p);
			}
		} else {
			Message.RIDING.send(p);
		}
	}
}
