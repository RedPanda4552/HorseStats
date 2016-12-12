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
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.ChestedHorse;
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
            AbstractHorse h = null;
            if (p.isInsideVehicle()) {
                if (p.getVehicle() instanceof Horse) {
                    h = (AbstractHorse) p.getVehicle();
                }
            }
            this.run(p, h, args);
        } else {
            sender.sendMessage(lang.get("generic.console"));
        }
        return true;
    }

    public void run(Player p, AbstractHorse h, String[] args) {
        if (h != null) {
            if (hasPermission(p, h, InteractionType.USE)) {
                // We are making a huge assumption here that the composition of these
                // interfaces is no different other than the presence of a chest.
                // Below we will type cast h to Horse, regardless of the true type.
                if (h instanceof Horse || h instanceof ChestedHorse) {
                    if (args.length >= 2) {
                        if (args[0].equalsIgnoreCase("color")) {
                            if (args[1].equalsIgnoreCase("black")) {
                                ((Horse) h).setColor(Color.BLACK);
                            } else if (args[1].equalsIgnoreCase("brown")) {
                                ((Horse) h).setColor(Color.BROWN);
                            } else if (args[1].equalsIgnoreCase("chestnut")) {
                                ((Horse) h).setColor(Color.CHESTNUT);
                            } else if (args[1].equalsIgnoreCase("creamy")) {
                                ((Horse) h).setColor(Color.CREAMY);
                            } else if (args[1].equalsIgnoreCase("darkbrown")) {
                                ((Horse) h).setColor(Color.DARK_BROWN);
                            } else if (args[1].equalsIgnoreCase("gray")) {
                                ((Horse) h).setColor(Color.GRAY);
                            } else if (args[1].equalsIgnoreCase("white")) {
                                ((Horse) h).setColor(Color.WHITE);
                            } else {
                                p.sendMessage(lang.tag + lang.r + lang.get("setStyle.style-params"));
                                return;
                            }
                            p.sendMessage(lang.tag + lang.get("setStyle.color-change") + " " + YELLOW + ((Horse) h).getColor());
                        } else if (args[0].equalsIgnoreCase("style")) {
                            if (args[1].equalsIgnoreCase("blackdots")) {
                                ((Horse) h).setStyle(Style.BLACK_DOTS);
                            } else if (args[1].equalsIgnoreCase("none")) {
                                ((Horse) h).setStyle(Style.NONE);
                            } else if (args[1].equalsIgnoreCase("white")) {
                                ((Horse) h).setStyle(Style.WHITE);
                            } else if (args[1].equalsIgnoreCase("whitedots")) {
                                ((Horse) h).setStyle(Style.WHITE_DOTS);
                            } else if (args[1].equalsIgnoreCase("whitefield")) {
                                ((Horse) h).setStyle(Style.WHITEFIELD);
                            } else {
                                p.sendMessage(lang.tag + lang.r + lang.get("setStyle.style-params"));
                                return;
                            }
                            p.sendMessage(lang.tag + lang.get("setStyle.style-change") + " " + YELLOW + ((Horse) h).getStyle());
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
                } else {
                    p.sendMessage(lang.tag + lang.r + lang.get("setStyle.bad-type"));
                    return;
                }
            } else {
                p.sendMessage(lang.tag + lang.r + lang.get("generic.owner"));
            }
        } else {
            p.sendMessage(lang.tag + lang.r + lang.get("generic.riding"));
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
