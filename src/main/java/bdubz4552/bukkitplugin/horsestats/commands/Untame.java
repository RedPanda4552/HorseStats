package bdubz4552.bukkitplugin.horsestats.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import bdubz4552.bukkitplugin.horsestats.HorseStatsCommand;
import bdubz4552.bukkitplugin.horsestats.HorseStatsMain;
import bdubz4552.bukkitplugin.horsestats.utilities.Translate;

public class Untame extends HorseStatsCommand {
	
	public Untame(HorseStatsMain horseStatsMain) {
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
				h.eject();
				h.setOwner(null);
				h.setTamed(false);
				if (h.getInventory().getSaddle() != null) {
					ItemStack stack = h.getInventory().getSaddle();
					h.getInventory().setSaddle(null);
					h.getWorld().dropItemNaturally(h.getLocation(), stack);
				}
				this.sendNormal(p, Translate.untame, "untame");
			} else {
				this.sendNormal(p, Translate.generic, "owner");
			}
		} else {
			this.sendNormal(p, Translate.generic, "riding");
		}
	}
}
