package org.scapesoft.game.npc.familiar;

import org.scapesoft.game.WorldTile;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.actions.summoning.Pouches;

public class Thornysnail extends Familiar {

	/**
	 *
	 */
	private static final long serialVersionUID = -1147053487269627345L;

	public Thornysnail(Player owner, Pouches pouch, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

	@Override
	public String getSpecialName() {
		return "Slime Spray";
	}

	@Override
	public String getSpecialDescription() {
		return "Inflicts up to 80 damage against your opponent.";
	}

	@Override
	public int getBOBSize() {
		return 3;
	}

	@Override
	public int getSpecialAmount() {
		return 0;
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
