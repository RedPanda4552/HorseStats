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

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.redpanda4552.HorseStats.HorseStatsCommand;
import io.github.redpanda4552.HorseStats.HorseStatsMain;
import io.github.redpanda4552.HorseStats.translate.Translate;

public class HFriend extends HorseStatsCommand {

    public HFriend(HorseStatsMain main, Translate tl) {
        super(main, tl);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(tl.generic("console"));
            return true;
        }
        
        Player player = (Player) sender;
        
        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("view")) {
                if (main.friendHelper.index.containsKey(player.getUniqueId())) {
                    if (main.friendHelper.index.get(player.getUniqueId()).isEmpty()) {
                        sender.sendMessage(tl.n + tl.hFriend("list-empty"));
                        return true;
                    } else {
                        player.sendMessage(tl.n + tl.hFriend("friend-list"));
                    }
                    
                    for (UUID friend : main.friendHelper.index.get(player.getUniqueId())) {
                        if (Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(friend))) {
                            player.sendMessage(tl.s + Bukkit.getPlayer(friend).getName());
                        } else {
                            player.sendMessage(tl.s + friend.toString());
                        }
                    }
                } else {
                    sender.sendMessage(tl.n + tl.hFriend("no-friend-list"));
                }
                
                return true;
            } 
        }
        
        if (args.length >= 2) {
            String id = "";
            
            if (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("remove")) {
                if (Bukkit.getPlayer(args[1]) != null) { //Is our input type a name?
                    id = Bukkit.getPlayer(args[1]).getUniqueId().toString();
                } else { //Or a UUID?
                    try {
                        if (Bukkit.getPlayer(UUID.fromString(args[1])) != null) { //If they're online
                            id = args[1];
                        } else if (main.friendHelper.readFriendListFromIndex(player.getUniqueId()).contains(UUID.fromString(args[1]))) { //If they're offline, but in the player's friend list
                            id = args[1];
                        } else { //Or in this case we don't have any idea who the player is talking about.
                            player.sendMessage(tl.e + tl.generic("player-not-found"));
                            return true;
                        }
                    } catch (IllegalArgumentException e) {
                        player.sendMessage(tl.e + tl.generic("player-not-found"));
                        return true;
                    }
                }
            } else {
                player.sendMessage(tl.n + tl.hFriend("usage"));
                return true;
            }
            
            if (!main.friendHelper.index.containsKey(player.getUniqueId())) { //Friend list does not exist, lets make one
                main.friendHelper.addEmptyFriendList(player.getUniqueId());
                main.friendHelper.writeToFile(player.getUniqueId());
            }
            
            ArrayList<UUID> friends = main.friendHelper.index.get(player.getUniqueId());
            
            if (args[0].equalsIgnoreCase("add")) {
                if (!friends.contains(UUID.fromString(id))) {
                    main.friendHelper.addFriend(player.getUniqueId(), UUID.fromString(id));
                    main.friendHelper.writeToFile(player.getUniqueId());
                    sender.sendMessage(tl.n + tl.hFriend("friend") + " " + ChatColor.YELLOW + id + ChatColor.GREEN + " " + tl.hFriend("friend-add"));
                } else {
                    player.sendMessage(tl.e + tl.hFriend("already-on-list"));
                }
            } else if (args[0].equalsIgnoreCase("remove")) {
                try {
                    if (friends.contains(UUID.fromString(id))) {
                        main.friendHelper.removeFriend(player.getUniqueId(), UUID.fromString(id));
                        main.friendHelper.writeToFile(player.getUniqueId());
                        player.sendMessage(tl.n + tl.hFriend("friend") + " " + ChatColor.YELLOW + id + ChatColor.GREEN + " " + tl.hFriend("friend-remove"));
                    } else {
                        player.sendMessage(tl.e + tl.hFriend("not-on-list"));
                    }
                } catch (IllegalArgumentException e) {
                    player.sendMessage(tl.e + tl.hFriend("bad-uuid"));
                }
            }
            return true;
        }
        player.sendMessage(tl.n + tl.hFriend("usage"));
        return true; 
    }
}
