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

import io.github.redpanda4552.HorseStats.commands.*;
import io.github.redpanda4552.HorseStats.event.*;
import io.github.redpanda4552.HorseStats.friend.FriendHelper;
import io.github.redpanda4552.HorseStats.translate.Translate;

import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Logger;

import net.gravitydevelopment.updater.Updater;
import net.gravitydevelopment.updater.Updater.UpdateResult;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Horse;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Main plugin class. Does all the initialization things.
 */
public class HorseStatsMain extends JavaPlugin {
	
	protected Logger log;
	private final HorseStatsMain main = this; //Only because of the update checker.
	public Translate tl;
	
	/**
	 * Class to facilitate friend actions.
	 */
	public FriendHelper friendHelper;
	
	/**
	 * A queue of horses selected and waiting for a teleport command.
	 */
	public HashMap<UUID, Horse> teleportQueue = new HashMap<UUID, Horse>();
	
	/**
	 * The name of the newest update, if a new update exists.
	 */
	public String updateName;
	
	/**
	 * Whether or not an update is available. Used by AdminNotificationListener. 
	 * Don't delete this on accident again thinking it does nothing.
	 * <b>Seriously, you almost did it a second time. This is important.</b>
	 */
	public boolean updateAvailable = false;
	
	/**
	 * If HorseStats cannot retrieve speed due to mismatched Bukkit versions, this will be true.
	 */
	public boolean noSpeedMode = false;
	
	public boolean outofdateConfig = false;
	
	public Material pMat;
	public String pMatName;
	public Material tMat;
	public String tMatName;
	
	/**
	 * Bukkit standard void for plugin start up.
	 */
	public void onEnable() {
		this.log = this.getLogger();
		saveDefaultConfig();
		this.tl = new Translate(this);
		this.friendHelper = new FriendHelper(this);
		
		noSpeedMode = testNoSpeedMode();
		
		pMat = Material.getMaterial(section("options").getString("stat-item"));
		pMatName = section("options").getString("stat-item-name");
		
		if (pMat == null) {
			log.warning("Bad stat display Material type in config! Defaulting to Lead.");
			pMat = Material.LEASH;
			pMatName = "Lead";
		}
		
		tMat = Material.getMaterial(main.section("options").getString("teleport-item"));
		tMatName = section("options").getString("teleport-item-name");
		
		if (tMat == null) {
			log.warning("Bad teleport select Material type in config! Defaulting to Ender Pearl.");
			tMat = Material.ENDER_PEARL;
			tMatName = "Ender Pearl";
		}
		
		getServer().getPluginManager().registerEvents(new PunchListener(this, tl), this);
		getServer().getPluginManager().registerEvents(new PlayerJoinLeaveListener(this, tl), this);		
		getServer().getPluginManager().registerEvents(new HorseInteractListener(this, tl), this);
		
		registerCommands();
		checkConfiguration();
		runUpdateChecker();
	}
	
	/**
	 * Registers all commands.
	 */
	private void registerCommands() {
		getCommand("horsestats").setExecutor(new Horsestats(this, tl));
		getCommand("htp").setExecutor(new Htp(this, tl));
		getCommand("setowner").setExecutor(new SetOwner(this, tl));
		getCommand("untame").setExecutor(new Untame(this, tl));
		getCommand("delchest").setExecutor(new Delchest(this, tl));
		getCommand("delname").setExecutor(new Delname(this, tl));
		getCommand("slayhorse").setExecutor(new Slayhorse(this, tl));
		getCommand("hfriend").setExecutor(new HFriend(this, tl));
		getCommand("hspawn").setExecutor(new Hspawn(this, tl));
		getCommand("setstyle").setExecutor(new SetStyle(this, tl));
		getCommand("setstat").setExecutor(new SetStat(this, tl));
		getCommand("tame").setExecutor(new Tame(this, tl));
	}
	
	/**
	 * Checks if the server version matches the plugin version.
	 * If classes are found, no problem.
	 * If not, no speed mode is activated.
	 * @return Boolean value indicating whether noSpeedMode should or should not be active.
	 */
	public boolean testNoSpeedMode() {
		try {
			Class.forName("org.bukkit.craftbukkit.v1_8_R1.entity.CraftHorse");
			return false;
		} catch (ClassNotFoundException e) {
			noSpeedMode = true;			
			log.warning("The version of CraftBukkit/Spigot on this server does not match that of HorseStats.");
			log.warning("To avoid full plugin failure, the speed value in the stat display will be disabled.");
			log.warning("To fix this issue, get a HorseStats build that is made for your version of CraftBukkit.");
			return true;
		}
	}
	
	/**
	 * Check if config is outdated. Warn if so.
	 * Config version in config.yml and HorseStats version do not need to match exactly;
	 * if the config is updated, the config version will be updated to match HorseStats version.
	 * Otherwise it will remain the same.
	 * @return True if config is fine. False if outdated.
	 */
	public boolean checkConfiguration() {
		if (this.getConfig().getString("config-version") == null || this.getConfig().getDouble("config-version") != 3.3) {
			outofdateConfig = true;			
			log.warning("It appears your HorseStats configuration file is out of date.");
			log.warning("Please take note of the settings you have in it, and delete it.");
			log.warning("A new configuration with new settings will generate next time you start or reload your server.");
			return false;
		}
		return true;
	}
	
	/**
	 * Update notifier; only notifies, does not download.
	 */
	private void runUpdateChecker() {
		new BukkitRunnable() {
			@Override
			public void run() {
				if (configBoolean("update-checks") == true) {
					Updater updater = new Updater(main, 62378, main.getFile(), Updater.UpdateType.NO_DOWNLOAD, false);
					if (updater.getResult() == UpdateResult.UPDATE_AVAILABLE) {
						updateName = updater.getLatestName();
						updateAvailable = true;
						log.info("A new build of HorseStats is available!");
						log.info("You can find " + updater.getLatestName() + " at: https://dev.bukkit.org/bukkit-plugins/horsestats");
					}
				}	
			}
		}.runTaskAsynchronously(main);
			
	}
	
	/**
	 * Gets a ConfigurationSection object from the given path.
	 * Convenience method to avoid having to grab config first.
	 * @param sectionName - The path to get the ConfigurationSection for.
	 * @return A ConfigurationSection object matching the given path.
	 */
	private ConfigurationSection section(String sectionName) {
		ConfigurationSection section = this.getConfig().getConfigurationSection(sectionName);
		return section;
	}
	
	/**
	 * Returns the boolean value of the specified option node. This method is <b><i>exclusive</i></b>
	 * to the "options" section of the configuration.
	 * @param configBoolean - The boolean to be checked.
	 * @return The boolean value of the specified node.
	 */
	public boolean configBoolean(String configBoolean) {
		if (section("options").getBoolean(configBoolean) == true) {
			return true;
		}
		return false;
	}
}
