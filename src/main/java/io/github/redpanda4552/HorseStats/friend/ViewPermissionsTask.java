package io.github.redpanda4552.HorseStats.friend;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import io.github.redpanda4552.HorseStats.Main;

public class ViewPermissionsTask extends BukkitRunnable {

    private final Main main;
    private final Player player;
    
    public ViewPermissionsTask(Main main, Player player) {
        this.main = main;
        this.player = player;
    }
    
    @Override
    public void run() {
        player.sendMessage(main.lang.tag + main.lang.get("hperm.view"));
        HashMap<UUID, PermissionSet> nameMap = main.permissionHelper.viewAllPermissions(player.getUniqueId());
        
        for (UUID uuid : nameMap.keySet()) {
            player.sendMessage(new String[] {
                    main.lang.g + Bukkit.getOfflinePlayer(uuid).getName() + " (UUID = " + uuid.toString() + ")",
                    main.lang.g + "- Damage = " + nameMap.get(uuid).hasDamagePermission(),
                    main.lang.g + "- Use = " + nameMap.get(uuid).hasUsePermission()
            });
        }
        
        player.sendMessage(main.lang.tag + "Done!");
    }

}
