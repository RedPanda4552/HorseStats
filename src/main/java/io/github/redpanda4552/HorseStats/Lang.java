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
package io.github.redpanda4552.HorseStats;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

public class Lang {
    
    private File file;
    private YamlConfiguration langConfig;
    
    // Faster ways to utilize chat colors
    public final String y = ChatColor.YELLOW + "", g = ChatColor.GREEN + "", r = ChatColor.RED + "", tag;
    
    public Lang(Main main) {
        file = new File("plugins/HorseStats/" + main.langFileName);
        
        if (main.getConfig().getBoolean("options.show-tag")) {
            tag = y + "[HorseStats] " + g;
        } else {
            tag = "" + g;
        }
        
        // Fail 1, file didn't exist on disk
        if (!file.exists()) {
            main.getLogger().info("Couldn't find " + main.langFileName + " in plugins/HorseStats. Attempting to unpack it from the jar...");
            main.saveResource(main.langFileName, false);
            file = new File("plugins/HorseStats/" + main.langFileName);
            
            // Fail 2, couldn't extract file config said, revert to default
            if (!file.exists()) {
                main.getLogger().warning("Could not extract the language pack specified in config! Attempting to unpack and set en-us.yml as the pack...");
                main.saveResource("en-us.yml", false);
                file = new File("plugins/HorseStats/" + main.langFileName);
                main.getConfig().set("language-pack", "en-us.yml");
                
                // Fail 3, couldn't extract default, jar is probably tampered with
                if (file.exists()) {
                    main.getLogger().warning("Could not unpack en-us.yml! Was it removed from inside the HorseStats jar?");
                    main.getLogger().warning("Messages will not display until a language pack is successfully loaded!");
                    // If we do this, it will at least send out "null" for strings,
                    // instead of throwing NPEs all over the place
                    langConfig = YamlConfiguration.loadConfiguration(file);
                    return;
                }
            }
        }
        
        langConfig = YamlConfiguration.loadConfiguration(file);
        
        if (!langConfig.getString("version").equals(main.langVersion)) {
            main.getLogger().warning("Your language pack is out of date! Some messages may not display properly!");
            
            if (main.langFileName.equals("en-us.yml")) {
                main.getLogger().info("Updating the default language pack. Changes will apply the next time the plugin is enabled.");
                main.saveResource("en-us.yml", true);
            }
        }
    }
    
    /**
     * Gets the String found at the given path, or null if no String exists at that path.
     */
    public final String get(String path) {
        return langConfig.getString(path);
    }
    
    /**
     * Takes in a command's usage message and applies yellow to the command and
     * arguments, and green to the special characters that denote and separate
     * arguments. Unused; maybe if we get it looking better it will see use.
     * @param str - The usage string to colorize.
     * @return The colored string.
     */
    public static final String colorize(String str) {
        StringBuilder sb = new StringBuilder();
        String[] components = str.split(" ");
        
        for (String component : components) {
            if (components[0].equalsIgnoreCase(component)) {
                sb.append(ChatColor.YELLOW + component);
            } else if (component.startsWith("<")) {
                sb.append(ChatColor.GREEN + "<");
                sb.append(ChatColor.YELLOW + component.replaceFirst("<", ""));
            } else if (component.equals("|")) {
                sb.append(ChatColor.GREEN + "|");
            } else if (component.endsWith(">")) {
                sb.append(ChatColor.YELLOW + component.replaceFirst(">", ""));
                sb.append(ChatColor.GREEN + ">");
            } else {
                sb.append(ChatColor.YELLOW + component);
            }
            
            sb.append(" ");
        }
        
        return sb.toString().trim();
    }
}
