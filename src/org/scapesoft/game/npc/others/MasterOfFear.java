package org.scapesoft.game.npc.others;

import org.scapesoft.game.WorldTile;
import org.scapesoft.game.npc.NPC;

public class MasterOfFear extends NPC {

	/**
	 *
	 */
	private static final long serialVersionUID = -135987477797778800L;

	public MasterOfFear(int id, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		setName("SlayerMasterD of fear");
	}
}
