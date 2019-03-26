package org.scapesoft.game.npc.combat.impl;

import org.scapesoft.game.Entity;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.npc.combat.CombatScript;
import org.scapesoft.game.npc.combat.NPCCombatDefinitions;
import org.scapesoft.game.player.Player;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since May 4, 2014
 */
public class SappingGlacyte extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 14303 };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		if (target instanceof Player) {
			target.player().getPrayer().drainPrayer((int) (target.player().getPrayer().getPrayerpoints() * .1));
		}
		NPCCombatDefinitions defs = npc.getCombatDefinitions();
		return defs.getAttackDelay();
	}

}
