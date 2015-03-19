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
package io.github.redpanda4552.HorseStats.translate;

import io.github.redpanda4552.HorseStats.HorseStatsMain;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

public class Translate {
	
	private File file;
	private YamlConfiguration tf;
	
	//Message prefixes; n = normal, e = error, s = stat; NES acronym not intended.
	public final String n = ChatColor.YELLOW + "[HorseStats] " + ChatColor.GREEN;
	public final String e = ChatColor.YELLOW + "[HorseStats] " + ChatColor.RED;
	public final String s = ChatColor.GREEN + "";
	
	public Translate(HorseStatsMain main) {
		this.file = new File("plugins/HorseStats/translate.yml");
		
		if (!file.exists()) {
			main.saveResource("translate.yml", false);
			this.file = new File("plugins/HorseStats/translate.yml");
		}
		tf = YamlConfiguration.loadConfiguration(this.file);
	}
	
	/**
	 * Gets the String specified by <b>Path</b>, within the translate section PlayerJoinLeaveListener
	 * @param path - The YAML header to check in this section
	 * @return The translated String at the specified YAML header
	 */
	public final String playerJoinLeave(String path) {
		return tf.getConfigurationSection("PlayerJoinLeaveListener").getString(path);
	}
	
	/**
	 * Gets the String specified by <b>Path</b>, within the translate section Event
	 * @param path - The YAML header to check in this section
	 * @return The translated String at the specified YAML header
	 */
	public final String event(String path) {
		return tf.getConfigurationSection("Event").getString(path);
	}
	
	/**
	 * Gets the String specified by <b>Path</b>, within the translate section Generic
	 * @param path - The YAML header to check in this section
	 * @return The translated String at the specified YAML header
	 */
	public final String generic(String path) {
		return tf.getConfigurationSection("Generic").getString(path);
	}
	
	/**
	 * Gets the String specified by <b>Path</b>, within the translate section HorseStats
	 * @param path - The YAML header to check in this section
	 * @return The translated String at the specified YAML header
	 */
	public final String horsestats(String path) {
		return tf.getConfigurationSection("HorseStats").getString(path);
	}
	
	/**
	 * Gets the String specified by <b>Path</b>, within the translate section SetStat
	 * @param path - The YAML header to check in this section
	 * @return The translated String at the specified YAML header
	 */
	public final String setStat(String path) {
		return tf.getConfigurationSection("SetStat").getString(path);
	}
	
	/**
	 * Gets the String specified by <b>Path</b>, within the translate section SetOwner
	 * @param path - The YAML header to check in this section
	 * @return The translated String at the specified YAML header
	 */
	public final String setOwner(String path) {
		return tf.getConfigurationSection("SetOwner").getString(path);
	}
	
	/**
	 * Gets the String specified by <b>Path</b>, within the translate section SetStyle
	 * @param path - The YAML header to check in this section
	 * @return The translated String at the specified YAML header
	 */
	public final String setStyle(String path) {
		return tf.getConfigurationSection("SetStyle").getString(path);
	}
	
	/**
	 * Gets the String specified by <b>Path</b>, within the translate section Tame
	 * @param path - The YAML header to check in this section
	 * @return The translated String at the specified YAML header
	 */
	public final String tame(String path) {
		return tf.getConfigurationSection("Tame").getString(path);
	}
	
	/**
	 * Gets the String specified by <b>Path</b>, within the translate section Htp
	 * @param path - The YAML header to check in this section
	 * @return The translated String at the specified YAML header
	 */
	public final String htp(String path) {
		return tf.getConfigurationSection("Htp").getString(path);
	}
	
	/**
	 * Gets the String specified by <b>Path</b>, within the translate section Delname
	 * @param path - The YAML header to check in this section
	 * @return The translated String at the specified YAML header
	 */
	public final String delname(String path) {
		return tf.getConfigurationSection("Delname").getString(path);
	}
	
	/**
	 * Gets the String specified by <b>Path</b>, within the translate section Delchest
	 * @param path - The YAML header to check in this section
	 * @return The translated String at the specified YAML header
	 */
	public final String delchest(String path) {
		return tf.getConfigurationSection("Delchest").getString(path);
	}
	
	/**
	 * Gets the String specified by <b>Path</b>, within the translate section Slayhorse
	 * @param path - The YAML header to check in this section
	 * @return The translated String at the specified YAML header
	 */
	public final String slayhorse(String path) {
		return tf.getConfigurationSection("Slayhorse").getString(path);
	}
	
	/**
	 * Gets the String specified by <b>Path</b>, within the translate section Hspawn
	 * @param path - The YAML header to check in this section
	 * @return The translated String at the specified YAML header
	 */
	public final String hspawn(String path) {
		return tf.getConfigurationSection("Hspawn").getString(path);
	}
	
	/**
	 * Gets the String specified by <b>Path</b>, within the translate section Hspawn
	 * @param path - The YAML header to check in this section
	 * @return The translated String at the specified YAML header
	 */
	public final String hFriend(String path) {
		return tf.getConfigurationSection("HFriend").getString(path);
	}
	
	/**
	 * Gets the String specified by <b>Path</b>, within the translate section Untame
	 * @param path - The YAML header to check in this section
	 * @return The translated String at the specified YAML header
	 */
	public final String untame(String path) {
		return tf.getConfigurationSection("Untame").getString(path);
	}
}
