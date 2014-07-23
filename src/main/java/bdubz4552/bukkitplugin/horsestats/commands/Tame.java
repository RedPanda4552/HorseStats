package bdubz4552.bukkitplugin.horsestats.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;

import bdubz4552.bukkitplugin.horsestats.HorseStatsCommand;
import bdubz4552.bukkitplugin.horsestats.HorseStatsMain;
import bdubz4552.bukkitplugin.horsestats.utilities.Translate;


public class Tame extends HorseStatsCommand {
	
	public Tame(HorseStatsMain horseStatsMain) {
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
			if (h.getOwner() == null) {
				h.setOwner(p);
				this.sendNormal(p, Translate.tame, "nowOwn");
			} else if (h.getOwner() == p) {
				this.sendNormal(p, Translate.tame, "alreadyOwn");
			} else {
				this.sendError(p, Translate.generic, "owner");
			}
		} else {
			this.sendError(p, Translate.generic, "riding");
		}
	}
}
