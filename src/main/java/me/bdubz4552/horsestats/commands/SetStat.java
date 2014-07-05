package me.bdubz4552.horsestats.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;

import me.bdubz4552.horsestats.HorseStatsCommand;
import me.bdubz4552.horsestats.Message;
import me.bdubz4552.horsestats.translate.Translate;

public class SetStat extends HorseStatsCommand implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command command,	String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			Horse h = null;
			if (p.isInsideVehicle()) {
				if (p.getVehicle() instanceof Horse) {
					h = (Horse) p.getVehicle();
				}
			}
			if (label.equalsIgnoreCase("setstat")) {
				if (this.permCheck(p, "setstat")) {
					this.run(p, h, args);
				}
			}
		} else {
			sender.sendMessage(Message.CONSOLE.getString());
		}
		return true;
	}
	
	public void run(Player p, Horse h, String args[]) {
		if (h != null) {
			if (h.getOwner() == p || main.hasGlobalOverride(p)) {
				if (args.length == 2) {			
					if (args[0].equalsIgnoreCase("health")) {
						double health = Double.parseDouble(args[1]);
						h.setMaxHealth(2 * health);
						Message.NORMAL.send(p, Translate.setstat("healthSetTo") + " " + health + " " + Translate.setstat("hearts"));
					} else if (args[0].equalsIgnoreCase("jump")) {
						double jump = Double.parseDouble(args[1]);
						if (jump > 22) {
							jump = 22;
							Message.JUMP_HEIGHT.send(p);
						}
						h.setJumpStrength(Math.sqrt(jump / 5.5));
						Message.NORMAL.send(p, Translate.setstat("jumpSetTo") + " " + jump + " " + Translate.setstat("blocks"));
					} else {
						Message.ERROR.send(p, Translate.setstat("usage"));
					}
				} else {
					Message.ERROR.send(p, Translate.setstat("usage"));
				}
			} else {
				Message.OWNER.send(p);
			}
		} else {
			Message.RIDING.send(p);
		}
	}
}
