package me.bdubz4552.horsestats;

import org.bukkit.entity.Player;

public class HorseStatsCommand {
	
	public HorseStatsMain main; //Used by sub classes. Within this class, this is null.
	
	public boolean permCheck(Player player, String permission) {
		if (player.hasPermission("HorseStats." + permission)) {
			return true;
		} else {
			Message.PERMS.send(player);
			return false;
		}
	}
}
