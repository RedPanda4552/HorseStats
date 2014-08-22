package io.github.redpanda4552.HorseStats.event;

import io.github.redpanda4552.HorseStats.HorseStatsMain;
import io.github.redpanda4552.HorseStats.utilities.Translate;

import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;

//Comments toggled on Caravan components.
public class HorseStatsHorseInteractListener extends HorseStatsListenerBase {

//	private HashMap<Player, Horse> caravanList = new HashMap<Player, Horse>();
	
	public HorseStatsHorseInteractListener(HorseStatsMain horseStatsMain) {
		super(horseStatsMain);
	}
	
	@EventHandler(ignoreCancelled=true)
	/**
	 * Event listener for nonOwnerHorseInteraction and saddleLock config settings.
	 * @param event - The PlayerInteractEntityEvent that triggered this.
	 */
	public void onPlayerInteractHorse(PlayerInteractEntityEvent event) {
		Player p = event.getPlayer();
		//nonOwnerHorseInteraction
		if (main.configBoolean("nonOwnerHorseInteraction") == false) {
			if (main.override(p)) {
				return;
			}
			
			if (event.getRightClicked() instanceof Horse) {
				Horse h = (Horse) event.getRightClicked();
				if (h.getOwner() != p && h.getOwner() != null) {
					event.setCancelled(true);
					this.sendError(p, Translate.generic, "owner");
				}
			}
		}	

		//saddleLock
		if (main.configBoolean("saddleLock") == true) {
			if (main.override(p)) {
				return;
			}
			
			if (event.getRightClicked() instanceof Horse) {
				Horse h = (Horse) event.getRightClicked();
				if (h.getOwner() != p && h.getOwner() != null) {
					if (h.getInventory().getSaddle() != null) {
						event.setCancelled(true);
						this.sendError(p, Translate.generic, "owner");
					}
				}
			}
		}
		
		//caravans. Still not ready for release. Comment toggled for now.
		
//		if (event.getRightClicked() instanceof Horse) {
//			
//			if (p.getItemInHand().getType() == Material.STICK) {
//				PlayerInventory inventory = p.getInventory();
//				
//				if (inventory.contains(Material.LEASH)) {
//					int i = inventory.first(Material.LEASH);
//					ItemStack stack = inventory.getItem(i);
//					stack.setAmount(stack.getAmount() - 1);
//					
//				}
//			}
//			
//			event.setCancelled(true);
//			Horse horse = (Horse) event.getRightClicked();
//			if (!caravanList.containsKey(p)) {
//				caravanList.put(p, horse);
//			} else {
//				caravanList.get(p).setLeashHolder(horse);
//				caravanList.remove(p);
//			}
//		}
	}
}
