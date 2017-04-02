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

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.redpanda4552.HorseStats.Main;
import io.github.redpanda4552.HorseStats.friend.InteractionType;
import io.github.redpanda4552.HorseStats.friend.ViewPermissionsTask;

public class CommandHPerm extends AbstractCommand {

    public CommandHPerm(Main main) {
        super(main);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if (main.anarchyMode) {
            sender.sendMessage(lang.tag + lang.r + lang.get("hperm.the") + lang.y + " hperm " + lang.r + lang.get("hperm.anarchy-mode"));
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage(lang.get("generic.console"));
            return true;
        }
        
        Player player = (Player) sender;
        
        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("view")) {
                new ViewPermissionsTask(main, player).runTaskAsynchronously(main);
                return true;
            }
        }
        
        if (args.length == 3) {
            UUID targetId = uuidProcess(player, args[1]);
            
            if (targetId == null) {
                return true;
            }
            
            InteractionType interactionType = null;
            
            if (args[2].equalsIgnoreCase("damage")) {
                interactionType = InteractionType.DAMAGE;
            } else if (args[2].equalsIgnoreCase("use")) {
                interactionType = InteractionType.USE;
            } else {
                player.sendMessage(
                        lang.tag + lang.get("hperm.perm-1") + " '" + lang.y + args[2] + lang.g + "'. " +
                        lang.get("hperm.perm-2") + " '" + lang.y + "destroy" + lang.g + "' "+ lang.get("hperm.and") +
                        " '" + lang.y + "use" + lang.g + "'."
                );
                return true;
            }
            
            boolean newPermission;
            
            if (args[0].equalsIgnoreCase("give")) {
                newPermission = true;
            } else if (args[0].equalsIgnoreCase("take")) {
                newPermission = false;
            } else {
                player.sendMessage(
                        lang.tag + lang.get("hperm.subcommand-1") + " " + lang.y + args[0] + lang.g + ". " + 
                        lang.get("hperm.subcommand-2") + " " + lang.y + "give" + lang.g + "," + " " + lang.y +
                        "take" + lang.g + " " + lang.get("hperm.and") + " " + lang.y + "view" + lang.g + "."
                );
                return true;
            }
            
            main.permissionHelper.updatePlayerPermission(targetId, player.getUniqueId(), interactionType, newPermission);
            player.sendMessage(
                    lang.tag + lang.get("hperm.success-1") + " " + lang.y + interactionType.toString() + lang.g +
                    " " + lang.get("hperm.success-2") + " " + lang.y + Bukkit.getOfflinePlayer(targetId).getName() + lang.g + "."
            );
            return true;
        }
        
        // Debug code, needs removed, but might be useful elsewhere so we'll leave it for now
//        for (UUID uuid : main.permissionHelper.permissionMap.keySet()) {
//            for (PermissionSet ps : main.permissionHelper.permissionMap.get(uuid)) {
//                player.sendMessage(uuid.toString() + " = " + ps.getPlayerId() + "." + ps.hasDamagePermission() + "." + ps.hasUsePermission());
//            }
//        }
        player.sendMessage(new String[] {
                lang.tag + lang.get("hperm.usage-1"),
                lang.tag + lang.get("hperm.usage-2")
        });
        return true; 
    }
    
    /**
     * Takes a String object which is either a UUID or player name and converts it to a UUID object.
     */
    private UUID uuidProcess(Player player, String str) {
        UUID targetId = null;
        
        try {
            targetId = UUID.fromString(str);
        } catch (IllegalArgumentException e) {
            @SuppressWarnings("deprecation")
            Player p = Bukkit.getPlayer(str);
            
            if (p != null) {
                targetId = p.getUniqueId();
            } else {
                player.sendMessage(lang.tag + lang.get("hperm.offline") + " '" + lang.y + str + lang.g + "'.");
                return null;
            }
        }
        
        OfflinePlayer target = Bukkit.getOfflinePlayer(targetId);
        
        if (target == null) {
            player.sendMessage(lang.tag + lang.get("hperm.uuid") + " '" + lang.y + targetId + lang.g + "'.");
            return null;
        }
        
        return targetId;
    }
}
