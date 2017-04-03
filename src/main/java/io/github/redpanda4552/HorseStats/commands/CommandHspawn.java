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

import io.github.redpanda4552.HorseStats.Main;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Donkey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.entity.SkeletonHorse;
import org.bukkit.entity.ZombieHorse;
import org.bukkit.entity.Llama;
import org.bukkit.entity.Mule;

public class CommandHspawn extends AbstractCommand {
    
    private final String usage = "/hspawn <horse | donkey | mule | llama | skeleton | zombie>";
    
    public CommandHspawn(Main main) {
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
    
    public void run(Player p, AbstractHorse h, String[] args) {
        if (h != null) {
            p.sendMessage(lang.tag + lang.r + lang.get("generic.cannot-ride"));
            return;
        }
        
        if (args.length >= 1) {
            switch (args[0].toLowerCase()) {
            case "horse":
                h = (Horse) p.getWorld().spawnEntity(p.getLocation(), EntityType.HORSE);
                break;
            case "donkey":
                h = (Donkey) p.getWorld().spawnEntity(p.getLocation(), EntityType.DONKEY);
                break;
            case "mule":
                h = (Mule) p.getWorld().spawnEntity(p.getLocation(), EntityType.MULE);
                break;
            case "llama":
                h = (Llama) p.getWorld().spawnEntity(p.getLocation(), EntityType.LLAMA);
                break;
            case "skeleton":
                h = (SkeletonHorse) p.getWorld().spawnEntity(p.getLocation(), EntityType.SKELETON_HORSE);
                break;
            case "zombie":
                h = (ZombieHorse) p.getWorld().spawnEntity(p.getLocation(), EntityType.ZOMBIE_HORSE);
                break;
            default:
                p.sendMessage(lang.tag + usage);
                break;
            }
        } else{
            // No argument specified, assume normal horse
            h = (Horse) p.getWorld().spawnEntity(p.getLocation(), EntityType.HORSE);
        }
        
        if (h != null && h.isValid()) {
            h.setAdult();
            p.sendMessage(lang.tag + lang.get("hspawn.spawn") + " " + friendlyName(h));
        } else {
            p.sendMessage(lang.tag + lang.r + lang.get("hspawn.fail") + " " + args[0]);
        }
    }
}
