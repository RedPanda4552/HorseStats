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
package io.github.redpanda4552.HorseStats.listeners;

import io.github.redpanda4552.HorseStats.Main;
import io.github.redpanda4552.HorseStats.friend.InteractionType;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.SkeletonHorse;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class ListenerDamage extends AbstractListener {
    
    public ListenerDamage(Main main) {
        super (main);
    }
    
    /**
     * This event is called whenever an entity is hurt by another entity.
     * The first if statement quickly drops this if it is not a player and horse.
     */
    @EventHandler
    public void onHorseHit(EntityDamageByEntityEvent event) {
        //A horse is hit
        if (event.getEntity() instanceof AbstractHorse) {
            AbstractHorse h = (AbstractHorse) event.getEntity();
            
            //A player hit it
            if (event.getDamager() instanceof Player) {
                Player p = (Player) event.getDamager();
                
                //They held the stat display item
                if (p.getInventory().getItemInMainHand().getType() == main.statDisplayMaterial) {
                    event.setCancelled(true);
                    displayStats(p, h);
                //They held the teleport item
                } else if (p.getInventory().getItemInMainHand().getType() == main.teleportSelectorMaterial) {
                    event.setCancelled(true);
                    teleportToggle(p, h);
                //They held something else
                } else {
                    if (!hasPermission(p, h, InteractionType.DAMAGE)) {
                        event.setCancelled(true);
                        p.sendMessage(lang.tag + lang.r + lang.get("generic.attack"));
                    }
                }                
            } else if (event.getDamager() instanceof Projectile) { 
                Projectile proj = (Projectile) event.getDamager();
                
                if (proj.getShooter() instanceof Player) {
                    Player p = (Player) proj.getShooter();
                    
                    if (!hasPermission(p, h, InteractionType.DAMAGE)) {
                        event.setCancelled(true);
                        p.sendMessage(lang.tag + lang.r + lang.get("generic.attack"));
                    }
                }
            }
        }
    }
    
    /**
     * Display the stats of a horse to a player.
     */
    public void displayStats(Player p, AbstractHorse horse) {
        fixOwner(p, horse);
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.HALF_EVEN);
        
        // Max Health/Hearts
        double dHealthMax = horse.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
        String sHealthMax = df.format(dHealthMax);
        double dHeartMax = dHealthMax / 2;
        String sHeartMax = df.format(dHeartMax);
        
        // Health/Hearts
        double dHealth = horse.getHealth();
        String sHealth = df.format(dHealth);
        double dHeart = dHealth / 2;
        String sHeart = df.format(dHeart);
        
        // Jump Height
        String jump = df.format(5.162 * Math.pow(horse.getJumpStrength(), 1.7175));
        
        // Speed
        double dSpeed = horse.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue() * 42.18;
        String sSpeed = df.format(dSpeed);
        
        // Adult status
        boolean adult = horse.isAdult();
        
        // Ready to breed
        boolean breed = horse.canBreed();
        
        // Age
        float age = horse.getAge();
        
        // TODO LLama strength
        
        // Owner
        AnimalTamer tamer = horse.getOwner();
        
        //Horse name
        String name = friendlyName(horse);
        
        if (horse.getCustomName() != null) {
            name = horse.getCustomName() + lang.get("damageListener.posessive");
        }
        
        //Teleport status
        boolean tpStatus = main.teleportQueue.containsValue(horse);
        
        //Age status
        String ageTime = "";
        
        if (adult == false) {
            ageTime = " (" + lang.get("damageListener.adult-minutes") + df.format(age/-1200) + ")";
        }
        
        //Owner name
        String owner = lang.get("damageListener.none");
        
        if (tamer != null) {
            owner = tamer.getName();
        }
        
        //Message output
        p.sendMessage(lang.g + "========================");
        p.sendMessage(lang.g + name + " " + lang.get("damageListener.stats"));
        p.sendMessage(lang.g + "========================");
        p.sendMessage(lang.g + lang.get("damageListener.max-health") + " " + sHealthMax + " (" + sHeartMax + " " + lang.get("damageListener.hearts") + ")");
        p.sendMessage(lang.g + lang.get("damageListener.health") + " " + sHealth + " (" + sHeart + " " + lang.get("damageListener.hearts") + ")");
        p.sendMessage(lang.g + lang.get("damageListener.jump") + " " + jump);
        p.sendMessage(lang.g + lang.get("damageListener.speed") + " " + sSpeed);
        p.sendMessage(lang.g + lang.get("damageListener.breed") + " " + breed);
        p.sendMessage(lang.g + lang.get("damageListener.teleport-status") + " " + tpStatus);
        p.sendMessage(lang.g + lang.get("damageListener.is-adult") + " " + adult + ageTime);
        p.sendMessage(lang.g + lang.get("damageListener.owner") + " " + owner);
    }
    
    /**
     * Toggles teleport selection for a horse.
     */
    public void teleportToggle(Player p, AbstractHorse horse) {
        if (horse.getOwner() == null) {
            p.sendMessage(lang.tag + lang.r + lang.get("damageListener.teleport-untame"));
            return;
        }
        
        if (!hasPermission(p, horse, InteractionType.USE)) {
            p.sendMessage(lang.tag + lang.r + lang.get("generic.owner"));
            return;   
        }
        
        if (main.teleportQueue.containsKey(p.getUniqueId())) {
            main.teleportQueue.remove(p.getUniqueId());
            p.sendMessage(lang.tag + lang.get("damageListener.teleport-deselected"));
        } else {
            main.teleportQueue.put(p.getUniqueId(), horse);
            p.sendMessage(lang.tag + lang.get("damageListener.teleport-selected"));
        }
    }
    
    private void fixOwner(Player p, AbstractHorse horse) {
        if (horse.getOwner() == null && horse.isTamed() == true) {
            if (horse instanceof SkeletonHorse) {
                horse.setOwner(p);
            } else {
                horse.setTamed(false);
                p.sendMessage(lang.tag + lang.get("damageListener.owner-fix"));
            }
        }
    }
}


