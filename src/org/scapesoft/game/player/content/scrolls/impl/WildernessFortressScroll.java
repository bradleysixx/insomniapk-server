package org.scapesoft.game.player.content.scrolls.impl;

import org.scapesoft.game.WorldTile;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.content.scrolls.ClueScroll;

/**
 * @author Lazarus <lazarus.rs.king@gmail.com>
 * @since Jun 22, 2014
 */
public class WildernessFortressScroll extends ClueScroll {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1123252474920266431L;

	@Override
	public String getName() {
		return null;
	}

	@Override
	public Integer getInformationInterface() {
		return 359;
	}

	@Override
	public Integer getAnimation() {
		return null;
	}

	@Override
	public String[] getHints() {
		return null;
	}

	@Override
	public Boolean completePrequisites(Player player) {
		return null;
	}

	@Override
	public WorldTile getActionTile() {
		return new WorldTile(3026, 3629, 0);
	}

}
