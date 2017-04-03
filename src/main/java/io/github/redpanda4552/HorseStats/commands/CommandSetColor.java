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
 */package io.github.redpanda4552.HorseStats.commands;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.entity.Llama;

import io.github.redpanda4552.HorseStats.Main;
import io.github.redpanda4552.HorseStats.friend.InteractionType;

public class CommandSetColor extends AbstractCommand {

    private final String horseColorOptions = new StringBuilder(lang.g + "<")
            .append(lang.y + "black")
            .append(lang.g + " | ")
            .append(lang.y + "brown")
            .append(lang.g + " | ")
            .append(lang.y + "chestnut")
            .append(lang.g + " | ")
            .append(lang.y + "creamy")
            .append(lang.g + " | ")
            .append(lang.y + "darkbrown")
            .append(lang.g + " | ")
            .append(lang.y + "gray")
            .append(lang.g + " | ")
            .append(lang.y + "white")
            .append(lang.g + ">")
            .toString();
    private final String llamaColorOptions = new StringBuilder(lang.g + "<")
            .append(lang.y + "brown")
            .append(lang.g + " | ")
            .append(lang.y + "creamy")
            .append(lang.g + " | ")
            .append(lang.y + "gray")
            .append(lang.g + " | ")
            .append(lang.y + "white")
            .append(lang.g + ">")
            .toString();
    
    public CommandSetColor(Main main) {
        super(main);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            AbstractHorse h = null;
            
            if (p.isInsideVehicle()) {
                if (p.getVehicle() instanceof AbstractHorse) {
                    h = (AbstractHorse) p.getVehicle();
                }
            }
            
            run(p, h, args);
        } else {
            sender.sendMessage(lang.get("generic.console"));
        }
        return true;
    }
    
    public void run(Player p, AbstractHorse h, String[] args) {
        if (!hasPermission(p, h, InteractionType.USE)) {
            p.sendMessage(lang.tag + lang.r + lang.get("generic.owner"));
            return;
        }
        
        if (h instanceof Horse) {
            if (args.length >= 1) {
                if (args[0].equalsIgnoreCase("black")) {
                    ((Horse) h).setColor(Horse.Color.BLACK);
                } else if (args[0].equalsIgnoreCase("brown")) {
                    ((Horse) h).setColor(Horse.Color.BROWN);
                } else if (args[0].equalsIgnoreCase("chestnut")) {
                    ((Horse) h).setColor(Horse.Color.CHESTNUT);
                } else if (args[0].equalsIgnoreCase("creamy")) {
                    ((Horse) h).setColor(Horse.Color.CREAMY);
                } else if (args[0].equalsIgnoreCase("darkbrown")) {
                    ((Horse) h).setColor(Horse.Color.DARK_BROWN);
                } else if (args[0].equalsIgnoreCase("gray")) {
                    ((Horse) h).setColor(Horse.Color.GRAY);
                } else if (args[0].equalsIgnoreCase("white")) {
                    ((Horse) h).setColor(Horse.Color.WHITE);
                } else {
                    p.sendMessage(lang.tag + "/setcolor " + horseColorOptions);
                    return;
                }
            } else {
                p.sendMessage(lang.tag + "/setcolor " + horseColorOptions);
                return;
            }
            
            p.sendMessage(lang.tag + lang.get("setColor.color-change") + " " + lang.y + ((Horse) h).getColor());
        } else if (h instanceof Llama) {
            if (args.length >= 1) {
                if (args[0].equalsIgnoreCase("brown")) {
                    ((Llama) h).setColor(Llama.Color.BROWN);
                } else if (args[0].equalsIgnoreCase("creamy")) {
                    ((Llama) h).setColor(Llama.Color.CREAMY);
                } else if (args[0].equalsIgnoreCase("gray")) {
                    ((Llama) h).setColor(Llama.Color.GRAY);
                } else if (args[0].equalsIgnoreCase("white")) {
                    ((Llama) h).setColor(Llama.Color.WHITE);
                } else {
                    p.sendMessage(lang.tag + "/setcolor" + llamaColorOptions);
                    return;
                }
            } else {
                p.sendMessage(lang.tag + "/setcolor" + llamaColorOptions);
                return;
            }
            
            p.sendMessage(lang.tag + lang.get("setStyle.color-change") + " " + lang.y + ((Llama) h).getColor());
        } else {
            p.sendMessage(lang.tag + lang.r + lang.get("setColor.bad-type"));
            return;
        }
        
    }

}
