package org.scapesoft.game.npc.combat.impl;

import org.scapesoft.game.Animation;
import org.scapesoft.game.Entity;
import org.scapesoft.game.Graphics;
import org.scapesoft.game.World;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.npc.combat.CombatScript;
import org.scapesoft.game.npc.combat.NPCCombatDefinitions;

public class Karamel extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 3495 };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		npc.setNextAnimation(new Animation(-1));
		npc.setNextAnimation(new Animation(1979));
		int damage = getRandomMaxHit(npc, defs.getMaxHit(), NPCCombatDefinitions.MAGE, target);
		if (damage != 0) {
			target.setNextGraphics(new Graphics(369, 0, 100));
		}
		World.sendProjectile(npc, target, 368, 60, 32, 50, 50, 0, 0);
		delayHit(npc, 2, target, getMagicHit(npc, damage));
		return defs.getAttackDelay();
	}

}
