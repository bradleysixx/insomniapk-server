package org.scapesoft.game.player.content;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.scapesoft.cache.loaders.ClientScriptMap;
import org.scapesoft.game.item.Item;
import org.scapesoft.game.item.ItemConstants;
import org.scapesoft.game.npc.others.GraveStone;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.misc.Utils;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since Mar 27, 2014
 */
public class PlayerDeathInformation {

	/**
	 * Gets the items on death from the player
	 *
	 * @param player
	 *            The player
	 * @return
	 */
	public static Map<DeathType, List<Item>> getItemsOnDeath(Player player) {
		Map<DeathType, List<Item>> map = new HashMap<>();
		List<Item> keptItems = new ArrayList<Item>();
		List<Item> untradeable = new ArrayList<Item>();
		List<Item> containedItems = new ArrayList<Item>();
		for (int i = 0; i < 14; i++) {
			if (player.getEquipment().getItem(i) != null && player.getEquipment().getItem(i).getId() != -1 && player.getEquipment().getItem(i).getAmount() != -1) {
				containedItems.add(new Item(player.getEquipment().getItem(i).getId(), player.getEquipment().getItem(i).getAmount()));
			}
		}
		for (int i = 0; i < 28; i++) {
			if (player.getInventory().getItem(i) != null && player.getInventory().getItem(i).getId() != -1 && player.getInventory().getItem(i).getAmount() != -1) {
				containedItems.add(new Item(player.getInventory().getItem(i).getId(), player.getInventory().getItem(i).getAmount()));
			}
		}
		int keptAmount = 3;
		if (player.hasSkull()) {
			keptAmount = 0;
		}
		if (player.getPrayer().usingPrayer(0, 10) || player.getPrayer().usingPrayer(1, 0)) {
			keptAmount++;
		}
		Item lastItem = new Item(1, 1);
		for (int i = 0; i < keptAmount; i++) {
			ListIterator<Item> it$ = containedItems.listIterator();
			while (it$.hasNext()) {
				Item item = it$.next();
				if (!ItemConstants.isTradeable(item)) {
					untradeable.add(item);
					it$.remove();
					continue;
				}
				int price = item.getDefinitions().getValue();
				if (price >= lastItem.getDefinitions().getValue()) {
					lastItem = item;
				}
			}
			keptItems.add(lastItem);
			containedItems.remove(lastItem);
			lastItem = new Item(1, 1);
		}
		ListIterator<Item> it$ = keptItems.listIterator();
		while (it$.hasNext()) {
			Item item = it$.next();
			if (item.getId() == 1 && item.getAmount() == 1) {
				it$.remove();
			}
		}
		/** Enters the lists into the map */
		map.put(DeathType.UNTRADEABLE, untradeable);
		map.put(DeathType.KEPT, keptItems);
		map.put(DeathType.LOST, containedItems);
		return map;
	}

	public static void openItemsKeptOnDeath(Player player) {
		player.getInterfaceManager().sendInterface(17);
		sendItemsKeptOnDeath(player, false);
	}

	public static void sendItemsKeptOnDeath(Player player, boolean wilderness) {
		boolean skulled = player.hasSkull();
		Integer[][] slots = GraveStone.getItemSlotsKeptOnDeath(player, wilderness, skulled, player.getPrayer().isProtectingItem());
		Item[][] items = GraveStone.getItemsKeptOnDeath(player, slots);
		long riskedWealth = 0;
		long carriedWealth = 0;
		for (Item item : items[1]) {
			carriedWealth = riskedWealth += item.getDefinitions().getValue() * item.getAmount();
		}
		for (Item item : items[0]) {
			carriedWealth += item.getDefinitions().getValue() * item.getAmount();
		}
		if (slots[0].length > 0) {
			for (int i = 0; i < slots[0].length; i++) {
				player.getVarsManager().sendVarBit(9222 + i, slots[0][i]);
			}
			player.getVarsManager().sendVarBit(9227, slots[0].length);
		} else {
			player.getVarsManager().sendVarBit(9222, -1);
			player.getVarsManager().sendVarBit(9227, 1);
		}
		player.getVarsManager().sendVarBit(9226, wilderness ? 1 : 0);
		player.getVarsManager().sendVarBit(9229, skulled ? 1 : 0);
		StringBuffer text = new StringBuffer();
		text.append("The number of items kept on").append("<br>").append("death is normally 3.").append("<br>").append("<br>").append("<br>");
		if (wilderness) {
			player.getPackets().sendIComponentText(17, 19, "Ignoring untradeables, you will loose these items.");
			text.append("Your gravestone will not").append("<br>").append("appear.");
		} else {
			int time = GraveStone.getMaximumTicks(player.getGraveStone());
			int seconds = (int) (time * 0.6);
			int minutes = seconds / 60;
			seconds -= minutes * 60;

			text.append("Gravestone:").append("<br>").append(ClientScriptMap.getMap(1099).getStringValue(player.getGraveStone())).append("<br>").append("<br>").append("Initial duration:").append("<br>").append(minutes + ":" + (seconds < 10 ? "0" : "") + seconds).append("<br>");
		}
		text.append("<br>").append("<br>").append("Carried wealth:").append("<br>").append(carriedWealth > Integer.MAX_VALUE ? "Too high!" : Utils.getFormattedNumber((int) carriedWealth)).append("<br>").append("<br>").append("Risked wealth:").append("<br>").append(riskedWealth > Integer.MAX_VALUE ? "Too high!" : Utils.getFormattedNumber((int) riskedWealth)).append("<br>").append("<br>");
		if (wilderness) {
			text.append("Your hub will be set to:").append("<br>").append("Edgeville.");
		} else {
			text.append("Current hub: Home"); // TODO this
		}
		player.getPackets().sendGlobalString(352, text.toString());
	}

	/**
	 * a The types of items in the list of items on death
	 *
	 * @author Tyluur
	 */
	public enum DeathType {
		KEPT, LOST, UNTRADEABLE
	}

}
