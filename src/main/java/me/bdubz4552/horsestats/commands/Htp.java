package me.bdubz4552.horsestats.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import me.bdubz4552.horsestats.*;
import me.bdubz4552.horsestats.event.HorseStatsListenerBase;
//TODO Modify htp to despawn and respawn horses.
public class Htp extends HorseStatsCommand implements CommandExecutor {
	
	private HorseStatsListenerBase base;
	
	public Htp(HorseStatsMain horseStatsMain, HorseStatsListenerBase horseStatsListenerBase) {
		this.main = horseStatsMain;
		this.base = horseStatsListenerBase;
	}
	
	public boolean onCommand(CommandSender sender, Command command,	String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			
			if (label.equalsIgnoreCase("htp")) {
				if (this.permCheck(p, "htp")) {
					this.run(p);
				}
			}
		} else {
			sender.sendMessage(Message.CONSOLE.getString());
		}
		return true;
	}
	
	/**
	 * Does not work if chunks are not loaded. Attempts at loading chunks were made,
	 * but no success.
	 * @param p - The player who initiated the teleport.
	 */
	public void run(Player p) {
		if (base.teleportQueue.get(p.getName()) == null) {
			Message.NONE_SELECTED.send(p);
		} else {
			Horse h = base.teleportQueue.get(p.getName());			
			if (main.configBoolean("interWorldTeleport") == false) {
				if (p.getWorld() != h.getWorld()) {
					Message.INTER_WORLD.send(p);
					return;
				}
			}
			
			if (h.teleport(p) == true) {
				Message.TELEPORTING.send(p);
			} else {
				Message.TELEPORT_FAIL.send(p);
			}
			
			base.teleportQueue.remove(p.getName());
		}
	}
}
