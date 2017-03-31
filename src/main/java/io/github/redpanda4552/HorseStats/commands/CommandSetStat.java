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

import io.github.redpanda4552.HorseStats.HorseStats;
import io.github.redpanda4552.HorseStats.friend.InteractionType;

import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;

public class CommandSetStat extends AbstractCommand {
    
    public CommandSetStat(HorseStats main) {
        super(main);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            AbstractHorse h = null;
            
            if (p.isInsideVehicle()) {
                if (p.getVehicle() instanceof AbstractHorse) {
                    h = (AbstractHorse) p.getVehicle();
                }
            }
            
            run(p, h, args);
        } else {
            sender.sendMessage(lang.get("generic.console"));
        }
        return true;
    }
    
    public void run(Player p, AbstractHorse h, String args[]) {
        if (h == null) {
            p.sendMessage(lang.tag + lang.r + lang.get("generic.riding"));
            return;
        }
        
        if (!hasPermission(p, h, InteractionType.USE)) {
            p.sendMessage(lang.tag + lang.r + lang.get("generic.owner"));
            return;
        }
        
        if (args.length >= 2) {            
            if (args[0].equalsIgnoreCase("health")) {
                double health = 0;
                
                try {
                    health = Double.parseDouble(args[1]);
                } catch (NumberFormatException e) {
                    p.sendMessage(lang.tag + lang.r + args[1] + " " + lang.get("setStat.nan"));
                }
                
                if (health > 1024) {
                    health = 1024;
                    p.sendMessage(lang.tag + lang.r + lang.get("setStat.health-limit"));
                    return;
                }
                
                // A bit of a roundabout compared to the old way, but I guess I could call this "more descriptive"?
                h.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(2 * health);
                h.setHealth(2 * health);
                p.sendMessage(lang.tag + lang.get("setStat.health-set-to") + " " + health + " " + lang.get("setStat.hearts"));
            } else if (args[0].equalsIgnoreCase("jump")) {
                double jump = 0;
                
                try {
                    jump = Double.parseDouble(args[1]);
                } catch (NumberFormatException e) {
                    p.sendMessage(lang.tag + lang.r + args[1] + " " + lang.get("setStat.nan"));
                }
                
                // This magic number is the absolute highest a horse can jump (the limit on the internal value is 2; this is the height in blocks)
                if (jump > 16.4544254158757) {
                    jump = 16.4544254158757;
                    p.sendMessage(lang.tag + lang.r + lang.get("setStat.jump-limit"));
                    return;
                }
                
                // These magic numbers were derived from a bunch of data collection. Maybe I'll throw the spreadsheet in the git repo.
                h.setJumpStrength(0.3846 * Math.pow(jump, 0.5821));
                p.sendMessage(lang.tag + lang.get("setStat.jump-set-to") + " " + jump + " " + lang.get("setStat.blocks"));
            } else if (args[0].equalsIgnoreCase("speed")) {
                double speed = 0;
                
                try {
                    speed = Double.parseDouble(args[1]);
                } catch (NumberFormatException e) {
                    p.sendMessage(lang.tag + lang.r + args[1] + " " + lang.get("setStat.nan"));
                }
                
                h.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(speed / 42.18);
                p.sendMessage(lang.tag + lang.get("setStat.speed-set-to") + " " + speed + " " + lang.get("setStat.blocks-per-second"));
            } else {
                p.sendMessage(lang.tag + lang.get("setStat.usage"));
            }
        } else {
            p.sendMessage(lang.tag + lang.get("setStat.usage"));
        }
    }
}