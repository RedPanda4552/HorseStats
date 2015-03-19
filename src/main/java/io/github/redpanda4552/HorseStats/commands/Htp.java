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

import io.github.redpanda4552.HorseStats.*;
import io.github.redpanda4552.HorseStats.translate.Translate;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;

//NEXTBUILD Add support for multiple horse teleporting.
public class Htp extends HorseStatsCommand {
	
	public Htp(HorseStatsMain main, Translate tl) {
		super(main, tl);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command,	String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			this.run(p);
		} else {
			sender.sendMessage(tl.generic("console"));
		}
		return true;
	}
	
	/**
	 * Does not work if chunks are not loaded.
	 * @param p - The player who initiated the teleport.
	 */
	public void run(Player p) {
		if (main.teleportQueue.get(p.getUniqueId()) == null) {
			p.sendMessage(tl.e + tl.htp("none-selected"));
		} else {
			Horse h = main.teleportQueue.get(p.getUniqueId());			
			if (main.configBoolean("multi-world-teleport") == false) {
				if (p.getWorld() != h.getWorld()) {
					p.sendMessage(tl.e + tl.htp("world-teleport"));
					return;
				}
			}
			
			if (h.teleport(p) == true) {
				p.sendMessage(tl.n + tl.htp("teleporting"));
			} else {
				p.sendMessage(tl.e + tl.htp("teleport-fail")); //Don't know what would cause teleport fail but just in case
			}
		}
	}
}
