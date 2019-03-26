package org.scapesoft.game.npc.combat.impl;

import org.scapesoft.game.Animation;
import org.scapesoft.game.Entity;
import org.scapesoft.game.World;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.npc.combat.CombatScript;
import org.scapesoft.game.npc.combat.NPCCombatDefinitions;
import org.scapesoft.utilities.misc.Utils;

public class TokHaarMej extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { "TzHaar-Mej", 15203 };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		int distanceX = target.getX() - npc.getX();
		int distanceY = target.getY() - npc.getY();
		int size = npc.getSize();
		int hit = 0;
		int attackStyle = Utils.random(2);
		if (attackStyle == 0 && (distanceX > size || distanceX < -1 || distanceY > size || distanceY < -1)) {
			attackStyle = 1;
		}
		switch (attackStyle) {
			case 0:
				hit = getRandomMaxHit(npc, defs.getMaxHit() - 36, NPCCombatDefinitions.MELEE, target);
				npc.setNextAnimation(new Animation(defs.getAttackEmote()));
				delayHit(npc, 0, target, getMeleeHit(npc, hit));
				break;
			case 1:
				hit = getRandomMaxHit(npc, defs.getMaxHit(), NPCCombatDefinitions.MAGE, target);
				npc.setNextAnimation(new Animation(16122));
				World.sendProjectile(npc, target, npc.getId() == 15203 ? 2991 : 2990, 34, 16, 30, 35, 16, 0);
				delayHit(npc, 2, target, getMagicHit(npc, hit));
				break;
		}
		return defs.getAttackDelay();
	}
}
