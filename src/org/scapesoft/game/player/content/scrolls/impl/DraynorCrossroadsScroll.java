package org.scapesoft.game.player.content.scrolls.impl;

import org.scapesoft.game.WorldTile;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.content.scrolls.ClueScroll;

/**
 * @author Lazarus <lazarus.rs.king@gmail.com>
 * @since Jun 22, 2014
 */
public class DraynorCrossroadsScroll extends ClueScroll {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1019220683311929629L;

	@Override
	public String getName() {
		return null;
	}

	@Override
	public Integer getInformationInterface() {
		return null;
	}

	@Override
	public Integer getAnimation() {
		return DANCE;
	}

	@Override
	public String[] getHints() {
		return new String[] { "Dance at the crossroads north of Draynor.", "Equip an iron chain body, a sapphire ring, and a shortbow." };
	}

	@Override
	public Boolean completePrequisites(Player player) {
		return player.getEquipment().getChestId() == 1101 && player.getEquipment().getRingId() == 1637 && player.getEquipment().getWeaponId() == 841;
	}

	@Override
	public WorldTile getActionTile() {
		return new WorldTile(3111, 3296, 0);
	}

}
