package org.scapesoft.game.player.content.scrolls.impl;

import org.scapesoft.game.WorldTile;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.content.scrolls.ClueScroll;

/**
 * @author Lazarus <lazarus.rs.king@gmail.com>
 * @since Jun 22, 2014
 */
public class WizardsCausewayScroll extends ClueScroll {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5160852518254684224L;

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
		return CLAP;
	}

	@Override
	public String[] getHints() {
		return new String[] { "Clap on the causeway to the Wizard's Tower.", "Equip an iron medium helmet, an emerald ring, and leather gloves." };
	}

	@Override
	public Boolean completePrequisites(Player player) {
		return player.getEquipment().getHatId() == 1137 && player.getEquipment().getRingId() == 1639 && player.getEquipment().getGlovesId() == 1059;
	}

	@Override
	public WorldTile getActionTile() {
		return new WorldTile(3113, 3180, 0);
	}

}
