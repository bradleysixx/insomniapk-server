package org.scapesoft.game.npc.familiar;

import org.scapesoft.game.WorldTile;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.actions.summoning.Pouches;

public class Swamptitan extends Familiar {

	/**
	 *
	 */
	private static final long serialVersionUID = -6073150798974730997L;

	public Swamptitan(Player owner, Pouches pouch, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

	@Override
	public String getSpecialName() {
		return "Swamp Plague";
	}

	@Override
	public String getSpecialDescription() {
		return "Inflicts a magical attack on near by opponents and attempts to poison them as well.";
	}

	@Override
	public int getBOBSize() {
		return 0;
	}

	@Override
	public int getSpecialAmount() {
		return 6;
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.ENTITY;
	}

	@Override
	public boolean submitSpecial(Object object) {
		return false;
	}

}
