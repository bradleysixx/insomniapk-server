package org.scapesoft.game.npc.combat.impl;

import org.scapesoft.game.Animation;
import org.scapesoft.game.Entity;
import org.scapesoft.game.World;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.npc.combat.CombatScript;
import org.scapesoft.game.npc.combat.NPCCombatDefinitions;
import org.scapesoft.utilities.misc.Utils;

public class DarkBeastCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 2783 };
	}

	@Override
	public int attack(NPC npc, final Entity target) {
		NPCCombatDefinitions def = npc.getCombatDefinitions();
		npc.setNextAnimation(new Animation(2731));
		if (Utils.isInRange(target.getX(), target.getY(), npc.getX(), npc.getY(), npc.getSize(), 3)) {
			delayHit(npc, 0, target, getMeleeHit(npc, getRandomMaxHit(npc, 170, def.getAttackStyle(), target)));
		} else {
			final int damage = getRandomMaxHit(npc, 90, def.getAttackStyle(), target);
			World.sendProjectile(npc, target, 2181, 41, 16, 41, 35, 16, 0);
			delayHit(npc, 2, target, getMagicHit(npc, damage));
		}
		return def.getAttackDelay();
	}
}
