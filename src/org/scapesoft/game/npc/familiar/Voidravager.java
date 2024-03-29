package org.scapesoft.game.npc.familiar;

import org.scapesoft.game.WorldTile;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.actions.summoning.Pouches;
import org.scapesoft.game.player.content.Magic;

public class Voidravager extends Familiar {

	/**
	 *
	 */
	private static final long serialVersionUID = 3950385081972248371L;

	public Voidravager(Player owner, Pouches pouch, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

	@Override
	public String getSpecialName() {
		return "Call To Arms";
	}

	@Override
	public String getSpecialDescription() {
		return "Teleports the player to Void Outpost.";
	}

	@Override
	public int getBOBSize() {
		return 0;
	}

	@Override
	public int getSpecialAmount() {
		return 4;
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.CLICK;
	}

	@Override
	public boolean submitSpecial(Object object) {
		Magic.sendTeleportSpell((Player) object, 14388, -1, 1503, 1502, 0, 0, new WorldTile(2662, 2649, 0), 3, true, Magic.OBJECT_TELEPORT);
		return true;
	}
}
