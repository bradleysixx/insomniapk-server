package org.scapesoft.game.npc.combat.impl;

import org.scapesoft.game.Animation;
import org.scapesoft.game.Entity;
import org.scapesoft.game.Graphics;
import org.scapesoft.game.Hit;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.npc.combat.CombatScript;
import org.scapesoft.game.npc.combat.NPCCombatDefinitions;
import org.scapesoft.utilities.misc.Utils;

public class TokashCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { "To'Kash the Bloodchiller" };
	}

	@Override
	public int attack(final NPC npc, final Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		int attack = Utils.getRandom(3);
		int hit = Utils.getRandom(400 + npc.getCombatLevel());
		switch (attack) {
			case 2:
			case 3:
			case 0:
				npc.setNextAnimation(new Animation(14392));
				delayHit(npc, 2, target, new Hit[] { getMeleeHit(npc, hit) });
				break;
			case 1:
				npc.setNextAnimation(new Animation(14525));
				npc.setNextGraphics(new Graphics(3003));
				target.setNextGraphics(new Graphics(3005));
				delayHit(npc, 2, target, new Hit[] { getMagicHit(npc, hit + 100) });
				break;
		}
		return defs.getAttackDelay();
	}
}