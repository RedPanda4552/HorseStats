package me.bdubz4552.horsestats.event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import me.bdubz4552.horsestats.HorseStatsMain;

import me.bdubz4552.horsestats.utilities.Translate;

public class AdminNotificationListener extends HorseStatsListenerBase {

	public AdminNotificationListener(HorseStatsMain horseStatsMain) {
		super (horseStatsMain);
	}

	@EventHandler
	public void onAdminJoin(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		if (p.hasPermission("HorseStats.pluginalerts")) {
			if (main.outofdateConfig == true) {
				this.sendNormal(p, Translate.adminNotificationListener, "config");
			}
			
			if (main.noSpeedMode == true) {
				this.sendNormal(p, Translate.adminNotificationListener, "noSpeed");
			}
			
			if (main.updateAvailable == true) {
				this.sendNormal(p, Translate.admin("newBuild") + " " + main.updateName + " " + Translate.admin("at"));
				//Sent as stat to avoid second [HorseStats] header. Want this to look like one message, but separated lines.
				this.sendStat(p, "https://dev.bukkit.org/bukkit-plugins/horsestats");
			}
		}
	}
}
