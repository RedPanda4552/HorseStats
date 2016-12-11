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

import io.github.redpanda4552.HorseStats.commands.*;
import io.github.redpanda4552.HorseStats.friend.PermissionHelper;
import io.github.redpanda4552.HorseStats.lang.Lang;
import io.github.redpanda4552.HorseStats.listeners.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Logger;

import net.gravitydevelopment.updater.Updater;
import net.gravitydevelopment.updater.Updater.UpdateResult;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.huskehhh.mysql.MySQL;
import com.huskehhh.mysql.SQLite;

public class HorseStats extends JavaPlugin {
    
    // Convenience field for logger in main class
    protected Logger log;
    
    // Universally accessed fields (Not necessarily pulled from config)
    public boolean noSpeedMode;
    public HashMap<UUID, Horse> teleportQueue;
    
    // Configuration fields
    public Material statDisplayMaterial, teleportSelectorMaterial;
    public String statDisplayMaterialFriendlyName, teleportSelectorMaterialFriendlyName;
    public String langFileName;
    public final boolean anarchyMode;
    
    // Version information
    public final String langVersion = "4.0", configVersion = "4.0";
    public boolean outOfDateConfig = false;
    
    // Support Classes
    private final HorseStats main = this; //Only because of the update checker.
    public Lang lang;
    public PermissionHelper permissionHelper;
    
    // SQL Fields
    private String SQLHostName, SQLPort, SQLDatabase, SQLUsername, SQLPassword, SQLiteDatabase;
    private MySQL mySQL = null;
    private SQLite sqlite = null;
    public Connection connection = null;
    
    // Updater Fields
    public boolean updateAvailable = false;
    public String updateName;
    
    // Constructor - Final fields defined in here
    public HorseStats() {
        anarchyMode = getConfig().getBoolean("options.anarchy-mode");
    }
    
