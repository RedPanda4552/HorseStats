package bdubz4552.bukkitplugin.horsestats.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import bdubz4552.bukkitplugin.horsestats.HorseStatsCommand;
import bdubz4552.bukkitplugin.horsestats.HorseStatsMain;
import bdubz4552.bukkitplugin.horsestats.utilities.Translate;


public class Slayhorse extends HorseStatsCommand {
	
	public Slayhorse(HorseStatsMain horseStatsMain) {
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
		if (h != null) {
			h.eject();
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("launch") && p.hasPermission("HorseStats.slayhorse.launch")) {
					Vector vec = new Vector(0, 6, 0);
					h.setVelocity(vec);
					p.chat(Translate.slayhorse("launch"));
					Location loc = new Location(h.getWorld(), h.getLocation().getX(), 256, h.getLocation().getZ());
					h.getWorld().strikeLightning(loc);
				}		
			}
			h.setHealth(0);
			this.sendNormal(p, Translate.slayhorse, "slain");
		} else {
			this.sendError(p, Translate.generic, "riding");
		}
	}
}
