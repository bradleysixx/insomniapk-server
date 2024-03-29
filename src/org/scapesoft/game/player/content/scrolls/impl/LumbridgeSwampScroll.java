package org.scapesoft.game.player.content.scrolls.impl;

import org.scapesoft.game.WorldTile;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.content.scrolls.ClueScroll;

/**
 * @author Lazarus <lazarus.rs.king@gmail.com>
 * @since Jun 22, 2014
 */
public class LumbridgeSwampScroll extends ClueScroll {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4088691710193744678L;

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
		return new String[] { "Dance in the shack in Lumbridge Swamp.", "Equip a bronze dagger, iron full helmet, and a gold ring" };
	}

	@Override
	public Boolean completePrequisites(Player player) {
		return player.getEquipment().getWeaponId() == 1205 && player.getEquipment().getHatId() == 1153 && player.getEquipment().getRingId() == 1635;
	}

	@Override
	public WorldTile getActionTile() {
		return new WorldTile(3203, 3169, 0);
	}

}
