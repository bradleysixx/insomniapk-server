package org.scapesoft.game.player.content.scrolls;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.scapesoft.Constants;
import org.scapesoft.game.WorldTile;
import org.scapesoft.game.item.Item;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.dialogues.impl.ScrollRemoval;
import org.scapesoft.game.player.dialogues.impl.SimpleItemMessage;
import org.scapesoft.utilities.game.player.CoinCaskets;
import org.scapesoft.utilities.misc.ChatColors;
import org.scapesoft.utilities.misc.Utils;

/**
 * @author Lazarus <lazarus.rs.king@gmail.com>
 * @since Jun 21, 2014
 */
public class ClueScrollManager implements Serializable {

	public ClueScrollManager() {

	}

	/**
	 * Handles the reading of a scroll
	 * 
	 * @param item
	 *            The scroll
	 */
	public void read(Item item) {
		final ScrollType type = getScrollType(item);
		ClueScroll scroll = scrollMap.get(type);
		if (scroll == null) {
			scroll = ScrollSystem.getRandomScroll(type);
			scrollMap.put(type, scroll);
		}
		scroll.open(player);
		if (player.getAttributes().get("scroll_remove") != null) {
			player.getDialogueManager().startDialogue(ScrollRemoval.class, scroll, type);
		}
		if (Constants.DEBUG) {
			System.out.println(scroll.getClass().getSimpleName() + " - " + type);
		}
		player.getAttributes().put("scroll_remove", true);
	}

	/**
	 * Gets the first item in the inventory that is a scroll and finds its type
	 * 
	 * @return
	 */
	public ScrollType getInventoryScroll() {
		for (Item item : player.getInventory().getItems().toArray()) {
			if (item == null)
				continue;
			if (isScroll(item)) {
				return getScrollType(item);
			}
		}
		return null;
	}

	/**
	 * If the player's dig was successful
	 * 
	 * @return
	 */
	public boolean successfulDig() {
		ScrollType type = getInventoryScroll();
		if (type == null)
			return false;
		ClueScroll scroll = scrollMap.get(type);
		if (scroll == null)
			return false;
		if (scroll.getAnimation() != null)
			return false;
		if (!player.getLocation().matches(scroll.getActionTile())) {
			sendLocationInformation(scroll.getActionTile());
			return true;
		}
		onSuccess(scroll, type);
		return true;
	}

	/**
	 * Sends information about your location compared to the clue tile.
	 * 
	 * @param tile
	 */
	private void sendLocationInformation(WorldTile tile) {
		if (tile.withinDistance(player, 20)) {
			WorldTile ours = player;
			int distance = Utils.getDistance(ours, tile);
			player.sendMessage("You are " + distance + " step" + (distance == 1 ? "" : "s") + " away!");
		} else {
			player.sendMessage("<col=" + ChatColors.MAROON + ">You are too far away! Try again.");
		}
	}

	/**
	 * Deletes the scroll from the player's inventory
	 * 
	 * @param type
	 *            The type of scroll we're going to delete
	 * @return
	 */
	public boolean deleteScroll(ScrollType type) {
		Item scrollItem = null;
		for (Item item : player.getInventory().getItems().toArray()) {
			if (item == null)
				continue;
			if (isScroll(item)) {
				ScrollType scrollType = getScrollType(item);
				if (scrollType == type) {
					scrollItem = item;
					break;
				}
			}
		}
		if (!player.getInventory().contains(scrollItem.getId()))
			return false;

		player.getInventory().deleteItem(scrollItem);
		scrollMap.remove(type);
		return true;
	}

	/**
	 * Gives the player their reward upon successful completion of the scroll
	 * 
	 * @param scroll
	 * @param type
	 */
	public void onSuccess(ClueScroll scroll, ScrollType type) {
		Item scrollItem = null;
		for (Item item : player.getInventory().getItems().toArray()) {
			if (item == null)
				continue;
			if (isScroll(item)) {
				ScrollType scrollType = getScrollType(item);
				if (scrollType == type) {
					scrollItem = item;
					break;
				}
			}
		}
		if (!player.getInventory().contains(scrollItem.getId()))
			return;

		if (Utils.percentageChance((int) type.getPercentChance())) {
			completedCount[type.ordinal()]++;

			player.getInventory().deleteItem(scrollItem);
			addCasket(type);

			player.getDialogueManager().startDialogue(SimpleItemMessage.class, CoinCaskets.REWARDS.getItemId(), "You've found a casket!");
			player.sendMessage("Well done, you've completed the Treasure Trail.");
			int count = completedCount[type.ordinal()];
			player.sendMessage("<col=" + ChatColors.DARK_RED + ">You have completed " + count + " " + type.name().toLowerCase() + " Treasure Trail" + (count == 0 || count > 1 ? "s" : "") + ".");

			scrollMap.remove(type);
		} else {
			scrollMap.remove(type);

			player.getInventory().deleteItem(scrollItem);
			player.getInventory().addItem(new Item(scrollItem.getId(), 1));

			player.getDialogueManager().startDialogue(SimpleItemMessage.class, scrollItem.getId(), "You've found another clue scroll!");
			player.sendMessage("You've found another clue scroll!");
		}
	}

	/**
	 * Adds the casket to the players inventory by the type of scroll completed
	 * 
	 * @param type
	 *            The type of scroll completed
	 */
	private void addCasket(ScrollType type) {
		Caskets casket = Caskets.getCasketByName(type.name());
		if (casket == null) {
			throw new RuntimeException("Unknown casket - " + type);
		}
		player.getInventory().addItem(new Item(casket.getItemId(), 1));
	}

