package org.scapesoft.game.npc.combat.impl;

import org.scapesoft.game.Animation;
import org.scapesoft.game.Entity;
import org.scapesoft.game.ForceTalk;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.npc.combat.CombatScript;
import org.scapesoft.game.npc.combat.NPCCombatDefinitions;
import org.scapesoft.utilities.misc.Utils;

public class OrkLegionCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { "Ork legion" };
	}

	public String[] messages = { "For Bork!", "Die Human!", "To the attack!", "All together now!" };

	@Override
	public int attack(NPC npc, Entity target) {
		final NPCCombatDefinitions cdef = npc.getCombatDefinitions();
		npc.setNextAnimation(new Animation(cdef.getAttackEmote()));
		if (Utils.getRandom(3) == 0) {
			npc.setNextForceTalk(new ForceTalk(messages[Utils.getRandom(messages.length > 3 ? 3 : 0)]));
		}
		delayHit(npc, 0, target, getMeleeHit(npc, cdef.getMaxHit()));
		return cdef.getAttackDelay();
	}

}
