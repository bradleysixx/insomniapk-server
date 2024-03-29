package org.scapesoft.game.npc.combat.impl;

import org.scapesoft.game.Animation;
import org.scapesoft.game.Entity;
import org.scapesoft.game.Graphics;
import org.scapesoft.game.World;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.npc.combat.CombatScript;
import org.scapesoft.game.npc.combat.NPCCombatDefinitions;
import org.scapesoft.game.npc.familiar.Familiar;

public class IronTitanCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 7376, 7375 };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		int distanceX = target.getX() - npc.getX();
		int distanceY = target.getY() - npc.getY();
		boolean distant = false;
		int size = npc.getSize();
		Familiar familiar = (Familiar) npc;
		boolean usingSpecial = familiar.hasSpecialOn();
		int damage = 0;
		if (distanceX > size || distanceX < -1 || distanceY > size || distanceY < -1) {
			distant = true;
		}
		if (usingSpecial) {// priority over regular attack
			npc.setNextAnimation(new Animation(7954));
			npc.setNextGraphics(new Graphics(1450));
			if (distant) {// range hit
				delayHit(npc, 2, target, getMagicHit(npc, getRandomMaxHit(npc, 220, NPCCombatDefinitions.MAGE, target)), getMagicHit(npc, getRandomMaxHit(npc, 220, NPCCombatDefinitions.MAGE, target)), getMagicHit(npc, getRandomMaxHit(npc, 220, NPCCombatDefinitions.MAGE, target)));
			} else {// melee hit
				delayHit(npc, 1, target, getMeleeHit(npc, getRandomMaxHit(npc, 230, NPCCombatDefinitions.MELEE, target)), getMeleeHit(npc, getRandomMaxHit(npc, 230, NPCCombatDefinitions.MELEE, target)), getMeleeHit(npc, getRandomMaxHit(npc, 230, NPCCombatDefinitions.MELEE, target)));
			}
		} else {
			if (distant) {
				damage = getRandomMaxHit(npc, 255, NPCCombatDefinitions.MAGE, target);
				npc.setNextAnimation(new Animation(7694));
				World.sendProjectile(npc, target, 1452, 34, 16, 30, 35, 16, 0);
				delayHit(npc, 2, target, getMagicHit(npc, damage));
			} else {// melee
				damage = getRandomMaxHit(npc, 244, NPCCombatDefinitions.MELEE, target);
				npc.setNextAnimation(new Animation(7946));
				npc.setNextGraphics(new Graphics(1447));
				delayHit(npc, 1, target, getMeleeHit(npc, damage));
			}
		}
		return defs.getAttackDelay();
	}

}
