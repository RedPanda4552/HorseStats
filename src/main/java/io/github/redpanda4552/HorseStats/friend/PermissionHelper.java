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
package io.github.redpanda4552.HorseStats.friend;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;

import io.github.redpanda4552.HorseStats.HorseStats;

public class PermissionHelper {
    
    private enum UpdateMode {
        UPDATE,
        REMOVE,
        NEW;
    }

    private HorseStats main;
    public HashMap<UUID, ArrayList<PermissionSet>> permissionMap;
    
    public PermissionHelper(HorseStats main) {
        this.main = main;
        permissionMap = new HashMap<UUID, ArrayList<PermissionSet>>();
    }
    
    /**
     * Check if a player has permission for a type of interaction.
     */
    public boolean playerHasPermission(UUID toCheck, UUID horseOwner, InteractionType interactionType) {
        for (PermissionSet ps : permissionMap.get(toCheck)) {
            if (ps.getPlayerId().equals(horseOwner)) {
                switch (interactionType) {
                case DAMAGE:
                    return ps.hasDamagePermission();
                case USE:
                    return ps.hasUsePermission();
                }
            }
        }
        return false;
    }
    
    /**
     * Load a player's permissions from SQL to memory.
     */
    public void loadPlayerPermissions(UUID toLoad) {
        ArrayList<PermissionSet> permissionSetArrayList = new ArrayList<PermissionSet>();
        PreparedStatement ps = null;
        
        try {
            ps = main.connection.prepareStatement("SELECT * FROM horsestats WHERE player_id = ?;");
            ps.setString(1, toLoad.toString());
            ResultSet res = ps.executeQuery();
            
            if (res.next() && res.getString("player_id") != null) {
                String permissionString = res.getString("permission_list");
                String[] permissionSets = permissionString.split(":");
                
                for (String permissionSet : permissionSets) {
                    if (!permissionSet.equals("")) {
                        String[] components = permissionSet.split("\\.");
                        
                        if (components.length == 3) {
                            permissionSetArrayList.add(new PermissionSet(UUID.fromString(components[0]), Integer.parseInt(components[1]) != 0, Integer.parseInt(components[2]) != 0));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            permissionMap.put(toLoad, permissionSetArrayList);
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Remove a player's permissions info from memory.
     * No SQL here; any SQL modification should be done with {@link #updatePlayerPermission(UUID, UUID, InteractionType, boolean)}.
     */
    public void unloadPlayerPermissions(UUID toUnload) {
        permissionMap.remove(toUnload);
    }
    
    /**
     * Create or update a player's permissions in memory and SQL.
     */
    public void updatePlayerPermission(UUID toUpdate, UUID horseOwner, InteractionType interactionType, boolean newPermission) {
        // In case the player is currently offline, we need to get their permissions into memory
        if (Bukkit.getPlayer(toUpdate) == null) {
            loadPlayerPermissions(toUpdate);
        }
        
        UpdateMode updateMode = UpdateMode.UPDATE;
        ArrayList<PermissionSet> permissionSetArrayList = permissionMap.get(toUpdate);
        
        if (permissionSetArrayList == null) {
            permissionSetArrayList = new ArrayList<PermissionSet>();
        }
        
        if (permissionSetArrayList.isEmpty()) {
            // If newPermission is false, and there are no existing entries to modify,
            // then continuing would end up creating an entry when we intend to remove
            // (even though there is none to remove in the first place)
            if (newPermission == true) {
                switch (interactionType) {
                case DAMAGE:
                    permissionSetArrayList.add(new PermissionSet(horseOwner, true, false));
                    break;
                case USE:
                    permissionSetArrayList.add(new PermissionSet(horseOwner, false, true));
                    break;
                }
                
                updateMode = UpdateMode.NEW;
            } else {
                // Since nothing is happening, execute finally block and exit.
                return;
            }
        } else {
            boolean permissionSetExists = false;
            for (PermissionSet permissionSet : permissionSetArrayList) {
                if (permissionSet.getPlayerId().equals(horseOwner)) {
                    switch (interactionType) {
                    case DAMAGE:
                        permissionSet.setDamagePermission(newPermission);
                        break;
                    case USE:
                        permissionSet.setUsePermission(newPermission);
                        break;
                    }
                    
                    // If a PermissionSet is false for both, there's no point since denial is default
                    // We can't remove right now because we'll set off a cmodification; reload handled later
                    if (isDeadPermissionSet(permissionSet)) {
                        updateMode = UpdateMode.REMOVE;
                    }
                    
                    permissionSetExists = true;
                }
            }
            
            if (!permissionSetExists) {
                switch (interactionType) {
                case DAMAGE:
                    permissionSetArrayList.add(new PermissionSet(horseOwner, true, false));
                    break;
                case USE:
                    permissionSetArrayList.add(new PermissionSet(horseOwner, false, true));
                    break;
                }
            }
        }
        
        StringBuilder permissionSetBuilder = new StringBuilder();
        
        for (PermissionSet permissionSet : permissionSetArrayList) {
            permissionSetBuilder.append(permissionSet.getPlayerId());
            permissionSetBuilder.append(".");
            
            if (permissionSet.hasDamagePermission()) {
                permissionSetBuilder.append("1");
            } else {
                permissionSetBuilder.append("0");
            }
            
            permissionSetBuilder.append(".");
            
            if (permissionSet.hasUsePermission()) {
                permissionSetBuilder.append("1");
            } else {
                permissionSetBuilder.append("0");
            }
            
            permissionSetBuilder.append(":");
        }
        
        String statement = null;
        PreparedStatement ps = null;
        
        try {
            switch (updateMode) {
            case UPDATE:
                statement = "UPDATE horsestats SET player_id = ?, permission_list = ? WHERE player_id = ?;";
                ps = main.connection.prepareStatement(statement.toString());
                ps.setString(1, toUpdate.toString());
                ps.setString(2, permissionSetBuilder.toString());
                ps.setString(3, toUpdate.toString());
                break;
            case NEW:
                statement = "INSERT INTO horsestats (player_id, permission_list) VALUES (?, ?);";
                ps = main.connection.prepareStatement(statement.toString());
                ps.setString(1, toUpdate.toString());
                ps.setString(2, permissionSetBuilder.toString());
                break;
            case REMOVE:
                statement = "DELETE FROM horsestats WHERE player_id = ?;";
                ps = main.connection.prepareStatement(statement.toString());
                ps.setString(1, toUpdate.toString());
                break;
            }
            
            ps.executeUpdate();
            
            // If removing, we need to reload from SQL here
            if (updateMode == UpdateMode.REMOVE) {
                unloadPlayerPermissions(toUpdate);
                loadPlayerPermissions(toUpdate);
            }
            
            // If the player is offline, unload their permissions now that we're done
            if (Bukkit.getPlayer(toUpdate) == null) {
                unloadPlayerPermissions(toUpdate);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Find all people that a player has given permissions to.
     * This method is going to be resource demanding! It will pull ALL data from SQL!
     * Do not run this outside of async!
     */
    public HashMap<UUID, PermissionSet> viewAllPermissions(UUID toCheck) {
        HashMap<UUID, PermissionSet> ret = new HashMap<UUID, PermissionSet>();
        PreparedStatement ps = null;
        
        try {
            ps = main.connection.prepareStatement("SELECT * FROM horsestats");
            ResultSet res = ps.executeQuery();
            
            while (res.next() && res.getString("player_id") != null) {
                String permissionString = res.getString("permission_list");
                String[] permissionSets = permissionString.split(":");
                
                for (String permissionSet : permissionSets) {
                    if (!permissionSet.equals("")) {
                        String[] components = permissionSet.split("\\.");
                        
                        if (components.length == 3) {
                            UUID uuid = UUID.fromString(components[0]);
                            
                            if (uuid.equals(toCheck)) {
                                ret.put(UUID.fromString(res.getString("player_id")), new PermissionSet(UUID.fromString(components[0]), Integer.parseInt(components[1]) != 0, Integer.parseInt(components[2]) != 0));
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Bukkit.getPlayer(toCheck).sendMessage(main.lang.tag + main.lang.get("hperm.viewall"));
        } finally {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return ret;
    }
    
    /**
     * Check if a {@link PermissionSet PermissionSet} is false for both damage and use.
     */
    private boolean isDeadPermissionSet(PermissionSet permissionSet) {
        return permissionSet.hasDamagePermission() == false && permissionSet.hasUsePermission() == false;
    }
}
