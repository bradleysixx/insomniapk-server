package org.scapesoft.game.minigames.games;


/**
 * @author Tyluur<itstyluur@gmail.com>
 * @since Nov 29, 2014
 */
public class ItemUpgrades {

	/**
	 * The array of items and the item they are upgraded to
	 */
	public static int[][] UPGRADE_ITEMS = new int[][] { { 8839, 19785 }, { 8840, 19786 } };

	/**
	 * Gets the base items (the items in the first column in the
	 * {@link #UPGRADE_ITEMS} 2d array.
	 * 
	 * @return
	 */
	public static int[] getBaseItems() {
		int[] items = new int[UPGRADE_ITEMS.length];
		for (int i = 0; i < UPGRADE_ITEMS.length; i++) {
			items[i] = UPGRADE_ITEMS[i][0];
		}
		return items;
	}

	/**
	 * Getting the upgraded item by the base id
	 * 
	 * @param itemId
	 *            The item id of the base item
	 * @return
	 */
	public static Integer getUpgradedItemByBase(int itemId) {
		for (int i = 0; i < UPGRADE_ITEMS.length; i++) {
			if (UPGRADE_ITEMS[i][0] == itemId) {
				return UPGRADE_ITEMS[i][1];
			}
		}
		return null;
	}

}
