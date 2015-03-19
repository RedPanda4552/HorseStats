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
package io.github.redpanda4552.HorseStats;

import java.util.ArrayList;
import java.util.UUID;

import io.github.redpanda4552.HorseStats.translate.Translate;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;

/**
 * An abstract class for commands to extend.
 * Sets up message routines so each command doesn't have to.
 */
public abstract class HorseStatsCommand implements CommandExecutor {
	
	protected HorseStatsMain main;
	protected Translate tl;
	
	public HorseStatsCommand(HorseStatsMain main, Translate tl) {
		this.main = main;
		this.tl = tl;
	}
	
	/**
	 * Check if a Player is the owner of a Horse.
	 * @param horse - The Horse to check ownership of.
	 * @param player - The Player in question.
	 * @return True if the Player is the owner, or has "HorseStats.global-override" permission. False if otherwise.
	 */
	protected boolean isOwner(Horse horse, Player player) {
		if (horse.getOwner() == player) {
			return true;
		} else if (player.hasPermission("HorseStats.global-override")) {
			return true;
		}
		return false;
	}
	
	/**
	 * Check if a player owns or has access rights to a horse.
	 * @param horse - The Horse to check
	 * @param player - The Player to check.
	 * @return True if access is allowed for any reason, false if no owner or no conditions are met.
	 */
	protected boolean canAccess(Horse horse, Player player) {
		if (horse.getOwner() == null) {
			return true;
		} else if (player.hasPermission("HorseStats.global-override")) {
			return true;
		} else if (horse.getOwner() == player) {
			return true;
		} else if (Bukkit.getPlayer(horse.getOwner().getUniqueId()).hasPermission("HorseStats.hfriend")) {
			ArrayList<UUID> friends = main.friendHelper.readFriendListFromIndex(horse.getOwner().getUniqueId());
			if (friends != null) {
				if (friends.contains(player.getUniqueId())) {
					return true;
				}
			}
		}
		return false;
	}
}
