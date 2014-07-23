package bdubz4552.bukkitplugin.horsestats.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;

import bdubz4552.bukkitplugin.horsestats.HorseStatsCommand;
import bdubz4552.bukkitplugin.horsestats.HorseStatsMain;
import bdubz4552.bukkitplugin.horsestats.utilities.Translate;

public class SetStat extends HorseStatsCommand {
	
	public SetStat(HorseStatsMain main) {
		super(main);
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
	
	public void run(Player p, Horse h, String args[]) {
		if (h != null) {
			if (h.getOwner() == p || main.override(p)) {
				if (args.length == 2) {			
					if (args[0].equalsIgnoreCase("health")) {
						double health = Double.parseDouble(args[1]);
						h.setMaxHealth(2 * health);
						this.sendNormal(p, Translate.setstat("healthSetTo") + " " + health + " " + Translate.setstat("hearts"));
					} else if (args[0].equalsIgnoreCase("jump")) {
						double jump = Double.parseDouble(args[1]);
						if (jump > 22) {
							jump = 22;
							this.sendError(p, Translate.setStat, "jumpLimit");
						}
						h.setJumpStrength(Math.sqrt(jump / 5.5));
						this.sendNormal(p, Translate.setstat("jumpSetTo") + " " + jump + " " + Translate.setstat("blocks"));
					} else {
						this.sendError(p, Translate.setStat, "usage");
					}
				} else {
					this.sendError(p, Translate.setStat, "usage");
				}
			} else {
				this.sendError(p, Translate.generic, "owner");
			}
		} else {
			this.sendError(p, Translate.generic, "riding");
		}
	}
}
