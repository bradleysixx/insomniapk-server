package org.scapesoft.utilities.game.player;

/**
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since Aug 11, 2013
 */
public enum CoinCaskets {
	
	/** The casket dropped when a monster dies */
	REWARDS(7237, 25000, 75000),
	
	REGULAR(405, 1000, 9000);

	CoinCaskets(int itemId, int baseCoinAmount, int extraCoinAmount) {
		this.itemId = itemId;
		this.baseCoinAmount = baseCoinAmount;
		this.extraCoinAmount = extraCoinAmount;
	}

	private final int itemId;
	private final int baseCoinAmount;
	private final int extraCoinAmount;

	/**
	 * @return the itemId
	 */
	public int getItemId() {
		return itemId;
	}

	/**
	 * @return the baseCoinAmount
	 */
	public int getBaseCoinAmount() {
		return baseCoinAmount;
	}

	/**
	 * @return the extraCoinAmount
	 */
	public int getExtraCoinAmount() {
		return extraCoinAmount;
	}

	/**
	 * Gets a casket by the item id
	 * 
	 * @param itemId
	 *            The id of the item
	 * @return
	 */
	public static CoinCaskets getCasket(int itemId) {
		for (CoinCaskets caskets : CoinCaskets.values())
			if (caskets.getItemId() == itemId)
				return caskets;
		return null;
	}
	
}