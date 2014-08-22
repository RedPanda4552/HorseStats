package io.github.redpanda4552.HorseStats.commands;

import io.github.redpanda4552.HorseStats.*;
import io.github.redpanda4552.HorseStats.utilities.Translate;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;

public class Htp extends HorseStatsCommand {
	
	public Htp(HorseStatsMain horseStatsMain) {
		super(horseStatsMain);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command,	String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			this.run(p);
		} else {
			sender.sendMessage(Translate.generic("console"));
		}
		return true;
	}
	
	/**
	 * Does not work if chunks are not loaded.
	 * @param p - The player who initiated the teleport.
	 */
	public void run(Player p) {
		if (main.teleportQueue.get(p.getName()) == null) {
			this.sendError(p, Translate.htp, "noneSelected");
		} else {
			Horse h = main.teleportQueue.get(p.getName());			
			if (main.configBoolean("interWorldTeleport") == false) {
				if (p.getWorld() != h.getWorld()) {
					this.sendError(p, Translate.htp, "worldTP");
					return;
				}
			}
			
			if (h.teleport(p) == true) {
				this.sendNormal(p, Translate.htp, "teleporting");
			} else {
				this.sendError(p, Translate.htp, "teleportFail");
			}
			
			main.teleportQueue.remove(p.getName());
		}
	}
}
