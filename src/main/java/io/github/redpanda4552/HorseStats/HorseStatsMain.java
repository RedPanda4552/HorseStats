package io.github.redpanda4552.HorseStats;

import io.github.redpanda4552.HorseStats.commands.*;
import io.github.redpanda4552.HorseStats.event.*;
import io.github.redpanda4552.HorseStats.utilities.Translate;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

import net.gravitydevelopment.updater.Updater;
import net.gravitydevelopment.updater.Updater.UpdateResult;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class HorseStatsMain extends JavaPlugin {
	
	protected Logger log;
	private final HorseStatsMain main = this; //Only because of the update checker.
	public Translate translate;
	
	public HashMap<String, Horse> teleportQueue = new HashMap<String, Horse>();
	
	/**
	 * The name of the newest update, if a new update exists.
	 */
	public String updateName;
	
	/**
	 * Whether or not an update is available. Used by AdminNotificationListener. 
	 * Don't delete this on accident again thinking it does nothing.
	 */
	public boolean updateAvailable = false;
	
	/**
	 * If HorseStats cannot retrieve speed due to mismatched Bukkit versions, this will be true.
	 */
	public boolean noSpeedMode = false;
	
	/**
	 * Set to true if the config is out of date.
	 */
	public boolean outofdateConfig = false;
	
	/**
	 * Registers all commands.
	 */
	private void registerCommands() {
		getCommand("horsestats").setExecutor(new Horsestats(this));
		getCommand("htp").setExecutor(new Htp(this));
		getCommand("setowner").setExecutor(new SetOwner(this));
		getCommand("untame").setExecutor(new Untame(this));
		getCommand("delchest").setExecutor(new Delchest(this));
		getCommand("delname").setExecutor(new Delname(this));
		getCommand("slayhorse").setExecutor(new Slayhorse(this));
		getCommand("hspawn").setExecutor(new Hspawn(this));
		getCommand("setstyle").setExecutor(new SetStyle(this));
		getCommand("setstat").setExecutor(new SetStat(this));
		getCommand("tame").setExecutor(new Tame(this));
	}
	
	/**
	 * Initializes the translation file.
	 * @return True if flawless or the file just had to be created. False if any other errors, or failure.
	 */
	public boolean initTranslate() {
		try {
			this.translate = new Translate(this);
			return true;
		} catch (IOException e) {
			//If the IOException is from the missing file
			if (e.getMessage().equalsIgnoreCase("Translate file not found.")) {
				log.info("Creating new 'translate.yml' in '<server root>/plugins/horsestats'");
			//Any other IOException
			} else {
				e.printStackTrace();
				log.warning("An unhandled IO exception was thrown while loading the HorseStats translation file." + 
						"Perhaps a line is not formatted correctly?");
			}
		}
		return false;
	}
	
	/**
	 * Checks if the server version matches the plugin version.
	 * If classes are found, no problem.
	 * If not, no speed mode is activated.
	 * 
	 * When CraftBukkit updates:<ol>
	 * <li>Replace imports in Event Handlers <del>and SetStat command</del></li>
	 * <li>Replace class strings below</li></ol>
	 *
	 * @return Boolean value indicating whether or not CB builds match.
	 */
	public boolean initMainEventListener() {
		try {
			Class.forName("net.minecraft.server.v1_7_R4.NBTBase"); //Checking vanilla MC
			Class.forName("org.bukkit.craftbukkit.v1_7_R4.entity.CraftHorse"); //Checking CB
			getServer().getPluginManager().registerEvents(new HorseStatsEventListener(this), this);
			return true;
		} catch (ClassNotFoundException e) {
			noSpeedMode = true;			
			log.warning("The version of CraftBukkit on this server does not match that of HorseStats.");
			log.warning("To avoid full plugin failure, the speed value in the stat display will be disabled.");
			log.warning("To fix this issue, get a HorseStats build that is made for your version of CraftBukkit.");
			getServer().getPluginManager().registerEvents(new HorseStatsNoSpeedEventListener(this), this);
			return false;
		}
	}
	
	/**
	 * Check if config is outdated. Warn if so.
	 * 
	 * Config version in config.yml and HorseStats version do not need to match exactly;
	 * if the config is updated, the config version will be updated to match HorseStats version.
	 * Otherwise it will remain the same.
	 * @return True if config is fine. False if outdated.
	 */
	public boolean checkConfiguration() {
		if (this.getConfig().getString("information.configVersion") == null || this.getConfig().getDouble("information.configVersion") != 3.1) {
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
				if (configBoolean("allowUpdateChecks") == true) {
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
	 * Bukkit standard void for plugin start up.
	 */
	public void onEnable() {
		this.log = this.getLogger();
		saveDefaultConfig();
		initTranslate();
		registerCommands();
		initMainEventListener();
		getServer().getPluginManager().registerEvents(new AdminNotificationListener(this), this);		
		getServer().getPluginManager().registerEvents(new HorseStatsHorseInteractListener(this), this);
		checkConfiguration();
		runUpdateChecker();
	}
	
	/**
	 * Gets a ConfigurationSection object from the given path.
	 * @param sectionName - The path to get the ConfigurationSection for.
	 * @return A ConfigurationSection object matching the given path.
	 */
	public ConfigurationSection section(String sectionName) {
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
	
	/**
	 * Checks if a player has the global override permission.
	 * @param p - The player to check permissions for.
	 * @return True if has permission, false if not.
	 */
	public boolean override(Player p) {
		if (p.hasPermission("HorseStats.globaloverride")) {
			return true;
		} else {
			return false;
		}
	}
}
