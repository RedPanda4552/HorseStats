package me.bdubz4552.horsestats.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;

import me.bdubz4552.horsestats.HorseStatsCommand;
import me.bdubz4552.horsestats.HorseStatsMain;

import me.bdubz4552.horsestats.utilities.Translate;

public class SetOwner extends HorseStatsCommand {
	
	public SetOwner(HorseStatsMain horseStatsMain) {
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
			this.run(p, h, args);
		} else {
			sender.sendMessage(Translate.generic("console"));
		}
		return true;
	}
	
	/**
	 * Since we just need to get the player at that moment, we can safely use their username
	 */
	@SuppressWarnings("deprecation")
	public void run(Player p, Horse h, String[] args) {
		if (h != null) {
			if (h.getOwner() == p || main.override(p)) {
				if (args.length == 1) {					
					if (Bukkit.getServer().getPlayerExact(args[0]) != null) {
						h.eject();
						this.sendNormal(p, Translate.setOwner, "setOwner");
						h.setOwner(p.getServer().getPlayerExact(args[0]));
					} else {
						this.sendError(p, Translate.generic, "playerNotFound");
					}
				} else {
					this.sendError(p, Translate.setOwner, "usage");
				}
			} else {
				this.sendError(p, Translate.generic, "owner");
			}
		} else {
			this.sendError(p, Translate.generic, "riding");
		}
	}
}
