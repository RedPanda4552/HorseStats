package me.bdubz4552.horsestats.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import me.bdubz4552.horsestats.HorseStatsCommand;
import me.bdubz4552.horsestats.HorseStatsMain;
import me.bdubz4552.horsestats.Message;

public class Slayhorse extends HorseStatsCommand implements CommandExecutor {
	
	public Slayhorse(HorseStatsMain horseStatsMain) {
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
			if (label.equalsIgnoreCase("slayhorse")) {
				if (this.permCheck(p, "slayhorse")) {
					this.run(p, h, args);
				}
			}
		} else {
			sender.sendMessage(Message.CONSOLE.getString());
		}
		return true;
	}
	
	public void run(Player p, Horse h, String[] args) {
		if (h != null) {
			h.eject();
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("launch") && p.hasPermission("HorseStats.slayhorse.launch")) {
					Vector vec = new Vector(0, 6, 0);
					h.setVelocity(vec);
					p.chat(Message.LAUNCH.getString());
					Location loc = new Location(h.getWorld(), h.getLocation().getX(), 256, h.getLocation().getZ());
					h.getWorld().strikeLightning(loc);
				}		
			}
			h.setHealth(0);
			Message.SLAIN.send(p);
		} else {
			Message.RIDING.send(p);
		}
	}
}
