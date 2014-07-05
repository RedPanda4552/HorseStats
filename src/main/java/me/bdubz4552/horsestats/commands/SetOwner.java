package me.bdubz4552.horsestats.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;

import me.bdubz4552.horsestats.HorseStatsCommand;
import me.bdubz4552.horsestats.HorseStatsMain;
import me.bdubz4552.horsestats.Message;
import me.bdubz4552.horsestats.translate.Translate;

public class SetOwner extends HorseStatsCommand implements CommandExecutor {
	
	public SetOwner(HorseStatsMain horseStatsMain) {
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
			if (label.equalsIgnoreCase("setowner")) {
				if (this.permCheck(p, "setowner")) {
					this.run(p, h, args);
				}
			}
		} else {
			sender.sendMessage(Message.CONSOLE.getString());
		}
		return true;
	}
	
	/**
	 * Because setOwner() uses Player objects and not Strings representing player names,
	 * getting a Player object from whatever the Player's name happens to be at that moment
	 * should not be a problem.
	 */
	@SuppressWarnings("deprecation")
	public void run(Player p, Horse h, String[] args) {
		if (h != null) {
			if (h.getOwner() == p || main.hasGlobalOverride(p)) {
				if (args.length == 1) {					
					if (Bukkit.getServer().getPlayerExact(args[0]) != null) {
						h.eject();
						Message.OWNER_CHANGED.send(p);
						h.setOwner(p.getServer().getPlayerExact(args[0]));
					} else {
						Message.PLAYER.send(p);
					}
				} else {
					Message.ERROR.send(p, Translate.setowner("usage"));
				}
			} else {
				Message.OWNER.send(p);
			}
		} else {
			Message.RIDING.send(p);
		}
	}
}
