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

import io.github.redpanda4552.HorseStats.HorseStatsCommand;
import io.github.redpanda4552.HorseStats.HorseStatsMain;
import io.github.redpanda4552.HorseStats.translate.Translate;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;

public class Delname extends HorseStatsCommand {
    
    public Delname(HorseStatsMain main, Translate tl) {
        super(main, tl);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command,    String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            Horse h = null;
            if (p.isInsideVehicle()) {
                if (p.getVehicle() instanceof Horse) {
                    h = (Horse) p.getVehicle();
                }
            }
            this.run(p, h);
        } else {
            sender.sendMessage(tl.generic("console"));
        }
        return true;
    }
    
    public void run(Player p, Horse h) {
        if (h != null) {
            if (this.canAccess(h, p)) {
                h.setCustomName(null);
                p.sendMessage(tl.n + tl.delname("name-delete"));
            } else {
                p.sendMessage(tl.e + tl.generic("owner"));
            }
        } else {
            p.sendMessage(tl.e + tl.generic("riding"));
        }
    }
}
