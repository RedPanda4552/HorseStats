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
package io.github.redpanda4552.HorseStats.commands;

import static org.bukkit.ChatColor.*;
import io.github.redpanda4552.HorseStats.HorseStatsCommand;
import io.github.redpanda4552.HorseStats.HorseStatsMain;
import io.github.redpanda4552.HorseStats.translate.Translate;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class Horsestats extends HorseStatsCommand { 
	
	public Horsestats(HorseStatsMain main, Translate tl) {
		super(main, tl);
	}
	
	private String[] genHelp =
	{ " "
	, GREEN  + "========================"
	, YELLOW + "HorseStats v" + main.getDescription().getVersion() + " " + tl.horsestats("by") + " 'bdubz4552'"
	, GREEN  + "========================"
	, YELLOW + tl.horsestats("choose")
	, GREEN  + "/hs stats"
	, GREEN  + "/hs teleport"
	, GREEN  + "/hs protect <grief | interact | friends>"
	, GREEN  + tl.horsestats("command")
	, YELLOW + "/help horsestats" + GREEN + "."
	};
	
	private String[] statHelp = 
	{ " "
	, YELLOW + tl.horsestats("stat-title")
	, GREEN  + tl.horsestats("instruction-1") + " " + YELLOW + main.pMatName + GREEN + ". "
	, GREEN  + tl.horsestats("instruction-2")
	, GREEN  + tl.horsestats("stat-1")
	, GREEN  + tl.horsestats("stat-2")
	, GREEN  + tl.horsestats("note-1")
	, GREEN  + tl.horsestats("note-2")
	};
	
	private String[] antiGriefHelp = 
	{ " "
	, YELLOW + tl.horsestats("anti-grief-title")
	, GREEN  + tl.horsestats("anti-grief-1")
	, GREEN  + tl.horsestats("anti-grief-2")
	, GREEN  + tl.horsestats("access-reasons")
	, GREEN  + tl.horsestats("reason-1")
	, GREEN  + tl.horsestats("reason-2")
	, GREEN  + tl.horsestats("reason-3")
	};
	
	private String[] antiInteractHelp = 
	{ " "
	, YELLOW + tl.horsestats("anti-interact-title")
	, GREEN  + tl.horsestats("anti-interact-1")
	, GREEN  + tl.horsestats("anti-interact-2")
	, GREEN  + tl.horsestats("access-reasons")
	, GREEN  + tl.horsestats("reason-1")
	, GREEN  + tl.horsestats("reason-2")
	, GREEN  + tl.horsestats("reason-3")
	};
	
	private String[] friendHelp = 
	{ " "
	, YELLOW + tl.horsestats("friend-title")
	, GREEN  + tl.horsestats("friend-1") + YELLOW + " /hfriend" + GREEN + "."
	, GREEN  + tl.horsestats("friend-2")
	, GREEN  + tl.horsestats("friend-3")+ YELLOW + " /untame " + GREEN + tl.horsestats("and") + YELLOW + " /setowner " + GREEN + tl.horsestats("friend-4")
	, GREEN  + tl.horsestats("friend-5")
	};
	
	private String[] teleportHelp = 
	{ " "
	, YELLOW + tl.horsestats("teleport-title")
	, GREEN  + tl.horsestats("teleport-1") + " " + YELLOW + main.tMatName + GREEN + "."
	, GREEN  + tl.horsestats("teleport-2")
	, GREEN  + tl.horsestats("teleport-3") + YELLOW + " /htp " + GREEN + tl.horsestats("teleport-4")
	, GREEN  + tl.horsestats("teleport-5")
	};
	
	@Override
	public boolean onCommand(CommandSender sender, Command command,	String label, String[] args) {
		if (args.length >= 1) {
			if (args[0].equalsIgnoreCase("stats")) {
				sender.sendMessage(statHelp);
				return true;
			} else if (args[0].equalsIgnoreCase("teleport")) {
				sender.sendMessage(teleportHelp);
				return true;
			} else if (args[0].equalsIgnoreCase("protect")) {
				if (args.length >= 2) {
					if (args[1].equalsIgnoreCase("grief")) {
						sender.sendMessage(antiGriefHelp);
						return true;
					} else if (args[1].equalsIgnoreCase("interact")) {
						sender.sendMessage(antiInteractHelp);
						return true;
					} else if (args[1].equalsIgnoreCase("friend")) {
						sender.sendMessage(friendHelp);
						return true;
					}
				}
			}
		}
		sender.sendMessage(genHelp);
		return true;
	}
}
