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

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinLeaveListener extends ListenerBase {

    public PlayerJoinLeaveListener(HorseStats main) {
        super (main);
    }

    @EventHandler
    public void playerJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        
        if (!main.anarchyMode) {
            main.permissionHelper.loadPlayerPermissions(p.getUniqueId());
        }
        
        if (p.hasPermission("HorseStats.pluginalerts")) {
            if (!main.checkConfiguration()) {
                p.sendMessage(lang.tag + lang.get("playerJoinLeaveListener.config"));
            }
            
            if (main.noSpeedMode) {
                p.sendMessage(lang.tag + lang.get("playerJoinLeaveListener.no-speed"));
            }
            
            if (main.updateAvailable) {
                p.sendMessage(lang.tag + lang.get("playerJoinLeaveListener.new-build") + " "  + lang.y + main.updateName + lang.g + " " + lang.get("playerJoinLeaveListener.at"));
                p.sendMessage(lang.g + "https://dev.bukkit.org/bukkit-plugins/horsestats");
            }
        }
    }
    
    @EventHandler
    public void playerLeave(PlayerQuitEvent event) {
        if (!main.anarchyMode) {
            Player p = event.getPlayer();
            main.permissionHelper.unloadPlayerPermissions(p.getUniqueId());
        }
    }
    
    @EventHandler // TODO Do we truly need this, or does PlayerQuitEvent monitor all disconnects...
    public void playerKicked(PlayerKickEvent event) {
        if (!main.anarchyMode) {
            Player p = event.getPlayer();
            main.permissionHelper.unloadPlayerPermissions(p.getUniqueId());
        }
    }
}