    // Plugin Disable
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
        
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            log.warning("Something didn't let the SQL connection close! If you are reloading your server, things may get nasty if the GC doesn't completely clean things up...");
        }
        
        // This may be silly and perfectly fine at the hands of the GC.
        // But since the Javadocs on the GC's behavior are so cryptic,
        // and it's the main user of the SQL connection, we'll null it out.
        permissionHelper = null;
    }
    
    // Plugin Enable
    public void onEnable() {
        log = getLogger();
        saveDefaultConfig();
        langFileName = getConfig().getString("options.language-pack");
        lang = new Lang(this);
        
        if (anarchyMode) {
            
        } else {
            permissionHelper = new PermissionHelper(this);
            
            // Start SQL Setup
            if (getConfig().getString("sql.driver").equalsIgnoreCase("sqlite")) {
                SQLiteDatabase = getConfig().getString("sql.sqlite-database-path");
                sqlite = new SQLite(SQLiteDatabase);
                
                if (SQLiteDatabase != null && SQLiteDatabase.length() >= 4 && SQLiteDatabase.endsWith(".db")) {
                    try {
                        connection = sqlite.openConnection();
                        PreparedStatement ps = connection.prepareStatement("CREATE TABLE IF NOT EXISTS horsestats (player_id char(36), permission_list text);");
                        ps.executeUpdate();
                    } catch (ClassNotFoundException | SQLException e) {
                        e.printStackTrace();
                        log.warning("SQL database connection failed!");
                        log.warning("HorseStats will assume all non owners are denied access until this is resolved.");
                        return;
                    }
                } else {
                    log.warning("The SQL section of the HorseStats configuration is not properly filled out!");
                    log.warning("To use SQLite, make sure 'sqlite-database-path' is filled out, and ends with '.db'.");
                    log.warning("HorseStats will assume all non owners are denied access until this is resolved.");
                    return;
                }
            } else if (getConfig().getString("sql.driver").equalsIgnoreCase("mysql")) {
                SQLDatabase = getConfig().getString("sql.database");
                SQLHostName = getConfig().getString("sql.mysql-host-name");
                SQLPort = getConfig().getString("sql.mysql-port");
                SQLUsername = getConfig().getString("sql.mysql-username");
                SQLPassword = getConfig().getString("sql.mysql-password");
                
                mySQL = new MySQL(SQLHostName, SQLPort, SQLDatabase, SQLUsername, SQLPassword);
                
                if (SQLHostName != null && SQLPort != null && SQLDatabase != null && SQLUsername != null && SQLPassword != null) {
                    try {
                        connection = mySQL.openConnection();
                        PreparedStatement ps = connection.prepareStatement("CREATE TABLE IF NOT EXISTS horsestats (player_id char(36), permission_list text);");
                        ps.executeUpdate();
                    } catch (ClassNotFoundException | SQLException e) {
                        e.printStackTrace();
                        log.warning("SQL database connection failed!");
                        log.warning("HorseStats will assume all non owners are denied access until this is resolved.");
                        return;
                    }
                } else {
                    log.warning("The SQL section of the HorseStats configuration is not properly filled out!");
                    log.warning("To use MySQL, make sure 'database', 'mysql-host-name', 'mysql-port', 'mysql-username', and 'mysql-password' are all filled out.");
                    log.warning("HorseStats will assume all non owners are denied access until this is resolved.");
                    return;
                }
            } else {
                log.warning("The config field 'sql.driver' is invalid! Set it to either 'sqlite' or 'mysql'.");
                log.warning("HorseStats will assume all non owners are denied access until this is resolved.");
                return;
            }
            
            // End SQL Setup
        }
        
        noSpeedMode = testNoSpeedMode();
        
        statDisplayMaterial = Material.getMaterial(getConfig().getString("options.stat-item"));
        statDisplayMaterialFriendlyName = getConfig().getString("options.stat-item-name");
        
        if (statDisplayMaterial == null) {
            log.warning("Bad stat display Material type in config! Defaulting to Lead.");
            statDisplayMaterial = Material.LEASH;
            statDisplayMaterialFriendlyName = "Lead";
        }
        
        teleportSelectorMaterial = Material.getMaterial(getConfig().getString("options.teleport-item"));
        teleportSelectorMaterialFriendlyName = getConfig().getString("options.teleport-item-name");
        
        if (teleportSelectorMaterial == null) {
            log.warning("Bad teleport select Material type in config! Defaulting to Ender Pearl.");
            teleportSelectorMaterial = Material.ENDER_PEARL;
            teleportSelectorMaterialFriendlyName = "Ender Pearl";
        }
        
        teleportQueue = new HashMap<UUID, Horse>();
        
        getServer().getPluginManager().registerEvents(new DamageListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinLeaveListener(this), this);        
        getServer().getPluginManager().registerEvents(new InteractListener(this), this);
        
        registerCommands();
        checkConfiguration();
        
        // This is a call to the PermissionHelper, which does not exist in anarchy mode
        if (!anarchyMode) {
            checkOnlinePlayers();
        }

        runUpdateChecker();
    }
    
    // Command Registers
    private void registerCommands() {
        getCommand("horsestats").setExecutor(new CommandHorsestats(this));
        getCommand("htp").setExecutor(new CommandHtp(this));
        getCommand("setowner").setExecutor(new CommandSetOwner(this));
        getCommand("untame").setExecutor(new CommandUntame(this));
        getCommand("delchest").setExecutor(new CommandDelchest(this));
        getCommand("delname").setExecutor(new CommandDelname(this));
        getCommand("slayhorse").setExecutor(new CommandSlayhorse(this));
        getCommand("hperm").setExecutor(new CommandHPerm(this));
        getCommand("hspawn").setExecutor(new CommandHspawn(this));
        getCommand("setstyle").setExecutor(new CommandSetStyle(this));
        getCommand("setstat").setExecutor(new CommandSetStat(this));
        getCommand("tame").setExecutor(new CommandTame(this));
    }
    
    /**
     * Checks if server build matches HorseStats. Speed stat requires NMS.
     */
    public boolean testNoSpeedMode() {
        try {
            Class.forName("org.bukkit.craftbukkit.v1_10_R1.entity.CraftHorse");
            return false;
        } catch (ClassNotFoundException e) {
            return true;
        }
    }
    
    /**
     * If the server reloaded, we will need to run through online players and re-add their info to memory.
     */
    public void checkOnlinePlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            permissionHelper.loadPlayerPermissions(player.getUniqueId());
        }
    }
    
    /**
     * Check if config is outdated. Warn if so.
     * @return True if config is fine. False if outdated.
     */
    public boolean checkConfiguration() {
        if (!getConfig().getString("config-version").equals(this.configVersion)) {
            outOfDateConfig = true;            
            log.warning("It appears your HorseStats configuration file is out of date. To update it, rename your current config.");
            log.warning("The next time HorseStats starts, a new config will generate and you can move your settings.");
            return false;
        }
        return true;
    }
    
    /**
     * Update notifier; only notifies, does not download.
     */
    private void runUpdateChecker() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (getConfig().getBoolean("options.update-checks") == true) {
                    Updater updater = new Updater(main, 62378, main.getFile(), Updater.UpdateType.NO_DOWNLOAD, false);
                    
                    if (updater.getResult() == UpdateResult.UPDATE_AVAILABLE) {
                        updateName = updater.getLatestName();
                        updateAvailable = true;
                        log.info("A new build of HorseStats is available!");
                        log.info("You can find " + updater.getLatestName() + " at: https://dev.bukkit.org/bukkit-plugins/horsestats");
                    }
                }    
            }
        }.runTaskAsynchronously(main);
            
    }
}
