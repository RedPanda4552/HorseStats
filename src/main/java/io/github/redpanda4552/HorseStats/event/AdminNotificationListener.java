package io.github.redpanda4552.HorseStats.event;

import io.github.redpanda4552.HorseStats.HorseStatsMain;
import io.github.redpanda4552.HorseStats.utilities.Translate;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

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
			
			//TODO Put this field back in and properly document it
			if (main.updateAvailable == true) {
				this.sendNormal(p, Translate.admin("newBuild") + " " + main.updateName + " " + Translate.admin("at"));
				//Sent as stat to avoid second [HorseStats] header. Want this to look like one message, but separated lines.
				this.sendStat(p, "https://dev.bukkit.org/bukkit-plugins/horsestats");
			}
		}
	}
}
