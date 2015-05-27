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
package io.github.redpanda4552.HorseStats.event;

import io.github.redpanda4552.HorseStats.HorseStatsMain;
import io.github.redpanda4552.HorseStats.translate.Translate;

import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PunchListener extends ListenerBase {
	
	
	/**
	 * @param main - HorseStatsMain
	 * @param tl - Translate
	 */
	public PunchListener(HorseStatsMain main, Translate tl) {
		super (main, tl);
	}

	/**
	 * This event is called whenever an entity is hurt by another entity.
	 * The first if statement quickly drops this if it is not a player and horse.
	 * @param event - The EntityDamageByEntityEvent that triggered this.
	 */
	@EventHandler
	public void onHorseHit(EntityDamageByEntityEvent event) {
		//A horse is hit
		if (event.getEntity() instanceof Horse) {
			Horse h = (Horse) event.getEntity();
			
			//A player hit it
			if (event.getDamager() instanceof Player) {
				Player p = (Player) event.getDamager();
				
				//They held the stat display item
				if (p.getItemInHand().getType() == main.pMat) {
					event.setCancelled(true);
					
					if (!canAccess(h, p) && main.configBoolean("block-stats")) {
						blockStats(p, h);
					} else {
						displayStats(p, h);
					}
				//They held the teleport item
				} else if (p.getItemInHand().getType() == main.tMat) {
					event.setCancelled(true);
					teleportToggle(p, h);
				//They held something else
				} else {
					if (main.configBoolean("anti-grief")) {
						if (!this.canAccess(h, p)) {
							event.setCancelled(true);
							p.sendMessage(tl.e + tl.generic("attack"));
						}
					}
				}				
			}
			//An arrow hit it
			else if (event.getDamager() instanceof Arrow) { 
				if (main.configBoolean("anti-grief")) {
					Arrow a = (Arrow) event.getDamager();
					
					if (a.getShooter() instanceof Player) {
						Player p = (Player) a.getShooter();
						
						if (!this.canAccess(h, p)) {
							event.setCancelled(true);
							p.sendMessage(tl.e + tl.generic("attack"));
						}
					}
				}
			}
		}
	}
	
	public void blockStats(Player p, Horse horse) {
		//In case there are any horses marked as tame but missing an owner
		if (horse.getOwner() == null && horse.isTamed() == true) {
			horse.setTamed(false);
			p.sendMessage(tl.n + tl.event("owner-fix"));
		}
		
		AnimalTamer tamer = horse.getOwner();
		
		//Owner name
		String owner = tl.event("none");
		
		if (tamer != null) {
			owner = tamer.getName();
		}
		
		p.sendMessage(tl.e + tl.event("block-stats-1") + owner + tl.event("block-stats-2"));
	}
	
	/**
	 * Display the stats of a horse to a player.
	 * @param p - The player to send stats to.
	 * @param horse - The horse who's stats are to be fetched.
	 */
	public void displayStats(Player p, Horse horse) {
		//In case there are any horses marked as tame but missing an owner
		if (horse.getOwner() == null && horse.isTamed() == true) {
			horse.setTamed(false);
			p.sendMessage(tl.n + tl.event("owner-fix"));
		}
		
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
		String name = tl.event("horse");
		
		if (horse.getCustomName() != null) {
			name = horse.getCustomName() + tl.event("posessive");
		}
		
		//Teleport status
		boolean tpStatus = false;
		
		if (main.teleportQueue.containsValue(horse)) {
			tpStatus = true;
		}
		
		//Age status
		String ageTime = "";
		
		if (adult == false) {
			ageTime = " (" + tl.event("adult-minutes") + age/-1200 + ")";
		}
		
		//Owner name
		String owner = tl.event("none");
		
		if (tamer != null) {
			owner = tamer.getName();
		}
		
		//Message output
		p.sendMessage(tl.s + "========================");
		p.sendMessage(tl.s + name + " " + tl.event("stats"));
		p.sendMessage(tl.s + "========================");
		p.sendMessage(tl.s + tl.event("max-health") + " " + (float) healthMax + " (" + (int) heartMax + " " + tl.event("hearts") + ")");
		p.sendMessage(tl.s + tl.event("health") + " " + (float) health + " (" + (int) heart + " " + tl.event("hearts") + ")");
		p.sendMessage(tl.s + tl.event("jump") + " " + (float) jump);
		p.sendMessage(tl.s + tl.event("speed") + " " + (float) getSpeed(horse) * 43);
		p.sendMessage(tl.s + tl.event("breed") + " " + breed);
		p.sendMessage(tl.s + tl.event("teleport-status") + " " + tpStatus);
		p.sendMessage(tl.s + tl.event("is-adult") + " " + adult + ageTime);
		p.sendMessage(tl.s + tl.event("owner") + " " + owner);
	}
	
	/**
	 * Toggles teleport selection for a horse.
	 * @param p - The player who attempted the toggle.
	 * @param horse - The horse who teleport is being toggled for.
	 */
	public void teleportToggle(Player p, Horse horse) {
		if (this.canAccess(horse, p)) {
			if (main.teleportQueue.containsKey(p.getUniqueId())) {
				main.teleportQueue.remove(p.getUniqueId());
				p.sendMessage(tl.n + tl.event("teleport-deselected"));
			} else {
				main.teleportQueue.put(p.getUniqueId(), horse);
				p.sendMessage(tl.n + tl.event("teleport-selected"));
			}
		} else if (horse.getOwner() == null) { 
			p.sendMessage(tl.e + tl.event("teleport-untame"));
		} else {
			p.sendMessage(tl.e + tl.generic("owner"));
		}
	}
	
	/**
	 * The 'fragile' code used to retrieve horse speed. NBT stuff.
	 * Needs namespace updates when NBT code changes, or build number changes (e.g. CB 1.7.2-R0.1 to CB 1.7.2-R0.2)
	 * Using full namespaces so that I can just return before exceptions start flying, instead of making a whole new class.
	 * @param horse - The horse that was hit with a lead.
	 * @return Double that represents horse speed.
	 */
	public double getSpeed(Horse horse) {
		double speed = -1;
		
		if (main.noSpeedMode) {
			return speed;
		}
		
		org.bukkit.craftbukkit.v1_8_R2.entity.CraftHorse cHorse = (org.bukkit.craftbukkit.v1_8_R2.entity.CraftHorse) horse;
		net.minecraft.server.v1_8_R2.NBTTagCompound compound = new net.minecraft.server.v1_8_R2.NBTTagCompound();
		cHorse.getHandle().b(compound);
		net.minecraft.server.v1_8_R2.NBTTagList list = (net.minecraft.server.v1_8_R2.NBTTagList) compound.get("Attributes");
		
		for(int i = 0; i < list.size() ; i++) {
			net.minecraft.server.v1_8_R2.NBTTagCompound base = list.get(i);
			
			if (base.getTypeId() == 10) {
				net.minecraft.server.v1_8_R2.NBTTagCompound attrCompound = (net.minecraft.server.v1_8_R2.NBTTagCompound)base;
				
				if (base.toString().contains("generic.movementSpeed")) {
					speed = attrCompound.getDouble("Base");
				}
			}
		}
		return speed;
	}		
}


