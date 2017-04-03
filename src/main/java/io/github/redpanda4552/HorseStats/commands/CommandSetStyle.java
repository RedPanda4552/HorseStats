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

import io.github.redpanda4552.HorseStats.Main;
import io.github.redpanda4552.HorseStats.friend.InteractionType;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.entity.Horse.Style;

public class CommandSetStyle extends AbstractCommand {
    
    private final String styleOptions = new StringBuilder(lang.g + "<")
            .append(lang.y + "none")
            .append(lang.g + " | ")
            .append(lang.y + "blackdots")
            .append(lang.g + " | ")
            .append(lang.y + "white")
            .append(lang.g + " | ")
            .append(lang.y + "whitedots")
            .append(lang.g + " | ")
            .append(lang.y + "whitefield")
            .append(lang.g + ">")
            .toString();
    
    public CommandSetStyle(Main main) {
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
            p.sendMessage(lang.tag + lang.r + "/setstyle " + lang.get("setStyle.bad-type"));
            return;
        }
        
        if (!hasPermission(p, h, InteractionType.USE)) {
            p.sendMessage(lang.tag + lang.r + lang.get("generic.owner"));
            return;
        }
        
        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("none")) {
                h.setStyle(Style.NONE);
            } else if (args[0].equalsIgnoreCase("blackdots")) {
                h.setStyle(Style.BLACK_DOTS);
            } else if (args[0].equalsIgnoreCase("white")) {
                h.setStyle(Style.WHITE);
            } else if (args[0].equalsIgnoreCase("whitedots")) {
                h.setStyle(Style.WHITE_DOTS);
            } else if (args[0].equalsIgnoreCase("whitefield")) {
                h.setStyle(Style.WHITEFIELD);
            } else {
                p.sendMessage(lang.tag + "/setstyle " + styleOptions);
                return;
            }
            
            p.sendMessage(lang.tag + lang.get("setStyle.style-change") + " " + lang.y + h.getStyle());
        } else {
            p.sendMessage(lang.tag + "/setstyle " + styleOptions);
        }
    }
}
