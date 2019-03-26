package org.scapesoft.game.npc.combat.impl;

import org.scapesoft.game.Animation;
import org.scapesoft.game.Entity;
import org.scapesoft.game.ForceTalk;
import org.scapesoft.game.Hit;
import org.scapesoft.game.Hit.HitLook;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.npc.combat.CombatScript;
import org.scapesoft.game.npc.combat.NPCCombatDefinitions;
import org.scapesoft.utilities.misc.Utils;

/**
 * 
 * @author Tyluur <itstyluur@gmail.com>
 * @since 2012-12-15
 */
public class DessousCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { "Dessous" };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		if (Utils.random(5) > 3) {
			delayHit(npc, 0, target, getMeleeHit(npc, getRandomMaxHit(npc, defs.getMaxHit(), NPCCombatDefinitions.MELEE, target)));
		} else {
			npc.setNextForceTalk(new ForceTalk("Hsssssssssss"));
			npc.setNextAnimation(new Animation(10501));
			target.applyHit(new Hit(target, 50, HitLook.REGULAR_DAMAGE));
			target.applyHit(new Hit(target, 50, HitLook.REGULAR_DAMAGE));
		}
		return defs.getAttackDelay();
	}

}
