package io.github.redpanda4552.HorseStats.utilities;

import io.github.redpanda4552.HorseStats.HorseStatsMain;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Translate {
	
	private File file;
	private static MemoryConfiguration translateFile;
	
	public static ConfigurationSection adminNotificationListener;
	public static ConfigurationSection horseStatsEventListener;
	public static ConfigurationSection horsestats;//Currently nothing.
	public static ConfigurationSection generic;
	public static ConfigurationSection delchest;
	public static ConfigurationSection delname;
	public static ConfigurationSection hspawn;
	public static ConfigurationSection htp;	
	public static ConfigurationSection setOwner;
	public static ConfigurationSection setStat;
	public static ConfigurationSection setStyle;
	public static ConfigurationSection slayhorse;
	public static ConfigurationSection tame;
	public static ConfigurationSection untame;
	
	/**
	 * Constructor for translation.
	 * @param main - HorseStatsMain
	 * @throws IOException - Thrown if translate.yml is not found.
	 */
	public Translate(HorseStatsMain main) throws IOException {
		this.file = new File("plugins/HorseStats/translate.yml");
		
		if (!file.exists()) {
			main.saveResource("translate.yml", false);
			throw new IOException("Translate file not found.");	
		}
		instantiateSections();
	}
	
	public void instantiateSections() {
		YamlConfiguration yc = YamlConfiguration.loadConfiguration(this.file);
		translateFile = yc;
		adminNotificationListener = translateFile.getConfigurationSection("AdminNotificationListener");
		horseStatsEventListener = translateFile.getConfigurationSection("HorseStatsEventListener");
		horsestats = translateFile.getConfigurationSection("HorseStats");
		generic = translateFile.getConfigurationSection("Generic");
		delchest = translateFile.getConfigurationSection("Delchest");
		delname = translateFile.getConfigurationSection("Delname");
		hspawn = translateFile.getConfigurationSection("Hspawn");
		htp = translateFile.getConfigurationSection("Htp");
		setOwner = translateFile.getConfigurationSection("SetOwner");
		setStat = translateFile.getConfigurationSection("SetStat");
		setStyle = translateFile.getConfigurationSection("SetStyle");
		slayhorse = translateFile.getConfigurationSection("Slayhorse");
		tame = translateFile.getConfigurationSection("Tame");
		untame = translateFile.getConfigurationSection("Untame");
	}
	
	/**
	 * Gets the String specified by <b>Path</b>, within the translate section AdminNotificationListener
	 * @param path - The YAML header to check in this section
	 * @return The translated String at the specified YAML header
	 */
	public static final String admin(String path) {
		return adminNotificationListener.getString(path);
	}
	
	/**
	 * Gets the String specified by <b>Path</b>, within the translate section HorseStatsEventListener
	 * @param path - The YAML header to check in this section
	 * @return The translated String at the specified YAML header
	 */
	public static final String event(String path) {
		return horseStatsEventListener.getString(path);
	}
	
	/**
	 * Gets the String specified by <b>Path</b>, within the translate section Generic
	 * @param path - The YAML header to check in this section
	 * @return The translated String at the specified YAML header
	 */
	public static final String generic(String path) {
		return generic.getString(path);
	}
	
	/**
	 * Gets the String specified by <b>Path</b>, within the translate section HorseStats
	 * @param path - The YAML header to check in this section
	 * @return The translated String at the specified YAML header
	 */
	public static final String horsestats(String path) {
		return horsestats.getString(path);
	}
	
	/**
	 * Gets the String specified by <b>Path</b>, within the translate section SetStat
	 * @param path - The YAML header to check in this section
	 * @return The translated String at the specified YAML header
	 */
	public static final String setstat(String path) {
		return setStat.getString(path);
	}
	
	/**
	 * Gets the String specified by <b>Path</b>, within the translate section SetOwner
	 * @param path - The YAML header to check in this section
	 * @return The translated String at the specified YAML header
	 */
	public static final String setowner(String path) {
		return setOwner.getString(path);
	}
	
	/**
	 * Gets the String specified by <b>Path</b>, within the translate section SetStyle
	 * @param path - The YAML header to check in this section
	 * @return The translated String at the specified YAML header
	 */
	public static final String setStyle(String path) {
		return setStyle.getString(path);
	}
	
	/**
	 * Gets the String specified by <b>Path</b>, within the translate section Tame
	 * @param path - The YAML header to check in this section
	 * @return The translated String at the specified YAML header
	 */
	public static final String tame(String path) {
		return tame.getString(path);
	}
	
	/**
	 * Gets the String specified by <b>Path</b>, within the translate section Htp
	 * @param path - The YAML header to check in this section
	 * @return The translated String at the specified YAML header
	 */
	public static final String htp(String path) {
		return htp.getString(path);
	}
	
	/**
	 * Gets the String specified by <b>Path</b>, within the translate section Delname
	 * @param path - The YAML header to check in this section
	 * @return The translated String at the specified YAML header
	 */
	public static final String delname(String path) {
		return delname.getString(path);
	}
	
	/**
	 * Gets the String specified by <b>Path</b>, within the translate section Delchest
	 * @param path - The YAML header to check in this section
	 * @return The translated String at the specified YAML header
	 */
	public static final String delchest(String path) {
		return delchest.getString(path);
	}
	
	/**
	 * Gets the String specified by <b>Path</b>, within the translate section Slayhorse
	 * @param path - The YAML header to check in this section
	 * @return The translated String at the specified YAML header
	 */
	public static final String slayhorse(String path) {
		return slayhorse.getString(path);
	}
	
	/**
	 * Gets the String specified by <b>Path</b>, within the translate section Hspawn
	 * @param path - The YAML header to check in this section
	 * @return The translated String at the specified YAML header
	 */
	public static final String hspawn(String path) {
		return hspawn.getString(path);
	}
	
	/**
	 * Gets the String specified by <b>Path</b>, within the translate section Untame
	 * @param path - The YAML header to check in this section
	 * @return The translated String at the specified YAML header
	 */
	public static final String untame(String path) {
		return untame.getString(path);
	}
}
