package org.scapesoft.game.player.content.scrolls.impl;

import org.scapesoft.game.WorldTile;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.content.scrolls.ClueScroll;

/**
 * @author Lazarus <lazarus.rs.king@gmail.com>
 * @since Jun 22, 2014
 */
public class DruidsCircleScroll extends ClueScroll {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7714980872344343384L;

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
		return CHEER;
	}

	@Override
	public String[] getHints() {
		return new String[] { "Cheer at the Druid's Circle.", "Equip an air tiara, bronze two-handed sword, and gold amulet." };
	}

	@Override
	public Boolean completePrequisites(Player player) {
		return player.getEquipment().getHatId() == 5527 && player.getEquipment().getWeaponId() == 1307 && player.getEquipment().getAmuletId() == 1692;
	}

	@Override
	public WorldTile getActionTile() {
		return new WorldTile(2923, 3484, 0);
	}

}
