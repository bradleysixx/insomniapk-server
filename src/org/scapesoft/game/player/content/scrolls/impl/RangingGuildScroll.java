package org.scapesoft.game.player.content.scrolls.impl;

import org.scapesoft.game.WorldTile;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.content.scrolls.ClueScroll;

/**
 * @author Lazarus <lazarus.rs.king@gmail.com>
 * @since Jun 22, 2014
 */
public class RangingGuildScroll extends ClueScroll {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4347513220184051311L;

	@Override
	public String getName() {
		return null;
	}

	@Override
	public Integer getInformationInterface() {
		return 349;
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
		return new WorldTile(2702, 3429, 0);
	}

}
