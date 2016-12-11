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

import io.github.redpanda4552.HorseStats.HorseStats;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class CommandHorsestats extends AbstractCommand { 
    
    public CommandHorsestats(HorseStats main) {
        super(main);
    }
    
    private final String[] genHelp =
    { " "
    , lang.y + "== HorseStats v" + main.getDescription().getVersion() + " " + lang.get("horseStats.by") + " 'pandubz' =="
    , lang.y + lang.get("horseStats.choose")
    , lang.g + "/hs stats"
    , lang.g + "/hs teleport"
    , lang.g + "/hs permissions"
    , lang.g + lang.get("horseStats.command")
    , lang.y + "/help horsestats" + lang.g + "."
    };
    
    private final String[] statHelp = 
    { " "
    , lang.y + lang.get("horseStats.stat-title")
    , lang.g + lang.get("horseStats.instruction-1") + " " + lang.y + main.statDisplayMaterialFriendlyName + lang.g + ". "
    , lang.g + lang.get("horseStats.instruction-2")
    , lang.g + lang.get("horseStats.stat-1")
    , lang.g + lang.get("horseStats.stat-2")
    , lang.g + lang.get("horseStats.note-1")
    , lang.g + lang.get("horseStats.note-2")
    };
    
    private final String[] permissionHelp = 
    { " "
    , lang.y + lang.get("horseStats.permission-title")
    , lang.g + lang.get("horseStats.permission-1")
    , lang.g + lang.get("horseStats.permission-2") + lang.y + " /hperm" + lang.g + "."
    , lang.g + lang.get("horseStats.permission-3") + lang.y + " /untame " + lang.g + lang.get("horseStats.and") + lang.y + " /setowner " + lang.g + lang.get("horseStats.permission-4")
    };
    
    private final String[] teleportHelp = 
    { " "
    , lang.y + lang.get("horseStats.teleport-title")
    , lang.g + lang.get("horseStats.teleport-1") + " " + lang.y + main.teleportSelectorMaterialFriendlyName + lang.g + "."
    , lang.g + lang.get("horseStats.teleport-2")
    , lang.g + lang.get("horseStats.teleport-3") + lang.y + " /htp " + lang.g + lang.get("horseStats.teleport-4")
    , lang.g + lang.get("horseStats.teleport-5")
    };
    
    @Override
    public boolean onCommand(CommandSender sender, Command command,    String label, String[] args) {
        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("stats")) {
                sender.sendMessage(statHelp);
                return true;
            } else if (args[0].equalsIgnoreCase("teleport")) {
                sender.sendMessage(teleportHelp);
                return true;
            } else if (args[0].equalsIgnoreCase("permissions")) {
                sender.sendMessage(permissionHelp);
                
                if (main.anarchyMode) {
                    sender.sendMessage(lang.tag + lang.r + lang.get("horseStats.anarchy-mode"));
                }
                return true;
            }
        }
        sender.sendMessage(genHelp);
        return true;
    }
}
