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
package io.github.redpanda4552.HorseStats.friend;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import io.github.redpanda4552.HorseStats.HorseStatsMain;

public class FriendHelper {

	public File friendFile;	
	public YamlConfiguration yc;
	
	public HashMap<UUID, ArrayList<UUID>> index;
	
	public FriendHelper(HorseStatsMain main) {
		this.index = new HashMap<UUID, ArrayList<UUID>>();
		this.friendFile = new File("plugins/HorseStats/friends.yml");
		if (!this.friendFile.exists()) {
			main.saveResource("friends.yml", false);
		}
		this.yc = YamlConfiguration.loadConfiguration(this.friendFile);
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			readFriendListFromFile(player.getUniqueId());
		}
	}
	
	/**
	 * Add a single friend to a FriendList, and update the FriendList index.
	 * <b>This does not perform any disc write operations.</b>
	 * @param uuid - The UUID of the Player who's friend list is being modified.
	 * @param friend - The UUID of the friend to add.
	 */
	public void addFriend(UUID uuid, UUID friend) {
		ArrayList<UUID> friends = this.index.remove(uuid);
		friends.add(friend);
		this.index.put(uuid, friends);
	}
	
	/**
	 * Remove a single friend from a FriendList, and update the FriendList index.
	 * <b>This does not perform any disc write operations.</b>
	 * @param uuid - The UUID of the Player who's friend list is being modified.
	 * @param friend - The UUID of the friend to remove.
	 */
	public void removeFriend(UUID uuid, UUID friend) {
		ArrayList<UUID> friends = this.index.remove(uuid);
		friends.remove(friend);
		this.index.put(uuid, friends);
	}
	
	/**
	 * Remove a friend list from the index.
	 * @param uuid - The UUID of the Player who's friends are to be taken out of memory.
	 */
	public void removeFriendList(UUID uuid) {
		this.index.remove(uuid);
	}
	
	/**
	 * Get a friend list from the memory index.
	 * @param uuid - The UUID of the Player to check the index for.
	 * @return An ArrayList of friends belonging to the Player, or null if none exists.
	 */
	public ArrayList<UUID> readFriendListFromIndex(UUID uuid) {
		if (index.containsKey(uuid)) {
			return index.get(uuid);
		}
		return null;
	}
	
	/**
	 * Read a friend list from the storage file and add it to the index.
	 * If a friend list for the specified Player does not exist, this will do nothing.
	 * @param uuid - The UUID to look for in the storage file.
	 */
	public void readFriendListFromFile(UUID uuid) {
		for (String key : yc.getKeys(false)) {
			if (uuid.toString().equalsIgnoreCase(key)) {
				ArrayList<UUID> friends = new ArrayList<UUID>();
				for (String str : yc.getConfigurationSection(key).getKeys(false)) {
					friends.add(UUID.fromString(str));
				}
				
				index.put(uuid, friends);
			}			
		}
	}
	
	/**
	 * Write a single friend list to the storage file.
	 * @param uuid - The UUID of the Player who's friends are being saved.
	 */
	public void writeToFile(UUID uuid) {
		yc.createSection(uuid.toString());
		for (UUID friendUUID : index.get(uuid)) {
			yc.getConfigurationSection(uuid.toString()).createSection(friendUUID.toString());
		}
		try {
			yc.save(this.friendFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds an empty friend list to the memory index.
	 * @param uuid - The UUID of the Player this friend list is for.
	 */
	public void addEmptyFriendList(UUID uuid) {
		index.put(uuid, new ArrayList<UUID>());
	}
}
