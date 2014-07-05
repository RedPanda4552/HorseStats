package me.bdubz4552.horsestats.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.bdubz4552.horsestats.HorseStatsCommand;
import me.bdubz4552.horsestats.HorseStatsMain;
import me.bdubz4552.horsestats.Message;

public class Untame extends HorseStatsCommand  implements CommandExecutor {
	
	public Untame(HorseStatsMain horseStatsMain) {
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
			if (label.equalsIgnoreCase("untame")) {
				if (this.permCheck(p, "untame")) {
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
			if (h.getOwner() == p || main.hasGlobalOverride(p)) {
				h.eject();
				h.setOwner(null);
				h.setTamed(false);
				if (h.getInventory().getSaddle() != null) {
					ItemStack stack = h.getInventory().getSaddle();
					h.getInventory().setSaddle(null);
					h.getWorld().dropItemNaturally(h.getLocation(), stack);
				}
				Message.NORMAL.send(p, "This horse is no longer tamed.");
			} else {
				Message.OWNER.send(p);
			}
		} else {
			Message.RIDING.send(p);
		}
	}
}
