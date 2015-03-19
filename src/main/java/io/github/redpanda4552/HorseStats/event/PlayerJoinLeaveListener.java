/**
 * This file is part of HorseStats, licensed under the MIT License (MIT)
 * 
 * Copyright (c) 2015 Brian Wood
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.github.redpanda4552.HorseStats.event;

import java.util.ArrayList;
import java.util.UUID;

import io.github.redpanda4552.HorseStats.HorseStatsMain;
import io.github.redpanda4552.HorseStats.translate.Translate;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinLeaveListener extends ListenerBase {

	public PlayerJoinLeaveListener(HorseStatsMain main, Translate tl) {
		super (main, tl);
	}

	@EventHandler
	public void playerJoin(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		if (p.hasPermission("HorseStats.pluginalerts")) {
			if (main.outofdateConfig) {
				p.sendMessage(tl.n + tl.playerJoinLeave("config"));
			}
			
			if (main.noSpeedMode) {
				p.sendMessage(tl.n + tl.playerJoinLeave("noSpeed"));
			}
			
			if (main.updateAvailable) {
				p.sendMessage(tl.n + tl.playerJoinLeave("new-build") + " " + main.updateName + " " + tl.playerJoinLeave("at"));
				//Sent as stat to avoid second [HorseStats] header. Want this to look like one message, but separated lines.
				p.sendMessage(tl.s + "https://dev.bukkit.org/bukkit-plugins/horsestats");
			}
		}
		
		if (p.hasPermission("HorseStats.friend")) {
			main.friendHelper.readFriendListFromFile(p.getUniqueId());
		}
	}
	
	@EventHandler
	public void playerLeave(PlayerQuitEvent event) {
		ArrayList<UUID> friends = main.friendHelper.readFriendListFromIndex(event.getPlayer().getUniqueId());
		
		if (friends != null) {
			main.friendHelper.removeFriendList(event.getPlayer().getUniqueId());
		}
		
		if (main.teleportQueue.containsKey(event.getPlayer().getUniqueId())) {
			main.teleportQueue.remove(event.getPlayer().getUniqueId());
		}
	}
}
