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

import static org.bukkit.ChatColor.*;

import io.github.redpanda4552.HorseStats.HorseStats;
import io.github.redpanda4552.HorseStats.friend.InteractionType;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.entity.Horse.Color;
import org.bukkit.entity.Horse.Style;

public class CommandSetStyle extends AbstractCommand {
    
    public CommandSetStyle(HorseStats main) {
        super(main);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            Horse h = null;
            
            if (p.isInsideVehicle()) {
                if (p.getVehicle() instanceof Horse) {
                    h = (Horse) p.getVehicle();
                }
            }
            
            run(p, h, args);
        } else {
            sender.sendMessage(lang.get("generic.console"));
        }
        return true;
    }

    public void run(Player p, Horse h, String[] args) {
        if (h == null) {
            p.sendMessage(lang.tag + lang.r + lang.get("setstyle.bad-type"));
            return;
        }
        
        if (!hasPermission(p, h, InteractionType.USE)) {
            p.sendMessage(lang.tag + lang.r + lang.get("generic.owner"));
            return;
        }
        
        // TODO Llama color
        if (args.length >= 2) {
            if (args[0].equalsIgnoreCase("color")) {
                if (args[1].equalsIgnoreCase("black")) {
                    h.setColor(Color.BLACK);
                } else if (args[1].equalsIgnoreCase("brown")) {
                    h.setColor(Color.BROWN);
                } else if (args[1].equalsIgnoreCase("chestnut")) {
                    h.setColor(Color.CHESTNUT);
                } else if (args[1].equalsIgnoreCase("creamy")) {
                    h.setColor(Color.CREAMY);
                } else if (args[1].equalsIgnoreCase("darkbrown")) {
                    h.setColor(Color.DARK_BROWN);
                } else if (args[1].equalsIgnoreCase("gray")) {
                    h.setColor(Color.GRAY);
                } else if (args[1].equalsIgnoreCase("white")) {
                    h.setColor(Color.WHITE);
                } else {
                    p.sendMessage(lang.tag + lang.r + lang.get("setStyle.style-params"));
                    return;
                }
                
                p.sendMessage(lang.tag + lang.get("setStyle.color-change") + " " + YELLOW + h.getColor());
            } else if (args[0].equalsIgnoreCase("style")) {
                if (args[1].equalsIgnoreCase("blackdots")) {
                    h.setStyle(Style.BLACK_DOTS);
                } else if (args[1].equalsIgnoreCase("none")) {
                    h.setStyle(Style.NONE);
                } else if (args[1].equalsIgnoreCase("white")) {
                    h.setStyle(Style.WHITE);
                } else if (args[1].equalsIgnoreCase("whitedots")) {
                    h.setStyle(Style.WHITE_DOTS);
                } else if (args[1].equalsIgnoreCase("whitefield")) {
                    h.setStyle(Style.WHITEFIELD);
                } else {
                    p.sendMessage(lang.tag + lang.r + lang.get("setStyle.style-params"));
                    return;
                }
                
                p.sendMessage(lang.tag + lang.get("setStyle.style-change") + " " + YELLOW + h.getStyle());
            } else {
                p.sendMessage(lang.tag + lang.get("setStyle.style-params"));
            }
        } else if (args.length == 1){
            if (args[0].equals("?")) {
                setstatHelp(p);
            } else {
                p.sendMessage(lang.tag + lang.get("setStyle.style-params"));
            }
        } else {
            p.sendMessage(lang.tag + lang.get("setStyle.style-params"));
        }
    }
    
    public void setstatHelp(Player p) {
        String[] styleHelp =
        { YELLOW + "/setstyle <color | style> <value>"
        , YELLOW + "Styles:"
        , GREEN  + "none, blackdots, whitedots, white, whitefield"
        , YELLOW + "Colors:"
        , GREEN  + "white, brown, chestnut, creamy, darkbrown, gray, black"
        };
        p.sendMessage(styleHelp);
    }
}
