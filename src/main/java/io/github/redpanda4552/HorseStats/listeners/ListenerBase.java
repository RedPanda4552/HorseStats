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
import io.github.redpanda4552.HorseStats.lang.Lang;

import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public abstract class ListenerBase implements Listener {
    
    protected HorseStats main;
    protected Lang lang;
    
    public ListenerBase(HorseStats main) {
        this.main = main;
        lang = main.lang;
    }
    
    /**
     * Check if a player owns or has been given permission for a horse.
     */
    protected boolean hasPermission(Player player, Horse horse, InteractionType interactionType) {
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
}
