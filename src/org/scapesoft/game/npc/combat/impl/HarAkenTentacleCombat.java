package org.scapesoft.game.npc.combat.impl;

import org.scapesoft.game.Animation;
import org.scapesoft.game.Entity;
import org.scapesoft.game.World;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.npc.combat.CombatScript;
import org.scapesoft.game.npc.combat.NPCCombatDefinitions;
import org.scapesoft.utilities.misc.Utils;

public class HarAkenTentacleCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 15209, 15210 };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		int attackStyle = !Utils.isInRange(npc.getX(), npc.getY(), npc.getSize(), target.getX(), target.getY(), target.getSize(), 0) ? 1 : Utils.random(2);
		switch (attackStyle) {
		case 0:
			npc.setNextAnimation(new Animation(defs.getAttackEmote()));
			delayHit(npc, 0, target, getMeleeHit(npc, getRandomMaxHit(npc, defs.getMaxHit() - 36, NPCCombatDefinitions.MELEE, target)));
			break;
		case 1:
			npc.setNextAnimation(new Animation(npc.getId() == 15209 ? 16253 : 16242));
			World.sendProjectile(npc, target, npc.getId() == 15209 ? 3004 : 2922, 140, 35, 80, 35, 16, 0);
			if (npc.getId() == 15209) {
				delayHit(npc, 2, target, getRangeHit(npc, getRandomMaxHit(npc, defs.getMaxHit(), NPCCombatDefinitions.RANGE, target)));
			} else {
				delayHit(npc, 2, target, getMagicHit(npc, getRandomMaxHit(npc, defs.getMaxHit(), NPCCombatDefinitions.MAGE, target)));
			}
			break;
		}
		return defs.getAttackDelay();
	}
}
