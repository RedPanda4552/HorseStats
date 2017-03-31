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

import io.github.redpanda4552.HorseStats.Lang;
import io.github.redpanda4552.HorseStats.Main;
import io.github.redpanda4552.HorseStats.friend.InteractionType;

import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 * An abstract class for commands to extend.
 */
public abstract class AbstractCommand implements CommandExecutor {
    
    protected Main main;
    protected Lang lang;
    
    public AbstractCommand(Main main) {
        this.main = main;
        lang = main.lang;
    }
    
    /**
     * Used by SetOwner and Untame commands, as it is more absolute and ignores the permission system.
     */
    protected boolean isOwner(AbstractHorse horse, Player player) {
        if (horse.getOwner() == player) {
            return true;
        } else if (player.hasPermission("HorseStats.global-override")) {
            return true;
        }
        return false;
    }
    
    /**
     * Check if a player owns or has been given permission for a horse.
     */
    protected boolean hasPermission(Player player, AbstractHorse horse, InteractionType interactionType) {
        if (horse.getOwner() == null) {
            return true;
        } else if (main.anarchyMode) {
            return true;
        } else if (player.hasPermission("HorseStats.global-override")) {
            return true;
        } else if (horse.getOwner() == player) {
            return true;
        } else if (main.permissionHelper.playerHasPermission(player.getUniqueId(), horse.getOwner().getUniqueId(), interactionType)) {
            return true;
        }
        return false;
    }
    
    /**
     * Get the end user friendly name of this Entity
     * @param entity - The Entity to get the name of
     * @return The name of the specified Entity that won't scare end users
     * @throws IllegalArgumentException If entity is null
     */
    protected String friendlyName(Entity entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity argument for friendlyName(Entity) was null!");
        }
        return entity.getType().toString().toLowerCase().replace("_", " ");
    }
}
