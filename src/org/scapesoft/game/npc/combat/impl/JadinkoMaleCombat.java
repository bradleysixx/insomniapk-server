package org.scapesoft.game.npc.combat.impl;

import org.scapesoft.game.Animation;
import org.scapesoft.game.Entity;
import org.scapesoft.game.Graphics;
import org.scapesoft.game.Hit;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.npc.combat.CombatScript;
import org.scapesoft.game.npc.combat.NPCCombatDefinitions;
import org.scapesoft.utilities.misc.Utils;

public class JadinkoMaleCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return (new Object[] { Integer.valueOf(13822) });
	}

	@Override
	public int attack(NPC jM, Entity target) {
		int attack = Utils.getRandom(3);
		int hit = Utils.getRandom(600);
		int distanceX = target.getX() - jM.getX();
		int distanceY = target.getY() - jM.getY();
		NPCCombatDefinitions defs = jM.getCombatDefinitions();
		if (distanceX < -1 || distanceY < -1) {
			jM.setNextAnimation(new Animation(3215));
			jM.setNextGraphics(new Graphics(2716));
			target.setNextGraphics(new Graphics(2726));
			delayHit(jM, 2, target, new Hit[] { getMagicHit(jM, hit) });
		} else {
			switch (attack) {
				case 2:
				case 3:
				case 0:
					jM.setNextAnimation(new Animation(3214));
					delayHit(jM, 2, target, new Hit[] { getMeleeHit(jM, hit) });
					break;
				case 1:
					jM.setNextAnimation(new Animation(3215));
					jM.setNextGraphics(new Graphics(2716));
					target.setNextGraphics(new Graphics(2726));
					delayHit(jM, 2, target, new Hit[] { getMagicHit(jM, hit) });
					break;
			}
		}
		return defs.getAttackDelay();
	}
}