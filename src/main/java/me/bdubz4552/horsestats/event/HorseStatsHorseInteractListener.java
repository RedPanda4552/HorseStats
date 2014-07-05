package me.bdubz4552.horsestats.event;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import me.bdubz4552.horsestats.HorseStatsMain;
import me.bdubz4552.horsestats.Message;

public class HorseStatsHorseInteractListener extends HorseStatsListenerBase implements Listener {

	private HashMap<Player, Horse> caravanList = new HashMap<Player, Horse>();
	
	public HorseStatsHorseInteractListener(HorseStatsMain horseStatsMain) {
		super(horseStatsMain);
	}
	
	@EventHandler(ignoreCancelled=true)
	/**
	 * Event listener for nonOwnerHorseInteraction and saddleLock config settings.
	 * Conditionals are separated to prevent any contingencies.
	 * @param event - The PlayerInteractEntityEvent that triggered this.
	 */
	public void onPlayerInteractHorse(PlayerInteractEntityEvent event) {
		
		//nonOwnerHorseInteraction
		if (main.configBoolean("nonOwnerHorseInteraction") == false) {
			Player p = event.getPlayer();
			//Kill this method before it cancels.
			if (main.hasGlobalOverride(p)) {
				return;
			}
			
			if (event.getRightClicked() instanceof Horse) {
				Horse h = (Horse) event.getRightClicked();
				if (h.getOwner() != p && h.getOwner() != null) {
					event.setCancelled(true);
					Message.OWNER.send(p);
				}
			}
		}	

		//saddleLock
		if (main.configBoolean("saddleLock") == true) {
			Player p = event.getPlayer();	
			//Kill this method before it cancels.
			if (main.hasGlobalOverride(p)) {
				return;
			}
			
			if (event.getRightClicked() instanceof Horse) {
				Horse h = (Horse) event.getRightClicked();
				if (h.getOwner() != p && h.getOwner() != null) {
					if (h.getInventory().getSaddle() != null) {
						event.setCancelled(true);
						Message.OWNER.send(p);
					}
				}
			}
		}
		
		//caravans. Still not ready for release.
		Player player = event.getPlayer();
		if (event.getRightClicked() instanceof Horse) {
			
			if (player.getItemInHand().getType() == Material.STICK) {
				PlayerInventory inventory = player.getInventory();
				
				if (inventory.contains(Material.LEASH)) {
					int i = inventory.first(Material.LEASH);
					ItemStack stack = inventory.getItem(i);
					stack.setAmount(stack.getAmount() - 1);
					
				}
			}
			
			event.setCancelled(true);
			Horse horse = (Horse) event.getRightClicked();
			if (!caravanList.containsKey(player)) {
				caravanList.put(player, horse);
			} else {
				caravanList.get(player).setLeashHolder(horse);
				caravanList.remove(player);
			}
		}
	}
}
