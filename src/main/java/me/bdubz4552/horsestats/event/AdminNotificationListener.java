package me.bdubz4552.horsestats.event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.bdubz4552.horsestats.HorseStatsMain;
import me.bdubz4552.horsestats.Message;
import me.bdubz4552.horsestats.translate.Translate;

public class AdminNotificationListener extends HorseStatsListenerBase implements Listener {

	public AdminNotificationListener(HorseStatsMain horseStatsMain) {
		super (horseStatsMain);
	}

	@EventHandler
	public void onAdminJoin(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		if (p.hasPermission("HorseStats.pluginalerts")) {
			if (main.outofdateConfig == true) {
				Message.CONFIG_WARN.send(p);
			}
			
			if (main.noSpeedMode == true) {
				Message.NO_SPEED_WARN.send(p);
			}
			
			if (main.updateAvailable == true) {
				Message.NORMAL.send(p, Translate.admin("newBuild") + " " + main.updateName + " " + Translate.admin("at"));
				//Sent as stat to avoid second [HorseStats] header. Want this to look like one message, but separated lines.
				Message.STAT.send(p, "https://dev.bukkit.org/bukkit-plugins/horsestats");
			}
		}
	}
}
