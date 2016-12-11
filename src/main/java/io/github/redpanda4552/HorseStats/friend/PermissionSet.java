package io.github.redpanda4552.HorseStats.friend;

import java.util.UUID;

public class PermissionSet {

    private UUID playerId;
    private boolean damagePermission, usePermission;
    
    public PermissionSet(UUID playerId, boolean damagePermission, boolean usePermission) {
        this.playerId = playerId;
        this.damagePermission = damagePermission;
        this.usePermission = usePermission;
    }
    
    public UUID getPlayerId() {
        return playerId;
    }
    
    public boolean hasDamagePermission() {
        return damagePermission;
    }
    
    public boolean hasUsePermission() {
        return usePermission;
    }
    
    public void setDamagePermission(boolean damagePermission) {
        this.damagePermission = damagePermission;
    }
    
    public void setUsePermission(boolean usePermission) {
        this.usePermission = usePermission;
    }
}
