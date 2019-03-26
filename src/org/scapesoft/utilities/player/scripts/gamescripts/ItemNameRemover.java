package org.scapesoft.utilities.player.scripts.gamescripts;

import java.io.File;
import java.io.IOException;

import org.scapesoft.cache.Cache;
import org.scapesoft.game.item.Item;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.player.Saving;
import org.scapesoft.utilities.player.scripts.GameScript;

/**
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since Feb 13, 2014
 */
public class ItemNameRemover extends GameScript {

	public static void main(String... args) throws IOException {
		Cache.init();
		for (File acc : getAccounts()) {
			try {
				Player player = (Player) Saving.loadSerializedFile(acc);
				if (player != null) {
					boolean modified = false;
					for (Item item : player.getInventory().getItems().toArray()) {
						if (item == null) {
							continue;
						}
						String name = item.getName().toLowerCase();
						for (String contained : CONTAINED_TO_REMOVE) {
							if (name.contains(contained.toLowerCase())) {
								player.getInventory().forceRemove(item.getId(), player.getInventory().getNumberOf(item.getId()));
								modified = true;
							}
						}
					}
					for (Item item : player.getEquipment().getItems().toArray()) {
						if (item == null) {
							continue;
						}
						String name = item.getName().toLowerCase();
						for (String contained : CONTAINED_TO_REMOVE) {
							if (name.contains(contained.toLowerCase())) {
								player.getEquipment().forceRemove(item.getId(), player.getEquipment().getItems().getNumberOf(item.getId()));
								modified = true;
							}
						}
					}
					for (Item item : player.getBank().getContainerCopy()) {
						if (item == null) {
							continue;
						}
						String name = item.getName().toLowerCase();
						for (String contained : CONTAINED_TO_REMOVE) {
							if (name.contains(contained.toLowerCase())) {
								player.getBank().forceRemove(item.getId());
								modified = true;
							}
						}
					}
					if (modified) {
						savePlayer(player, acc);
						System.out.println(acc.getName() + " had items and was deleted");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * If the items the player has contains these strings, it will be removed.
	 */
	private static final String[] CONTAINED_TO_REMOVE = { "morrigan", "vesta", "statius", "zuriel" };

}
