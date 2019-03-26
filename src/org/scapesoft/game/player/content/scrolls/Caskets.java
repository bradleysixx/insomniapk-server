package org.scapesoft.game.player.content.scrolls;

/**
 * @author Tyluur<itstyluur@gmail.com>
 * @since Nov 29, 2014
 */
public enum Caskets {

	ELITE(19039), HARD(13047), MEDIUM(13077), EASY(10223);

	Caskets(int itemId) {
		this.itemId = itemId;
	}

	/**
	 * Getting the item id of the casket
	 * 
	 * @return
	 */
	public int getItemId() {
		return itemId;
	}

	/**
	 * The item id of the casket
	 */
	private final int itemId;

	/**
	 * Getting the casket by an item id
	 * 
	 * @param itemId
	 * @return
	 */
	public static Caskets getCasketById(int itemId) {
		for (Caskets casket : Caskets.values()) {
			if (casket.getItemId() == itemId) {
				return casket;
			}
		}
		return null;
	}

	public static Caskets getCasketByName(String name) {
		for (Caskets casket : Caskets.values()) {
			if (casket.name().equalsIgnoreCase(name)) {
				return casket;
			}
		}
		return null;
	}

}
