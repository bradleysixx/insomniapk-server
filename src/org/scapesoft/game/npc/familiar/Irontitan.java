package org.scapesoft.game.npc.familiar;

import org.scapesoft.game.WorldTile;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.actions.summoning.Pouches;

public class Irontitan extends Familiar {

	/**
	 *
	 */
	private static final long serialVersionUID = 6059371477618091701L;

	public Irontitan(Player owner, Pouches pouch, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

	@Override
	public String getSpecialName() {
		return "Iron Within";
	}

	@Override
	public String getSpecialDescription() {
		return "Inflicts three melee attacks instead of one in the next attack.";
	}

	@Override
	public int getBOBSize() {
		return 0;
	}

	@Override
	public int getSpecialAmount() {
		return 20;
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