	/**
	 * Gives the player the rewards for the type of scroll
	 */
	public void giveRewards(Item item) {
		ScrollType type = getScrollType(item);
		if (type == null) {
			throw new RuntimeException("Could not find type for item " + item);
		}
		player.getInventory().deleteItem(item.getId(), 1);
		player.getDialogueManager().startDialogue(SimpleItemMessage.class, CoinCaskets.REWARDS.getItemId(), "You claim your reward from the " + item.getName().toLowerCase() + ".");

		final int itemCount = Utils.random(2, 5);
		final Item[] items = new Item[itemCount];
		Treasures treasures = Treasures.values()[type.ordinal()];
		List<Integer> possibleItems = treasures.getItemsList();
		for (int i = 0; i < itemCount; i++) {
			boolean rareList = true;
			if (rareList) {
				Collections.shuffle(possibleItems);
				items[i] = new Item(possibleItems.get(0), 1);
			} else {
				items[i] = JUNK_ITEMS[Utils.random(JUNK_ITEMS.length - 1)];
			}
		}
		player.getInterfaceManager().sendInterface(364);
		player.getPackets().sendItems(141, items);
		player.setCloseInterfacesEvent(new Runnable() {

			@Override
			public void run() {
				for (Item item : items) {
					player.getInventory().addDroppable(item);
				}
			}
		});
	}

	/**
	 * Gets the type of scroll the item is
	 * 
	 * @param item
	 *            The scroll
	 * @return
	 */
	public static ScrollType getScrollType(Item item) {
		String chars = new String();
		boolean start = false;
		for (char c : item.getName().toCharArray()) {
			if (c == '(') {
				start = true;
				continue;
			} else if (c == ')') {
				start = false;
				break;
			} else {
				if (start) {
					chars += "" + c;
				}
			}
		}
		return ScrollType.valueOf(chars.toUpperCase());
	}

	/**
	 * If the item is a clue scroll
	 * 
	 * @param item
	 *            The scroll
	 */
	public static boolean isScroll(Item item) {
		return item.getDefinitions().getName().toLowerCase().contains("clue scroll");
	}

	/**
	 * Handles the clue scrolls active when an emote is performed
	 * 
	 * @param buttonId
	 *            The button id of an emote
	 */
	public void handleEmote(int buttonId) {
		ScrollType type = getInventoryScroll();
		if (type == null)
			return;
		ClueScroll scroll = scrollMap.get(type);
		if (scroll == null)
			return;
		if (!player.getLocation().matches(scroll.getActionTile())) {
			sendLocationInformation(scroll.getActionTile());
			return;
		}
		if (scroll.getAnimation() == null)
			return;
		if (buttonId != scroll.getAnimation())
			return;
		if (!scroll.completePrequisites(player)) {
			return;
		}
		onSuccess(scroll, type);
	}

	/**
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * @param player
	 *            the player to set
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}

	/**
	 * The map of active scrolls, identified by the id of the scroll
	 */
	private Map<ScrollType, ClueScroll> scrollMap = new HashMap<ScrollType, ClueScroll>();

	/**
	 * The amount of scrolls completed
	 */
	private Integer[] completedCount = new Integer[] { 0, 0, 0, 0 };

	/**
	 * The player
	 */
	private transient Player player;

	/**
	 * The item id of the easy scroll
	 */
	public static final int EASY_SCROLL = 3490;

	/**
	 * The item id of the medium scroll
	 */
	public static final int MEDIUM_SCROLL = 2801;

	/**
	 * The item id of the hard scroll
	 */
	public static final int HARD_SCROLL = 2799;

	/**
	 * The item id of the elite scroll
	 */
	public static final int ELITE_SCROLL = 19064;

	/**
	 * The array of junk items that are possible to be received
	 */
	private static final Item[] JUNK_ITEMS = new Item[] { new Item(556, Utils.random(500, 1000)), // air
																									// runes
			new Item(995, Utils.random(100, 15000)), // coins
			new Item(1079, 1), // rune platebody
			new Item(1127, 1), // rune platelegs
			new Item(9185, 1), // rune crossbow
			new Item(1275, 1), // rune pickaxe
			new Item(2435, Utils.random(5, 15)), // prayer pots
			new Item(1273, 1), // mith pick
			new Item(1291), // bronze longsword
			new Item(1217), // black dagger
			new Item(10476, Utils.random(30, 100)), // purple sweets
			new Item(10327, Utils.random(10, 100)), // gnomish firelighters
			new Item(861), // magic short
			new Item(892, Utils.random(1, 15)), // rune arrows
			new Item(882, Utils.random(1, 100)), // bronze arrow
			new Item(336, Utils.random(10)), // raw trout
			new Item(1446), // body talisman
			new Item(1442), // fire talisman
			new Item(561, Utils.random(20, 50)), // nature runes
			new Item(2503), // black d'hide body
			new Item(565, Utils.random(20, 50)), // blood runes,
			new Item(1725), // str ammy
			new Item(1478), // accuracy ammy
			new Item(1727), // mage ammy
			new Item(841), // shortbow
			new Item(849), // willow shortbow
			new Item(558, Utils.random(20, 200)), // mind rune
			new Item(386, 10), // sharks
			new Item(332, Utils.random(1, 100)), // raw salmons
			new Item(1381), // air staff
			new Item(1383), // water staff
			new Item(1169), // coif
			new Item(860, Utils.random(10, 30)), // magic longbow
			new Item(7948) // burnt monkfish
	};

	/**
	 * 
	 */
	private static final long serialVersionUID = -5330866078999927724L;

}
