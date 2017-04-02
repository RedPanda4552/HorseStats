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

import java.text.DecimalFormat;
import org.apache.commons.lang.WordUtils;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Llama;
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
    public void displayStats(Player player, AbstractHorse horse) {
        fixOwner(player, horse);
        DecimalFormat df = new DecimalFormat("#.##");
        String healthMax = df.format(horse.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
        String heartMax = df.format(horse.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() / 2);
        String health = df.format(horse.getHealth());
        String heart = df.format(horse.getHealth() / 2);
        String jump = df.format(5.162 * Math.pow(horse.getJumpStrength(), 1.7175));
        String speed = df.format(horse.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue() * 42.18);
        boolean adult = horse.isAdult();
        boolean breed = horse.canBreed();
        float age = horse.getAge();
        AnimalTamer tamer = horse.getOwner();
        String name = friendlyName(horse);
        
        if (horse.getCustomName() != null) {
            name = horse.getCustomName() + lang.get("damageListener.posessive");
        }
        
        boolean tpStatus = main.teleportQueue.containsValue(horse);
        String ageTime = "";
        
        if (adult == false) {
            ageTime = " (" + lang.get("damageListener.adult-minutes") + df.format(age/-1200) + ")";
        }
        
        String owner = lang.get("damageListener.none");
        
        if (tamer != null) {
            owner = tamer.getName();
        }
        
        int chestCapacity = 0;
        
        if (horse instanceof Llama) {
            chestCapacity = ((Llama) horse).getStrength() * 3;
        }
        
        player.sendMessage(lang.g + "========================");
        player.sendMessage(lang.g + name + " " + lang.get("damageListener.stats"));
        player.sendMessage(lang.g + "========================");
        player.sendMessage(lang.g + lang.get("damageListener.health") + " " + health + "/" + healthMax + " (" + heart + lang.r + "❤" + lang.g + "/" + heartMax + lang.r + "❤" + lang.g + ")");
        player.sendMessage(lang.g + lang.get("damageListener.jump") + " " + jump);
        player.sendMessage(lang.g + lang.get("damageListener.speed") + " " + speed);
        
        if (horse instanceof Llama) {
            player.sendMessage(lang.g + lang.get("damageListener.chest-capacity") +  " " + chestCapacity);
        }
        
        player.sendMessage(lang.g + lang.get("damageListener.breed") + " " + breed);
        player.sendMessage(lang.g + lang.get("damageListener.teleport-status") + " " + tpStatus);
        player.sendMessage(lang.g + lang.get("damageListener.is-adult") + " " + adult + ageTime);
        player.sendMessage(lang.g + lang.get("damageListener.owner") + " " + owner);
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
    
    protected String friendlyName(Entity entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity argument for friendlyName(Entity) was null!");
        }
        
        return WordUtils.capitalize(entity.getType().toString().toLowerCase().replace("_", " "));
    }
}


