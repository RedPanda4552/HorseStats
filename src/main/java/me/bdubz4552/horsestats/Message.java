package me.bdubz4552.horsestats;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.bdubz4552.horsestats.translate.Translate;

/**
 * Preset Message enums with type imbedded. Each type has a note to make recognition easier.
 */
public enum Message {
	//Base messages
	/** HorseStats tag and Green text */
	NORMAL(ChatColor.YELLOW + "[HorseStats] " + ChatColor.GREEN),
	/** HorseStats tag and red text */
	ERROR(ChatColor.YELLOW + "[HorseStats] " + ChatColor.RED),
	/** Green Text */
	STAT(ChatColor.GREEN + ""),
	
	//Delchest
	/** Chest Deleted. */
	CHEST_DELETED(NORMAL.getString() + Translate.message("chestDelete")),
	
	//Delname
	/** Custom name erased. */
	NAME_ERASED(NORMAL.getString() + Translate.message("nameErase")),
	
	//Hspawn
	/** Horse Spawned */
	HORSE(NORMAL.getString() + Translate.message("horseSpawn")),
	/** Donkey Spawned */
	DONKEY(NORMAL.getString() + Translate.message("donkeySpawn")),
	/** Mule Spawned */
	MULE(NORMAL.getString() + Translate.message("muleSpawn")),
	/** Cannot be riding a horse */
	NOT_RIDING(ERROR.getString() + Translate.message("cannotRide")),
	
	//htp
	/** No horse selected */
	NONE_SELECTED(ERROR.getString() + Translate.message("noneSelected")),
	/** Can't tp between worlds */
	INTER_WORLD(ERROR.getString() + Translate.message("worldTP")),
	/** Teleporting */
	TELEPORTING(NORMAL.getString() + Translate.message("teleporting")),
	/** Teleport failed */
	TELEPORT_FAIL(ERROR.getString() + Translate.message("teleportFail")),
	
	//Setowner
	/** Changed owner */
	OWNER_CHANGED(NORMAL.getString() + Translate.message("ownerChange")),
	
	//Setstat
	/** Horses jump no higher than 22 */
	JUMP_HEIGHT(ERROR.getString() + Translate.message("jumpLimit")),
	
	//Setstyle
	/** Only horses can be modified */
	ONLY_MODIFY_HORSE(ERROR.getString() + Translate.message("onlyHorse")),
	
	//Slayhorse
	/** He's a magical pony */
	LAUNCH(Translate.message("launch")),
	/** Slain */
	SLAIN(NORMAL.getString() + Translate.message("slain")),
	
	//Tame
	/** Now own this horse */
	NOW_OWN(NORMAL.getString() + Translate.message("nowOwn")),
	/** Already own this horse */
	ALREADY_OWN(NORMAL.getString() + Translate.message("alreadyOwn")),
	
	//Untame
	/** Untamed */
	UNTAME(NORMAL.getString() + Translate.message("untame")),
	
	//Admin listener
	/** Config outdated */
	CONFIG_WARN(NORMAL.getString() + Translate.message("configWarning")),
	/** No speed mode active */
	NO_SPEED_WARN(NORMAL.getString() + Translate.message("noSpeedWarning")),
	
	//Main listener
	/** Fixed owner-tamed continuity error */
	OWNER_FIX(NORMAL.getString() + Translate.message("ownerFix")),
	/** Horse selected for teleport */
	TELEPORT_SELECTED(NORMAL.getString() + Translate.message("teleportSelected")),
	/** Horse deselected for teleport */
	TELEPORT_DESELECTED(NORMAL.getString() + Translate.message("teleportDeselected")),
	/** Must be tamed */
	TELEPORT_TAME(ERROR.getString() + Translate.message("teleportUntame")),
	
	//Error Messages
	RIDING(ERROR.getString() + Translate.message("riding")),
	PERMS(ERROR.getString() + Translate.message("permissions")),
	ATTACK(ERROR.getString() + Translate.message("attack")),
	OWNER(ERROR.getString() + Translate.message("owner")),
	STYLE_PARAMS(ERROR.getString() + Translate.message("setstyleArgs")),
	PLAYER(ERROR.getString() + Translate.message("playerNotFound")),
	CONSOLE(ERROR.getString() + Translate.message("console"));
	
	public final String message;
	
	private Message(String str) {
		message = str;
	}
	
	public String getString() {
		return message;
	}
	
	/**
	 * Send a message to a player, with a specific string.
	 * @param p - Player to receive the message.
	 * @param msg - The message to be received.
	 */
	public void send(Player p, String string) {
		p.sendMessage(this.message + string);
	}

	/**
	 * Sends a message to a player, using one of the predefined Message enums.
	 * @param p - Player to receive the message.
	 */
	public void send(Player p) {
		p.sendMessage(this.message);
	}
}
