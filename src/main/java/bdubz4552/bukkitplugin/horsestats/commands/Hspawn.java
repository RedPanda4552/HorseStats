package bdubz4552.bukkitplugin.horsestats.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.entity.Horse.Variant;

import bdubz4552.bukkitplugin.horsestats.HorseStatsCommand;
import bdubz4552.bukkitplugin.horsestats.HorseStatsMain;
import bdubz4552.bukkitplugin.horsestats.utilities.Translate;

public class Hspawn extends HorseStatsCommand {
	
	public Hspawn(HorseStatsMain horseStatsMain) {
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
	
	public void run(Player p, Horse h, String[] args) {
		if (h == null) {						
			Variant v = null;
			if (args.length >= 1) {
				if (args[0].equalsIgnoreCase("donkey")) {
					v = Variant.DONKEY;
					this.sendNormal(p, Translate.hspawn, "donkeySpawn");
				} else if (args[0].equalsIgnoreCase("mule")) {
					v = Variant.MULE;
					this.sendNormal(p, Translate.hspawn, "muleSpawn");
				} else {
					this.sendError(p, Translate.hspawn, "usage");
					return;
				}
			} else{
				v = Variant.HORSE;
			}
			h = (Horse) p.getWorld().spawnEntity(p.getLocation(), EntityType.HORSE);
			h.setAdult();
			h.setVariant(v);
			if (v == Variant.HORSE) {
				
				this.sendNormal(p, Translate.hspawn, "horseSpawn");
			}
		} else {
			this.sendError(p, Translate.generic, "cannotRide");
		}
	}
}
