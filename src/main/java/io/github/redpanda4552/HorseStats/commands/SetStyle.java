package io.github.redpanda4552.HorseStats.commands;

import static org.bukkit.ChatColor.*;
import io.github.redpanda4552.HorseStats.HorseStatsCommand;
import io.github.redpanda4552.HorseStats.HorseStatsMain;
import io.github.redpanda4552.HorseStats.utilities.Translate;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.Variant;
import org.bukkit.entity.Player;
import org.bukkit.entity.Horse.Color;
import org.bukkit.entity.Horse.Style;


public class SetStyle extends HorseStatsCommand {
	
	public SetStyle(HorseStatsMain horseStatsMain) {
		super(horseStatsMain);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,	String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			Horse h = null;
			if (p.isInsideVehicle()) {
				if (p.getVehicle() instanceof Horse) {
					h = (Horse) p.getVehicle();
				}
			}
			this.run(p, h, args);
		} else {
			sender.sendMessage(Translate.generic("console"));
		}
		return true;
	}

	public void run(Player p, Horse h, String[] args) {
		if (h != null) {
			if (h.getOwner() == p || main.override(p)) {
				if (h.getVariant() == Variant.HORSE) {
					if (args.length >= 2) {
						if (args[0].equalsIgnoreCase("color")) {
							if (args[1].equalsIgnoreCase("black")) {
								h.setColor(Color.BLACK);
							}
							else if (args[1].equalsIgnoreCase("brown")) {
								h.setColor(Color.BROWN);
							}
							else if (args[1].equalsIgnoreCase("chestnut")) {
								h.setColor(Color.CHESTNUT);
							}
							else if (args[1].equalsIgnoreCase("creamy")) {
								h.setColor(Color.CREAMY);
							}

							else if (args[1].equalsIgnoreCase("darkbrown")) {
								h.setColor(Color.DARK_BROWN);
							}
							else if (args[1].equalsIgnoreCase("gray")) {
								h.setColor(Color.GRAY);
							}
							else if (args[1].equalsIgnoreCase("white")) {
								h.setColor(Color.WHITE);
							} else {
								this.sendError(p, Translate.setStyle, "styleParams");
							}
						}
						if (args[0].equalsIgnoreCase("style")) {
							if (args[1].equalsIgnoreCase("blackdots")) {
								h.setStyle(Style.BLACK_DOTS);
							}
							else if (args[1].equalsIgnoreCase("none")) {
								h.setStyle(Style.NONE);
							}
							else if (args[1].equalsIgnoreCase("white")) {
								h.setStyle(Style.WHITE);
							}
							else if (args[1].equalsIgnoreCase("whitedots")) {
								h.setStyle(Style.WHITE_DOTS);
							}
							else if (args[1].equalsIgnoreCase("whitefield")) {
								h.setStyle(Style.WHITEFIELD);
							} else {
								this.sendError(p, Translate.setStyle, "styleParams");
							}
						}
					} else if (args.length == 1){
						if (args[0].equals("?")) {
							setstatHelp(p);
						} else {
							this.sendError(p, Translate.setStyle, "styleParams");
						}
					} else {
						this.sendError(p, Translate.setStyle, "styleParams");
					}
				} else {
					this.sendError(p, Translate.setStyle, "onlyHorse");
				}
			} else {
				this.sendError(p, Translate.generic, "owner");
			}
		} else {
			this.sendError(p, Translate.generic, "riding");
		}
	}
	public void setstatHelp(Player p) {
		String[] styleHelp =
		{ YELLOW + "/setstyle <color|style> <value>"
		, YELLOW + "Styles:"
		, GREEN  + "none, blackdots, whitedots, white, whitefield"
		, YELLOW + "Colors:"
		, GREEN  + "white, brown, chestnut, creamy, darkbrown, gray, black"
		};
		p.sendMessage(styleHelp);
	}
}
