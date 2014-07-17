package me.bdubz4552.horsestats;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

//TODO Full refactor
public abstract class HorseStatsCommand implements CommandExecutor {
	
	protected final String normalTag = ChatColor.YELLOW + "[HorseStats] " + ChatColor.GREEN;
	protected final String errorTag = ChatColor.YELLOW + "[HorseStats] " + ChatColor.RED;
	protected final String statLine = ChatColor.GREEN + "";
	
	protected HorseStatsMain main;
	
	public HorseStatsCommand(HorseStatsMain main) {
		this.main = main;
	}
	
	/**
	 * Send a normal message to a player with HorseStats tag.
	 * @param player - The player to send the message to
	 * @param section - The section of the translate file to look in
	 * @param path - The specific key in the section
	 */
	public void sendNormal(Player player, ConfigurationSection section, String path) {
		if (section == null || path == null) {
			player.sendMessage(errorTag + "String translate failure at '" + section.toString() + "." + path + "'. "
					+ "Please check your translate.yml for errors.");
		}
		player.sendMessage(normalTag + section.getString(path));
	}
	
	/**
	 * Send a normal message to a player with HorseStats tag.
	 * @param player - The player to send the message to
	 * @param message - The message to send
	 */
	public void sendNormal(Player player, String message) {
		player.sendMessage(normalTag + message);
	}
	
	/**
	 * Send an error message to a player with HorseStats tag.
	 * @param player - The player to send the message to
	 * @param section - The section of the translate file to look in
	 * @param path - The specific key in the section
	 */
	public void sendError(Player player, ConfigurationSection section, String path) {
		if (section == null || path == null) {
			player.sendMessage(errorTag + "String translate failure at '" + section.toString() + "." + path + "'. "
					+ "Please check your translate.yml for errors.");
		}
		player.sendMessage(errorTag + section.getString(path));
	}
	
	/**
	 * Send a stat message to a player with HorseStats tag.
	 * @param player - The player to send the message to
	 * @param section - The section of the translate file to look in
	 * @param path - The specific key in the section
	 */
	public void sendStat(Player player, ConfigurationSection section, String path) {
		if (section == null || path == null) {
			player.sendMessage(errorTag + "String translate failure at '" + section.toString() + "." + path + "'. "
					+ "Please check your translate.yml for errors.");
		}
		player.sendMessage(statLine + section.getString(path));
	}
}
