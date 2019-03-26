package org.scapesoft.game.npc.combat.impl;

import org.scapesoft.game.Entity;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.npc.combat.CombatScript;

public class BarricadeCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { "Barricade" };
	}

	/*
	 * empty
	 */
	@Override
	public int attack(NPC npc, Entity target) {
		return 0;
	}

}
