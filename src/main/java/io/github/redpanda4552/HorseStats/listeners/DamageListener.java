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

import io.github.redpanda4552.HorseStats.HorseStats;
import io.github.redpanda4552.HorseStats.friend.InteractionType;

import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.Variant;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class DamageListener extends ListenerBase {
    
    public DamageListener(HorseStats main) {
        super (main);
    }
    
    /**
     * This event is called whenever an entity is hurt by another entity.
     * The first if statement quickly drops this if it is not a player and horse.
     */
    @EventHandler
    public void onHorseHit(EntityDamageByEntityEvent event) {
        //A horse is hit
        if (event.getEntity() instanceof Horse) {
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
        
        //Raw data
        double healthMax = horse.getMaxHealth();
        double heartMax = healthMax / 2;
        double health = horse.getHealth();
        double heart = health / 2;
        double jump = 5.5 * (Math.pow(horse.getJumpStrength(), 2));
        boolean adult = horse.isAdult();
        boolean breed = horse.canBreed();
        float age = horse.getAge();
        AnimalTamer tamer = horse.getOwner();
        
        //Horse name
        String name = lang.get("damageListener.horse");
        
        if (horse.getCustomName() != null) {
            name = horse.getCustomName() + lang.get("damageListener.posessive");
        }
        
        //Teleport status
        boolean tpStatus = false;
        
        if (main.teleportQueue.containsValue(AbstractHorse)) {
            tpStatus = true;
        }
        
        //Age status
        String ageTime = "";
        
        if (adult == false) {
            ageTime = " (" + lang.get("damageListener.adult-minutes") + age/-1200 + ")";
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
        p.sendMessage(lang.g + lang.get("damageListener.max-health") + " " + (float) healthMax + " (" + (int) heartMax + " " + lang.get("damageListener.hearts") + ")");
        p.sendMessage(lang.g + lang.get("damageListener.health") + " " + (float) health + " (" + (int) heart + " " + lang.get("damageListener.hearts") + ")");
        p.sendMessage(lang.g + lang.get("damageListener.jump") + " " + (float) jump);
        p.sendMessage(lang.g + lang.get("damageListener.speed") + " " + (float) getSpeed(AbstractHorse) * 43);
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
        } else if (this.hasPermission(p, horse, InteractionType.USE)) {
            if (main.teleportQueue.containsKey(p.getUniqueId())) {
                main.teleportQueue.remove(p.getUniqueId());
                p.sendMessage(lang.tag + lang.get("damageListener.teleport-deselected"));
            } else {
                main.teleportQueue.put(p.getUniqueId(), horse);
                p.sendMessage(lang.tag + lang.get("damageListener.teleport-selected"));
            }
        } else {
            p.sendMessage(lang.tag + lang.r + lang.get("generic.owner"));
        }
    }
    
    /**
     * The 'fragile' code used to retrieve horse speed. NBT stuff.
     * Needs package name updates when NBT code changes, or build number changes (e.g. CB 1.7.2-R1 to CB 1.7.2-R2)
     * Using full package names instead of imports so that I can just return before exceptions start flying, instead of making a whole new class.
     */
    public double getSpeed(AbstractHorse horse) {
        double speed = -1; //In case of bad Spigot version
        
        if (!main.noSpeedMode) {
            org.bukkit.craftbukkit.v1_11_R1.entity.CraftHorse cHorse = (org.bukkit.craftbukkit.v1_11_R1.entity.CraftHorse) horse;
            net.minecraft.server.v1_11_R1.NBTTagCompound compound = new net.minecraft.server.v1_11_R1.NBTTagCompound();
            cHorse.getHandle().b(compound);
            net.minecraft.server.v1_11_R1.NBTTagList list = (net.minecraft.server.v1_11_R1.NBTTagList) compound.get("Attributes");
            
            for(int i = 0; i < list.size() ; i++) {
                net.minecraft.server.v1_11_R1.NBTTagCompound base = list.get(i);
                
                if (base.getTypeId() == 10) {
                    net.minecraft.server.v1_11_R1.NBTTagCompound attrCompound = (net.minecraft.server.v1_11_R1.NBTTagCompound)base;
                    
                    if (base.toString().contains("generic.movementSpeed")) {
                        speed = attrCompound.getDouble("Base");
                    }
                }
            }
        }
        
        return speed;
    }
    
    private void fixOwner(Player p, AbstractHorse horse) {
        if (horse.getOwner() == null && horse.isTamed() == true) {
            if (horse.getVariant() == Variant.SKELETON_HORSE) {
                horse.setOwner(p);
            } else {
                horse.setTamed(false);
                p.sendMessage(lang.tag + lang.get("damageListener.owner-fix"));
            }
        }
    }
}


