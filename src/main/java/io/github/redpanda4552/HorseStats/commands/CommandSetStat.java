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

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;

public class CommandSetStat extends AbstractCommand {
    
    public CommandSetStat(HorseStats main) {
        super(main);
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
            
            this.run(p, h, args);
        } else {
            sender.sendMessage(lang.get("generic.console"));
        }
        return true;
    }
    
    public void run(Player p, Horse h, String args[]) {
        if (h != null) {
            if (hasPermission(p, h, InteractionType.USE)) {
                if (args.length == 2) {            
                    if (args[0].equalsIgnoreCase("health")) {
                        double health = Double.parseDouble(args[1]);
                        
                        if (health > 1024) {
                            health = 1024;
                            p.sendMessage(lang.tag + lang.r + lang.get("setStat.health-limit"));
                            return;
                        }
                        
                        h.setMaxHealth(2 * health);
                        h.setHealth(2 * health);
                        p.sendMessage(lang.tag + lang.get("setStat.health-set-to") + " " + health + " " + lang.get("setStat.hearts"));
                    } else if (args[0].equalsIgnoreCase("jump")) {
                        double jump = Double.parseDouble(args[1]);
                        
                        if (jump > 22) {
                            jump = 22;
                            p.sendMessage(lang.tag + lang.r + lang.get("setStat.jump-limit"));
                            return;
                        }
                        
                        h.setJumpStrength(Math.sqrt(jump / 5.5));
                        p.sendMessage(lang.tag + lang.get("setStat.jump-set-to") + " " + jump + " " + lang.get("setStat.blocks"));
                    } else {
                        p.sendMessage(lang.tag + lang.get("setStat.usage"));
                    }
                } else {
                    p.sendMessage(lang.tag + lang.get("setStat.usage"));
                }
            } else {
                p.sendMessage(lang.tag + lang.r + lang.get("generic.owner"));
            }
        } else {
            p.sendMessage(lang.tag + lang.r + lang.get("generic.riding"));
        }
    }
}
